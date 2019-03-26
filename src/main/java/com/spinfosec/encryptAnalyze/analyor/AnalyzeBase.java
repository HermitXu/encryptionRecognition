package com.spinfosec.encryptAnalyze.analyor;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dao.entity.SpDscvrFiles;
import com.spinfosec.dao.entity.SpOrgUnitDict;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.encryptAnalyze.thrift.service.CiphertextRecognitionService;
import com.spinfosec.parse.bean.*;
import com.spinfosec.parse.service.base.thrift.ParseService;
import com.spinfosec.service.srv.IAlgorithmSrv;
import com.spinfosec.service.srv.IEventSrv;
import com.spinfosec.service.srv.IOrgService;
import com.spinfosec.system.ApplicationProperty;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TransforDataInfo;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.MIMETypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.*;

/**
 * @author ank
 * @version v 1.0
 * @title [解析器基类]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.AnalyzeBase
 * @description [解析器基类]
 * @create 2018/11/13 11:08
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public abstract class AnalyzeBase implements Runnable
{
    private Logger logger = LoggerFactory.getLogger(AnalyzeBase.class);

    // 文件解析返回状态码
    protected static final int FILE_PARSE_CODE_SUCCESS = 0;
    protected static final int FILE_PARSE_CODE_ERROR = 1;
    protected static final int FILE_PARSE_CODE_ENCRYPTED = 2;

    protected static final String ENCRYPT_STATUS_NO = "0";  // 未加密
    protected static final String ENCRYPT_STATUS_YES = "1";  // 已加密
    protected static final String ENCRYPT_STATUS_UNKNOW = "2";  // 未知

    protected static final String ENCRYPT_ALGORITHM_RESULT_NO = "未加密";
    protected static final String ENCRYPT_ALGORITHM_RESULT_UNKNOW = "未知";

    protected static final String CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY = "isEncrypt";
    protected static final String CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY = "algorithm";

    @Autowired
    private IEventSrv eventSrv = (IEventSrv) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("eventSrv");

    @Autowired
    private IAlgorithmSrv algorithmSrv = (IAlgorithmSrv) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("algorithmSrv");

    @Autowired
	private IOrgService orgService = (IOrgService) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("orgSrv");

    protected ApplicationProperty property = (ApplicationProperty) WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean("applicationProperty");

    private TransforDataInfo transforDataInfo = null;

    // 策略信息
    private SpDiscoveryTasks discoveryTasks;
    // 目标信息
    private TargetResFormData targetResFormData;

    public AnalyzeBase(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        this.transforDataInfo = transforDataInfo;
        this.discoveryTasks = spDiscoveryTasks;
        this.targetResFormData = targetResFormData;
    }

    @Override
    public void run()
    {
        analyze();
    }

    /**
     * 解析接口
     * @return
     */
    public abstract RspCode analyze();

    public RspCode processParseBinaryToText(byte[] fileContent, String fileName, String columnName)
    {
        JSONObject result = parseBinaryToText(fileContent, fileName);
        if (FILE_PARSE_CODE_SUCCESS == result.getInteger("code"))
        {
            // 解析成功
            // 调用密文解析算法
            List<ResultItem> list = (List<ResultItem>) result.get("result");
            if (null != list && !list.isEmpty())
            {
                for (ResultItem resultItem : list)
                {
                    if (resultItem.code == 0)
                    {
                        // 判断是否为空白，邮件无正文的时候过文件解析时返回空白
                        if (StringUtils.isNotBlank(resultItem.getContent()))
                        {
                            Map<String, String> resultMap = null;
                            try
                            {
                                // 如果是数据库中的二进制的压缩文件，顶层的文件名可能获取不到，会返回null，所以替换掉
                                String fileNameTemp = resultItem.getFileName();
                                if (StringUtils.isNotEmpty(fileNameTemp))
                                {
                                    fileNameTemp = fileNameTemp.replace("null", "");
                                    resultItem.setFileName(fileNameTemp);
                                }
                                logger.info("开始调用密文识别服务：" + resultItem.getFileName());
                                List<String> contentList = new ArrayList<String>();
                                contentList.add(resultItem.getContent());
                                resultMap = ciphertextRecognition(contentList);
                            }
                            catch (Exception e)
                            {
                                logger.error("文件" + resultItem.getFileName() + "调用密文识别算法发生错误", e);
//                            return RspCode.INVOKE_BY_SE_CIPHERTEXT_RECOGNITION_ERROR;
                                resultMap = new HashMap<String, String>();
                                resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY, ENCRYPT_STATUS_UNKNOW);
                                resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY, "");
                            }
                            String fileType = getTransforDataInfo().getFileMetadata().getFileType();
                            logger.info("fileType = " + fileType);
                            logger.info("resultItem.getMimeType() = " + resultItem.getMimeType());
                            logger.info("resultItem.getFileName() = " + resultItem.getFileName());
                            if (StringUtils.isEmpty(fileType))
                            {
                                fileType = MIMETypeUtil.getInstance().getFileTypByMimeType(resultItem.getMimeType());
                                logger.info("文件类型为空，通过mimeType来获取解析后的类型：" + fileType);
                            }
                            else
                            {
                                if (!resultItem.getFileName().toLowerCase().endsWith(fileType.substring(fileType.indexOf(".")).toLowerCase()))
                                {
                                    fileType = MIMETypeUtil.getInstance().getFileTypByMimeType(resultItem.getMimeType());
                                    logger.info("文件类型和文件名称不符，通过mimeType来获取解析后的类型：" + fileType);
                                }
                            }
                            generalEvent(resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY), resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY), columnName, getTransforDataInfo().getFileMetadata().getFilePath(), resultItem.getFileName(), fileType);
                        }
                        else
                        {
                            return RspCode.INVOKE_BY_SE_NO_DATA_FIND;
                        }
                    }
                    else
                    {
                        logger.info("单元文件" + resultItem.getFileName() + "解析结果：" + resultItem.getCode() + "," + resultItem.getMessage());
                        continue;
                    }
                }
                return RspCode.INVOKE_BY_SE_OK;
            }
            else
            {
                return RspCode.INVOKE_BY_SE_PARSE_FILE_ERROR;
            }
        }
        else if (FILE_PARSE_CODE_ENCRYPTED == result.getInteger("code"))
        {
            // 文档被加密
            generalEvent(ENCRYPT_STATUS_YES, null, null, getTransforDataInfo().getFileMetadata().getFilePath(), getTransforDataInfo().getFileMetadata().getFileName(), getTransforDataInfo().getFileMetadata().getFileType());
            return RspCode.INVOKE_BY_SE_OK;
        }
        else
        {
            // 解析失败

            return RspCode.INVOKE_BY_SE_PARSE_FILE_ERROR;
        }
    }


    /**
     * 通过文件解析服务将二进制解析为文本
     * @param fileContent 调用时的传输二进制数据
     * @param fileName 文件名称
     * @return List<String>
     */
    public JSONObject parseBinaryToText(byte[] fileContent, String fileName)
    {
        JSONObject result = new JSONObject();

        List<String> resultContent = new ArrayList<String>();
        TTransport tTransport = null;
        try
        {
            logger.info("开始调用文件解析服务：" + DateUtil.dateToString(new Date(System.currentTimeMillis()), DateUtil.DATETIME_FORMAT_PATTERN));
            tTransport = new TSocket(property.getThriftFileParseIp(), Integer.parseInt(property.getThriftFileParsePort()), Integer.parseInt(property.getThriftFileParseTimeout()));
            TProtocol tProtocol = new TBinaryProtocol(tTransport);
            ParseService.Iface client = new ParseService.Client(tProtocol);
            tTransport.open();
            ParseParams parseParams = new ParseParams();
            parseParams.setStream(fileContent);
            parseParams.setFileName(fileName);
            parseParams.setEnableOCR(false);
            parseParams.setEnableOfficeOCR(false);
            parseParams.setEnablePdfOCR(false);
            ParseResult parseResult = client.parse(parseParams);
            int code = parseResult.getCode();
            if (code == FILE_PARSE_CODE_SUCCESS)
            {
                logger.info("解析成功");
                // 解析成功
                List<ResultItem> items = parseResult.getItems();
                result.put("code", FILE_PARSE_CODE_SUCCESS);
                result.put("result", items);
            }
            else if (code == FILE_PARSE_CODE_ENCRYPTED)
            {
                // 此文档被加密，文件解析服务无法处理
                logger.info("待解析文档被加密");
                result.put("code", FILE_PARSE_CODE_ENCRYPTED);
            }
            else
            {
                logger.info("文件解析结果：" + parseResult.getCode() + "," + parseResult.getMessage());
                result.put("code", FILE_PARSE_CODE_ERROR);
            }
            logger.info("调用文件解析服务结束：" + DateUtil.dateToString(new Date(System.currentTimeMillis()), DateUtil.DATETIME_FORMAT_PATTERN));
        }
        catch (TException e)
        {
            logger.error("调用文件解析时发生调用错误：" + e.getMessage(), e);
            result.put("code", FILE_PARSE_CODE_ERROR);
        }
        catch (Exception e)
        {
            logger.error("调用文件解析时发生错误：" + e.getMessage(), e);
            result.put("code", FILE_PARSE_CODE_ERROR);
        }
        finally
        {
            if (null != tTransport)
            {
                tTransport.close();
            }
        }
        return result;
    }

    protected Map<String, String> ciphertextRecognition(List<String> list) throws Exception
    {
        Map<String, String> resultMap = new HashMap<String, String>();
        String algorithm = "";
        String isEncrypt = ENCRYPT_STATUS_UNKNOW;
        double algorithmScore = 0L;
        TTransport tTransport = null;
        try
        {
            logger.info("开始调用密文识别算法：" + DateUtil.dateToString(new Date(System.currentTimeMillis()), DateUtil.DATETIME_FORMAT_PATTERN));
            tTransport = new TSocket(property.getThriftCiphertextRecognitionIp(), Integer.parseInt(property.getThriftCiphertextRecognitionPort()), Integer.parseInt(property.getThriftCiphertextRecognitionTimeout()));
            TProtocol tProtocol = new TBinaryProtocol(tTransport);
            CiphertextRecognitionService.Iface client = new CiphertextRecognitionService.Client(tProtocol);
            tTransport.open();
            Map<String, Double> result = client.ciphertextRecognition(list);
            logger.info("密文识别结果：" + result.toString());
            Double plain = result.get("Plain");
            if (null != plain)
            {
                logger.info("密文识别算法结果Plain:" + plain.intValue());
                if (plain.intValue() == 0)
                {
                    // 加密
                    isEncrypt = ENCRYPT_STATUS_YES;
                    result.remove("Plain");
                    Set<Map.Entry<String, Double>> entries = result.entrySet();
                    for (Map.Entry<String, Double> entry : entries)
                    {
                        if (entry.getValue() > algorithmScore)
                        {
                            algorithm = entry.getKey();
                        }
                    }
                }
                else
                {
                    // 未加密
                    isEncrypt = ENCRYPT_STATUS_NO;
                    algorithm = ENCRYPT_ALGORITHM_RESULT_NO;
                }
            }
            resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY, isEncrypt);
            resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY, algorithm);
            logger.info("调用密文识别算法结束：" + DateUtil.dateToString(new Date(System.currentTimeMillis()), DateUtil.DATETIME_FORMAT_PATTERN));
        }
        catch (TTransportException e)
        {
            logger.error("调用密文识别算法发生调用错误：" + e.getMessage(), e);
            throw e;
        }
        catch (TException e)
        {
            logger.error("调用密文识别算法发生调用错误：" + e.getMessage(), e);
            throw e;
        }
        catch (Exception e)
        {
            logger.error("调用密文识别算法发生错误：" + e.getMessage(), e);
            throw e;
        }
        finally
        {
            if (null != tTransport)
            {
                tTransport.close();
            }
        }
        return resultMap;
    }

    /**
     * 生成事件入库
     * @param isEncrypt 0 未加密   1  加密   2 未知
     * @param algorithm 算法名称或空，空为未知
     * @Param columnName 列名
     */
    public void generalEvent(String isEncrypt, String algorithm, String columnName, String realPath, String realFileName, String fileType)
    {
        logger.info("开始上报事件，算法：" + algorithm);

        SpOrgUnitDict orgUnit = orgService.getOrgById(discoveryTasks.getBeCheckedOrgId());

        // 根据算法名称查询算法信息
        SpDscvrFiles spDscvrFiles = new SpDscvrFiles();
        String id = GenUtil.getUUID();
        spDscvrFiles.setId(id);
        spDscvrFiles.setAlgorithmType(StringUtils.isNotEmpty(algorithm) ? algorithm : ENCRYPT_ALGORITHM_RESULT_UNKNOW);
        spDscvrFiles.setDatabaseName(targetResFormData.getTargetRes().getDatabaseName());
        spDscvrFiles.setDatabaseType(targetResFormData.getTargetRes().getDatabaseType());
        Date currentDate = DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN);
        spDscvrFiles.setDetectDateTs(currentDate);
        spDscvrFiles.setFdateAccessedTs(DateUtil.stringToDate(transforDataInfo.getFileMetadata().getAccessTime(), DateUtil.DATETIME_FORMAT_PATTERN));
        spDscvrFiles.setFdateCreatedTs(DateUtil.stringToDate(transforDataInfo.getFileMetadata().getCreateTime(), DateUtil.DATETIME_FORMAT_PATTERN));
        spDscvrFiles.setFdateModifiedTs(DateUtil.stringToDate(transforDataInfo.getFileMetadata().getModifyTime(), DateUtil.DATETIME_FORMAT_PATTERN));
        spDscvrFiles.setFileExtension(fileType);
        spDscvrFiles.setFileName(realFileName);
        spDscvrFiles.setFilePath(realPath + (StringUtils.isNotEmpty(columnName) ? "#" + columnName :""));
        spDscvrFiles.setHostName(targetResFormData.getTargetRes().getIp());
        spDscvrFiles.setIncExternalId(id);
        spDscvrFiles.setIp(targetResFormData.getTargetRes().getIp());
        spDscvrFiles.setJobId(discoveryTasks.getId());
        spDscvrFiles.setIsAuthorized(0);
        spDscvrFiles.setIsEncrypt(Integer.parseInt(isEncrypt));
        spDscvrFiles.setJobName(discoveryTasks.getName());
        spDscvrFiles.setOrgId(discoveryTasks.getBeCheckedOrgId());
        spDscvrFiles.setOrgName(orgUnit.getName());
        spDscvrFiles.setSendTime(currentDate);
        spDscvrFiles.setTaskType(discoveryTasks.getDiscoveryTaskType());
        spDscvrFiles.setCreatedBy(discoveryTasks.getCreatedBy());
        eventSrv.saveDscvrFiles(spDscvrFiles);
    }

    public TransforDataInfo getTransforDataInfo()
    {
        return transforDataInfo;
    }

    public SpDiscoveryTasks getDiscoveryTasks()
    {
        return discoveryTasks;
    }

    public TargetResFormData getTargetResFormData()
    {
        return targetResFormData;
    }
}
