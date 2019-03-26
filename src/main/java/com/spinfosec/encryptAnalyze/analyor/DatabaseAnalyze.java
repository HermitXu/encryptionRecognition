package com.spinfosec.encryptAnalyze.analyor;

import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TableBinaryContent;
import com.spinfosec.thrift.dto.TableColumn;
import com.spinfosec.thrift.dto.TableContent;
import com.spinfosec.thrift.dto.TransforDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author ank
 * @version v 1.0
 * @title [数据库数据解析器]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.DatabaseAnalyze
 * @description [累计获取的数据，当非二进制数据达到40000时，统一调用密文解析算法，二进制数据不进行累积]
 * @create 2018/11/27 18:02
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class DatabaseAnalyze extends AnalyzeBase
{
    private Logger logger = LoggerFactory.getLogger(DatabaseAnalyze.class);

    // 非二进制时保存每张表的记录数
    private static Map<String, Long> tableRecordCountMap = new HashMap<String, Long>();
    private static Map<String, List<TableColumn>> tableColumnsMap = new HashMap<String, List<TableColumn>>();
    // 非二进制时保存每张表的数据
    private static Map<String, List<List<String>>> tableRowsMap = new HashMap<String, List<List<String>>>();
    // 记录数据表已经累计完成的表名，方便SE再次传来此表数据时做丢弃处理
    public static Map<String, List<String>> dropDataTableMap = new HashMap<String, List<String>>();

    public DatabaseAnalyze(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        super(transforDataInfo, spDiscoveryTasks, targetResFormData);
    }

    @Override
    public RspCode analyze()
    {
        logger.info("开始进行数据库数据的解析");
        TableContent tableContent = super.getTransforDataInfo().getTableContent();
        TableBinaryContent tableBinaryContent = super.getTransforDataInfo().getTableBinaryContent();
        if (null != tableBinaryContent && null != tableBinaryContent.getBinaryRow())
        {
            logger.info("开始解析数据库二进制数据");
            // 二进制数据处理，数据库二进制处理和文件二进制处理流程一致
            String pkColumnInfo = "";
            String pkColumnName = tableBinaryContent.getPkColumnName();
            String pkColumnValue = tableBinaryContent.getPkColumnValue();
            logger.info("二进制数据pkColumnName=" + pkColumnName);
            logger.info("二进制数据pkColumnValue=" + pkColumnValue);
            if (StringUtils.isNotEmpty(pkColumnName) && StringUtils.isNotEmpty(pkColumnValue))
            {
                pkColumnInfo = "(主键列:"+pkColumnName+",主键值:"+pkColumnValue+")";
            }
            return processParseBinaryToText(tableBinaryContent.getBinaryRow(), getTransforDataInfo().getFileMetadata().getFileName(), getTransforDataInfo().getTableBinaryContent().getColumnName() + pkColumnInfo);
        }
        else
        {
            logger.info("开始解析数据库非二进制数据");
            // 非二进制数据处理
            String tableName = tableContent.getTableName();
            String filePath = getTransforDataInfo().getFileMetadata().getFilePath();
            String databaseTableKey = tableName;
            if (StringUtils.isNotEmpty(filePath))
            {
                if (filePath.contains(tableName) && filePath.contains("/"))
                {
                    logger.info("filePath = " + filePath);
                    databaseTableKey = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.indexOf(tableName) + tableName.length());
                }
            }
            if (DatabaseAnalyze.dropDataTableMap.containsKey(getDiscoveryTasks().getId()) && DatabaseAnalyze.dropDataTableMap.get(getDiscoveryTasks().getId()).contains(databaseTableKey))
            {
                logger.info(databaseTableKey + "中数据未累计完成");
                // 数据没有查询到底部时返回正常，继续查询
                return RspCode.INVOKE_BY_SE_SCAN_STOP;
            }
            long total = tableContent.getTotal();
            logger.info("表" + databaseTableKey + "的记录数为：" + total);
            if (DatabaseAnalyze.tableRowsMap.containsKey(databaseTableKey))
            {
                List<List<String>> rows = DatabaseAnalyze.tableRowsMap.get(databaseTableKey);
                rows.addAll(tableContent.getRows());
            }
            else
            {
                List<List<String>> rows = tableContent.getRows();
                DatabaseAnalyze.tableRowsMap.put(databaseTableKey, rows);
                DatabaseAnalyze.tableRecordCountMap.put(databaseTableKey, total);
                DatabaseAnalyze.tableColumnsMap.put(databaseTableKey, tableContent.getColumns());
            }
            if (total == 0)
            {
                logger.info(databaseTableKey + "中数据为空！");
                // 数据为空时返回
                return RspCode.INVOKE_BY_SE_NO_DATA_FIND;
            }
            long countLimit = total > Long.parseLong(property.getDatabaseTextdataLimit()) ? Long.parseLong(property.getDatabaseTextdataLimit()) : total;
            if (DatabaseAnalyze.tableRowsMap.get(databaseTableKey).size() == countLimit)
            {
                logger.info(databaseTableKey + "中数据查询完毕，开始分析处理");
                // 该表数据查询完成时调用
                return processDbTextData(databaseTableKey);
            }
            else if (DatabaseAnalyze.tableRowsMap.get(databaseTableKey).size() < countLimit)
            {
                logger.info(databaseTableKey + "中数据未累计完成，请继续");
                // 数据没有查询到底部时返回正常，继续查询
                return RspCode.INVOKE_BY_SE_OK;
            }
            else
            {
                logger.info(databaseTableKey + "中数据未累计完成");
                if (!DatabaseAnalyze.dropDataTableMap.containsKey(getDiscoveryTasks().getId()))
                {
                    List<String> dropDataTableList = new ArrayList<String>();
                    dropDataTableList.add(databaseTableKey);
                    dropDataTableMap.put(getDiscoveryTasks().getId(), dropDataTableList);
                }
                else
                {
                    dropDataTableMap.get(getDiscoveryTasks().getId()).add(databaseTableKey);
                }
                return RspCode.INVOKE_BY_SE_SCAN_STOP;
            }
        }
    }

    /**
     * 处理非二进制数据
     * @param databaseTableKey
     */
    private RspCode processDbTextData(String databaseTableKey)
    {
        logger.info("获取" + databaseTableKey + "表中数据");
        List<List<String>> tnRows = DatabaseAnalyze.tableRowsMap.get(databaseTableKey);

        // table数据查询完整
        // 获取表格的columns信息
        logger.info("获取" + databaseTableKey + "表中列信息数据");
        List<TableColumn> tableColumns = DatabaseAnalyze.tableColumnsMap.get(databaseTableKey);
        if (tableColumns.isEmpty())
        {
            logger.info("获取列信息为空，返回");
            return RspCode.INVOKE_BY_SE_NO_COLUMN_DATA_FIND;
        }
        // 比较数据的列数和列信息的列数，取最小的作为循环次数，来按列数据去调用密文识别算法
        int minCount = tableColumns.size() < tnRows.get(0).size() ? tableColumns.size() : tnRows.get(0).size();
        Map<Integer, List<String>> indexContentMap = new HashMap<Integer, List<String>>();
        for (int i = 0; i < minCount; i++)
        {
            indexContentMap.put(i, new ArrayList<String>());
        }
        for (List<String> tnRow : tnRows)
        {
            for (int i = 0; i < minCount; i++)
            {
                if (StringUtils.isNotEmpty(tnRow.get(i)))
                {
                    indexContentMap.get(i).add(tnRow.get(i));
                }
            }
        }

        Set<Map.Entry<Integer, List<String>>> indexEntries = indexContentMap.entrySet();
        for (Map.Entry<Integer, List<String>> indexEntry : indexEntries)
        {
            Integer index = indexEntry.getKey();
            List<String> value = indexEntry.getValue();
            logger.info("minCount = " + minCount);
            logger.info("列索引index = " + index + ", tableColumns = " + tableColumns.size() + ", rowColumnSize = " + tnRows.get(0).size());
            String columnName = DatabaseAnalyze.tableColumnsMap.get(databaseTableKey).get(index).getName();
            logger.info("数据库非二进制数据开始调用密码识别算法：列：" + columnName);
            Map<String, String> resultMap = null;
            try
            {
                if (value.isEmpty())
                {
                    logger.info("列索引" + index + "，列名：" + columnName + "为空，跳过");
                    continue;
                }
                resultMap = ciphertextRecognition(value);
            }
            catch (Exception e)
            {
                logger.error("调用密文识别算法发生错误", e);
                return RspCode.INVOKE_BY_SE_CIPHERTEXT_RECOGNITION_ERROR;
            }
            generalEvent(resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY), resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY), columnName, getTransforDataInfo().getFileMetadata().getFilePath(), getTransforDataInfo().getFileMetadata().getFileName(), getTransforDataInfo().getFileMetadata().getFileType());
        }
        logger.info("清理map中的数据：key = " + databaseTableKey);
        DatabaseAnalyze.tableRowsMap.remove(databaseTableKey);
        DatabaseAnalyze.tableColumnsMap.remove(databaseTableKey);
        DatabaseAnalyze.tableRecordCountMap.remove(databaseTableKey);
        return RspCode.INVOKE_BY_SE_OK;
    }
}
