package com.spinfosec.encryptAnalyze.analyor;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import com.spinfosec.dao.entity.SpDiscoveryTasks;
import com.spinfosec.dao.entity.SpTargetResDetail;
import com.spinfosec.dto.pojo.system.tatic.TargetResFormData;
import com.spinfosec.parse.bean.ResultItem;
import com.spinfosec.system.MemInfo;
import com.spinfosec.system.RspCode;
import com.spinfosec.thrift.dto.TransforDataInfo;
import com.spinfosec.utils.AESPython;
import com.spinfosec.utils.Contants;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author ank
 * @version v 1.0
 * @title [文件系统解析器]
 * @ClassName: com.spinfosec.encryptAnalyze.analyor.FileSystemAnalyze
 * @description [文件系统解析器，累计一定大小的文件或一定数量的文件后，然后统一调用密文识别服务，上报事件以文件目录为准]
 * @create 2019/1/16 11:19
 * @copyright Copyright(C) 2019 SHIPING INFO Corporation. All rights reserved.
 */
public class FileSystemAnalyze extends AnalyzeBase
{

    private  static Logger logger = LoggerFactory.getLogger(FileSystemAnalyze.class);

    private static final int SIZE_OF_COLLECTION = 80 *1024 * 1024;

    private static final int MAX_COUNT_OF_COLLECTION = 40000;


    // 保存每个策略下的每个目录路径下的文件个数
    protected static volatile Map<String, Map<String, Integer>> jobFileTotalCountMap = new HashMap<String, Map<String, Integer>>();
    // 保存每个策略下的每个目录路径下的已经扫描的文件体积
    protected static volatile Map<String, Map<String, Integer>> jobFileScanedSizeMap = new HashMap<String, Map<String, Integer>>();
    // 保存采集的数据
    protected static volatile Map<String, Map<String, List<String>>> jobFileListMap = new HashMap<String, Map<String, List<String>>>();
    // 保存已经采集足够数据的策略
    protected static volatile Map<String, List<String>> jobPathIsScanOk = new HashMap<String, List<String>>();
    // 保存每个策略下每个路径下失败的文件个数
    protected static volatile Map<String, Map<String, Integer>> jobFailedPathCount = new HashMap<String, Map<String, Integer>>();

    protected static volatile List<String> alreadyTatalCountJobId = new ArrayList<String>();

    private static synchronized void init(TransforDataInfo transforDataInfo, TargetResFormData targetResFormData, AnalyzeBase analyzeBase)
    {
        if (alreadyTatalCountJobId.contains(transforDataInfo.getJobId()))
        {
            logger.info("策略" + transforDataInfo.getJobId() + "下各个目录下文件数量已统计");
            return;
        }
        alreadyTatalCountJobId.add(transforDataInfo.getJobId());

        // 初始化各个路径下的文件总数
        Thread countDirectoryFilesThread = new Thread()
        {
            @Override
            public void run()
            {
                List<String> pathList = new ArrayList<String>();
                // 获取用户策略选择的目录路径
                List<SpTargetResDetail> targetResDetailList = targetResFormData.getTargetResDetailList();
                for (SpTargetResDetail spTargetResDetail : targetResDetailList)
                {
                    logger.info("开始统计策略"+transforDataInfo.getJobId()+"下路径"+spTargetResDetail.getPath()+"中的文件个数");
                    try
                    {
                        String path = spTargetResDetail.getPath();
                        // 获取path下的文件总数量
                        int countOfDir = ((FileSystemAnalyze) analyzeBase).getCountOfDir(path);
                        pathList.add(path);
                        logger.info("目录" + path + "下的文件数量为：" + countOfDir);
                        if (jobFileTotalCountMap.containsKey(transforDataInfo.getJobId()))
                        {
                            Map<String, Integer> fileTotalCountMap = jobFileTotalCountMap.get(transforDataInfo.getJobId());
                            if (null != fileTotalCountMap)
                            {
                                if (!fileTotalCountMap.containsKey(path))
                                {
                                    fileTotalCountMap.put(path, countOfDir);
                                }
                            }
                            else
                            {
                                fileTotalCountMap = new HashMap<String, Integer>();
                                fileTotalCountMap.put(path, countOfDir);
                                jobFileTotalCountMap.put(transforDataInfo.getJobId(), fileTotalCountMap);
                            }
                        }
                        else
                        {
                            Map<String, Integer> fileTotalCountMap = new HashMap<String, Integer>();
                            fileTotalCountMap.put(path, countOfDir);
                            jobFileTotalCountMap.put(transforDataInfo.getJobId(), fileTotalCountMap);
                        }

                        logger.info("路径" + path + "下文件个数已经统计完成， 开始调用处理");
                        // 文件数量统计完成后，调用一次处理流程，避免数据采集完成时没有达到标（体积和数量达到限值）准而停滞导致的事件漏报，但是需要判断如果已经处理过，则不需要在此处理
                        judgeIsDataCollectionOk(transforDataInfo, path, analyzeBase);
                        logger.info("路径" + path + "下文件个数已经统计完成， 调用处理结束");
                    }
                    catch (Exception e)
                    {
                        logger.error("捕获到异常", e);
                    }
                }
            }
        };
        countDirectoryFilesThread.start();
    }

    public FileSystemAnalyze(TransforDataInfo transforDataInfo, SpDiscoveryTasks spDiscoveryTasks, TargetResFormData targetResFormData)
    {
        super(transforDataInfo, spDiscoveryTasks, targetResFormData);
    }

    @Override
    public RspCode analyze()
    {
        logger.info("开始处理策略" + getDiscoveryTasks().getName() + "下路径" + getTransforDataInfo().getFileMetadata().getFilePath() + "的数据");

        // 首先进行静态对象的初始化操作
        init(getTransforDataInfo(), getTargetResFormData(), this);

        // 将文件解析后的文本放入到内存中，累计,以用户选择的主机资源中的路径为依据
        String pathKey = "";
        List<SpTargetResDetail> targetResDetailList = getTargetResFormData().getTargetResDetailList();
        if (null != targetResDetailList && !targetResDetailList.isEmpty())
        {
            for (SpTargetResDetail spTargetResDetail : targetResDetailList)
            {
                if (getTransforDataInfo().getFileMetadata().getFilePath().length() > spTargetResDetail.getPath().length() &&
                        getTransforDataInfo().getFileMetadata().getFilePath().startsWith(spTargetResDetail.getPath() + "/"))// 增加“/”结束符，避免出现相同的文件名上多几个字符的情况
                {
                    pathKey = spTargetResDetail.getPath();
                    break;
                }
            }
        }
        if (jobPathIsScanOk.containsKey(getTransforDataInfo().getJobId()) && jobPathIsScanOk.get(getTransforDataInfo().getJobId()).contains(pathKey))
        {
            logger.info("策略" + getTransforDataInfo().getJobId() + "下" + pathKey + "已经完成");
            return RspCode.INVOKE_BY_SE_OK;
        }
        
        // 调用文件解析服务，返回文本内容，保存至内存
        logger.info("调用文件解析服务对文件" + getTransforDataInfo().getFileMetadata().getFilePath() + "进行解析");
        byte[] fileContent = getTransforDataInfo().getFileContent();
        JSONObject result = parseBinaryToText(fileContent, getTransforDataInfo().getFileMetadata().getFileName());
        if (FILE_PARSE_CODE_SUCCESS == result.getInteger("code"))
        {
            List<ResultItem> list = (List<ResultItem>) result.get("result");
            if (null != list && !list.isEmpty())
            {
                StringBuilder sb = new StringBuilder();
                for (ResultItem resultItem : list)
                {
                    if (resultItem.code == 0)
                    {
                        String content = resultItem.getContent();
                        sb.append(content);
                    }
                }

                if (jobFileListMap.containsKey(getTransforDataInfo().getJobId()))
                {
                    Map<String, List<String>> fileListMap = jobFileListMap.get(getTransforDataInfo().getJobId());
                    if (null != fileListMap)
                    {
                        if (fileListMap.containsKey(pathKey))
                        {
                            fileListMap.get(pathKey).add(sb.toString());
                        }
                        else
                        {
                            List<String> contentList = new ArrayList<String>();
                            contentList.add(sb.toString());
                            fileListMap.put(pathKey, contentList);
                        }
                    }
                }
                else
                {
                    List<String> contentList = new ArrayList<String>();
                    contentList.add(sb.toString());
                    Map<String, List<String>> fileListMap = new HashMap<String, List<String>>();
                    fileListMap.put(pathKey, contentList);
                    jobFileListMap.put(getTransforDataInfo().getJobId(), fileListMap);
                }
                logger.info("路径" + pathKey + "已经累计了" + jobFileListMap.get(getTransforDataInfo().getJobId()).get(pathKey).size() + "个文件数据");

                // 统计已累计的大小
                if (jobFileScanedSizeMap.containsKey(getTransforDataInfo().getJobId()))
                {
                    Map<String, Integer> fileScanedSizeMap = jobFileScanedSizeMap.get(getTransforDataInfo().getJobId());
                    if (null != fileScanedSizeMap)
                    {
                        if (fileScanedSizeMap.containsKey(pathKey))
                        {
                            Integer size = null != fileScanedSizeMap.get(pathKey) ? fileScanedSizeMap.get(pathKey) : 0;
                            fileScanedSizeMap.put(pathKey, size + sb.toString().getBytes().length);
                        }
                        else
                        {
                            fileScanedSizeMap.put(pathKey, sb.toString().getBytes().length);
                        }
                    }
                    else
                    {
                        fileScanedSizeMap = new HashMap<String, Integer>();
                        fileScanedSizeMap.put(pathKey, sb.toString().getBytes().length);
                        jobFileScanedSizeMap.put(getTransforDataInfo().getJobId(), fileScanedSizeMap);
                    }
                }
                else
                {
                    Map<String, Integer> fileScanedSizeMap = new HashMap<String, Integer>();
                    fileScanedSizeMap.put(pathKey, sb.toString().getBytes().length);
                    jobFileScanedSizeMap.put(getTransforDataInfo().getJobId(), fileScanedSizeMap);
                }
                logger.info("路径" + pathKey + "已累计大小为：" + jobFileScanedSizeMap.get(getTransforDataInfo().getJobId()).get(pathKey) + "字节");
                return judgeIsDataCollectionOk(getTransforDataInfo(), pathKey, this);
            }
            else
            {
                if (jobFailedPathCount.containsKey(getTransforDataInfo().getJobId()))
                {
                    Map<String, Integer> failedPathMap = jobFailedPathCount.get(getTransforDataInfo().getJobId());
                    if (null != failedPathMap)
                    {
                        int failedSize = null != failedPathMap.get(pathKey) ? failedPathMap.get(pathKey) : 0;
                        failedPathMap.put(pathKey, failedSize + 1);
                    }
                    else
                    {
                        failedPathMap = new HashMap<String, Integer>();
                        failedPathMap.put(pathKey, 1);
                        jobFailedPathCount.put(getTransforDataInfo().getJobId(), failedPathMap);
                    }
                }
                else
                {
                    Map<String, Integer> failedPathMap = new HashMap<String, Integer>();
                    failedPathMap.put(pathKey, 1);
                    jobFailedPathCount.put(getTransforDataInfo().getJobId(), failedPathMap);
                }
            }
        }
        return null;
    }

    private static synchronized RspCode judgeIsDataCollectionOk(TransforDataInfo transforDataInfo, String pathKey, AnalyzeBase analyzeBase)
    {
        // 判断是否已经处理过了
        if (jobPathIsScanOk.containsKey(transforDataInfo.getJobId()) && jobPathIsScanOk.get(transforDataInfo.getJobId()).contains(pathKey))
        {
            logger.info("路径" + pathKey + "数据已累计完成，丢弃");
            return RspCode.INVOKE_BY_SE_OK;
        }

        // 开始做数据是否采集完成的判断  根据采集的数量或者采集的大小来判断
        // 获取已经采集的数量
        int scanedCount = 0;
        if (jobFileListMap.containsKey(transforDataInfo.getJobId()) && jobFileListMap.get(transforDataInfo.getJobId()).containsKey(pathKey))
        {
            scanedCount = jobFileListMap.get(transforDataInfo.getJobId()).get(pathKey).size();
        }
        // 获取已经采集的大小
        Integer scanedSize = 0;
        if (jobFileScanedSizeMap.containsKey(transforDataInfo.getJobId()) && jobFileScanedSizeMap.get(transforDataInfo.getJobId()).containsKey(pathKey))
        {
            scanedSize = jobFileScanedSizeMap.get(transforDataInfo.getJobId()).get(pathKey);
        }
        // 获取策略下某个路径下的文件总数
        Integer totalCount = MAX_COUNT_OF_COLLECTION;
        if (jobFileTotalCountMap.containsKey(transforDataInfo.getJobId()) && jobFileTotalCountMap.get(transforDataInfo.getJobId()).containsKey(pathKey))
        {
            totalCount = jobFileTotalCountMap.get(transforDataInfo.getJobId()).get(pathKey);
        }
        logger.info("路径" + pathKey + "已扫描的文件数量为" + scanedCount);
        // 获取失败的文件个数
        int failCount = 0;
        if (jobFailedPathCount.containsKey(transforDataInfo.getJobId()) && jobFailedPathCount.get(transforDataInfo.getJobId()).containsKey(pathKey))
        {
            failCount = null != jobFailedPathCount.get(transforDataInfo.getJobId()).get(pathKey) ? jobFailedPathCount.get(transforDataInfo.getJobId()).get(pathKey) : 0;
        }
        logger.info("路径" + pathKey + "采集失败的文件数量为" + failCount);
        if (scanedCount >= (totalCount - failCount))
        {
            // 该路径下的文件已经全部扫描完成
            logger.info("路径" + pathKey + "数据已经全部采集完成");
            // 开始调用密文识别算法
            processFileData(transforDataInfo, pathKey, analyzeBase);
            return RspCode.INVOKE_BY_SE_OK;
        }
        else if (scanedSize >= SIZE_OF_COLLECTION)
        {
            // 已经采集到足够多的文件
            logger.info("路径" + pathKey + "已经采集到足够大小的文件" + scanedSize + "kb");
            // 开始调用密文识别算法
            processFileData(transforDataInfo, pathKey, analyzeBase);
            return RspCode.INVOKE_BY_SE_OK;
        }
        else
        {
            logger.info("路径" + pathKey+"采集目标没有达成，继续");
            return RspCode.INVOKE_BY_SE_OK;
        }
    }

    private static synchronized RspCode processFileData(TransforDataInfo transforDataInfo, String pathKey, AnalyzeBase analyzeBase)
    {
        if (jobPathIsScanOk.containsKey(transforDataInfo.getJobId()))
        {
            List<String> scanJobIds = jobPathIsScanOk.get(transforDataInfo.getJobId());
            if (null != scanJobIds)
            {
                scanJobIds.add(pathKey);
            }
            else
            {
                scanJobIds = new ArrayList<String>();
                scanJobIds.add(pathKey);
                jobPathIsScanOk.put(transforDataInfo.getJobId(), scanJobIds);
            }
        }
        else
        {
            List<String> scanJobIds = new ArrayList<String>();
            scanJobIds.add(pathKey);
            jobPathIsScanOk.put(transforDataInfo.getJobId(), scanJobIds);
        }
        if (jobFileListMap.containsKey(transforDataInfo.getJobId()) && jobFileListMap.get(transforDataInfo.getJobId()).containsKey(pathKey))
        {
            List<String> list = jobFileListMap.get(transforDataInfo.getJobId()).get(pathKey);
            Map<String, String> resultMap = new HashMap<String, String>();
            try
            {
                resultMap = analyzeBase.ciphertextRecognition(list);
            }
            catch (Exception e)
            {
                logger.error(pathKey + "调用密文识别算法发生错误", e);
                resultMap = new HashMap<String, String>();
                resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY, ENCRYPT_STATUS_UNKNOW);
                resultMap.put(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY, "");
            }
            analyzeBase.generalEvent(resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_IS_ENCRYPT_KEY), resultMap.get(CIPHERTEXT_RECOGNITION_RESULT_ALGORITHM_KEY), null, pathKey, pathKey, null);
            // 数据清理
            logger.info("清理jobId:" + transforDataInfo.getJobId() + "下" + pathKey + "的数据");
            jobFileTotalCountMap.get(transforDataInfo.getJobId()).remove(pathKey);
            jobFileScanedSizeMap.get(transforDataInfo.getJobId()).remove(pathKey);
            jobFileListMap.get(transforDataInfo.getJobId()).remove(pathKey);
        }
        return RspCode.INVOKE_BY_SE_OK;
    }

    public static void clear(String jobId)
    {
        logger.info("开始清理" + jobId + "数据");
        jobFileTotalCountMap.remove(jobId);
        jobFileScanedSizeMap.remove(jobId);
        jobFileListMap.remove(jobId);
        jobPathIsScanOk.remove(jobId);
        jobFailedPathCount.remove(jobId);
        alreadyTatalCountJobId.remove(jobId);
    }


    private int getCountOfDir(String path)
    {
        int count = 0;

        // 对password做解密处理
        String password = getTargetResFormData().getTargetRes().getPassword();
        try
        {
            if (StringUtils.isNotEmpty(password))
            {
                password = AESPython.Decrypt(password, AESPython.SKEY);
            }
        }
        catch (Exception e)
        {
            logger.error("密码解密失败！", e);
        }

        String discoveryTaskType = getDiscoveryTasks().getDiscoveryTaskType();
        if (Contants.DISCOVERY_TASK_TYPE_FILE.equalsIgnoreCase(discoveryTaskType))
        {
            logger.info("获取共享目录下的数据的总数：" + path);
            String username = getTargetResFormData().getTargetRes().getUsername();
            String ip = getTargetResFormData().getTargetRes().getIp();
            count = getCountOfDirWithSmb(path, ip, username, password);
        }
        if (Contants.DISCOVERY_TASK_TYPE_FTP.equalsIgnoreCase(discoveryTaskType))
        {
            logger.info("获取Ftp下的数据的总数：" + path);
            String host = getTargetResFormData().getTargetRes().getIp();
            String port = getTargetResFormData().getTargetRes().getPort();
            String username = getTargetResFormData().getTargetRes().getUsername();
            path = path.substring(host.length() + 1); // ftp 的path需要截取掉ip
            count = getCountOfDirWithFtp(path, host, port, username, password);
        }
        if (Contants.DISCOVERY_TASK_TYPE_LINUX.equalsIgnoreCase(discoveryTaskType))
        {
            logger.info("获取Linux目录下的数据的总数：" + path);
            String host = getTargetResFormData().getTargetRes().getIp();
            String port = getTargetResFormData().getTargetRes().getPort();
            String username = getTargetResFormData().getTargetRes().getUsername();
            path = path.substring(host.length()); // path需要截取掉ip
            String keyName = getTargetResFormData().getTargetRes().getPublicKeyName();
            String priKey = MemInfo.getServletContextPath() + File.separator + "document" + File.separator + "sftpKey" + File.separator + keyName;
            count = getCountOfDirWithLinux(path, host, username, password, priKey, port);
        }
        logger.info("策略类型：" + discoveryTaskType + "下" + path + "路径下的文件总数为：" + count);
        return count;
    }

    private int getCountOfDirWithSmb(String path, String ip, String username, String password)
    {
        List<SmbFile> list = new ArrayList<SmbFile>();
        try
        {
            if (!path.endsWith("/"))
            {
                path = path + "/";
            }
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ip, username, password);
            String remotePath = "smb://" + path;
            SmbFile smbFile = new SmbFile(remotePath, auth);
            if (smbFile.isDirectory())
            {
                getCountOfDirWithSmb(smbFile, list);
            }
        }
        catch (Exception e)
        {
            logger.error("通过smb获取远程文件数量失败！", e);
        }
        return list.size();
    }

    private void getCountOfDirWithSmb(SmbFile dir, List<SmbFile> list) throws SmbException
    {
        SmbFile[] listFiles = dir.listFiles();
        if (null != listFiles && listFiles.length > 0)
        {
            for (SmbFile file : listFiles)
            {
                if (file.isFile())
                {
                    list.add(file);
                }
                else
                {
                    getCountOfDirWithSmb(file, list);
                }
            }
        }
    }

    private int getCountOfDirWithFtp(String path, String host, String port, String username, String password)
    {
        List<FTPFile> list = new ArrayList<FTPFile>();
        FTPClient ftpClient = null;
        try
        {
            ftpClient = new FTPClient();
            ftpClient.connect(host, Integer.parseInt(port));
            ftpClient.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            }
            else
            {
                logger.info("FTP连接成功。");
                ftpClient.setListHiddenFiles(true);
                ftpClient.enterLocalPassiveMode();
                getCountOfDirWithFtp(ftpClient, list, path);
                ftpClient.logout();
            }
        }
        catch (IOException e)
        {
            logger.error("ftp连接失败！", e);
        }
        finally
        {
            if (null != ftpClient)
            {
                try
                {
                    ftpClient.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return list.size();
    }

    private void getCountOfDirWithFtp(FTPClient ftpClient, List<FTPFile> list, String path) throws IOException
    {
        FTPFile[] ftpFiles;
        if (StringUtils.isEmpty(path))
        {
            ftpFiles = ftpClient.listFiles();
        }
        else
        {
            ftpFiles = ftpClient.listFiles(path);
        }
        if (null != ftpFiles && ftpFiles.length > 0)
        {
            for (FTPFile ftpFile : ftpFiles)
            {
                if (ftpFile.isFile())
                {
                    list.add(ftpFile);
                }
                else
                {
                    getCountOfDirWithFtp(ftpClient, list, (null != path ? path + "/" : "") + ftpFile.getName());
                }
            }
        }
    }


    private int getCountOfDirWithLinux(String path, String host, String username, String password, String priKey, String port)
    {
        List<ChannelSftp.LsEntry> list = new ArrayList<ChannelSftp.LsEntry>();
        Session session = null;
        ChannelSftp channelSftp = null;
        try
        {
            JSch jSch = new JSch();
            if (StringUtils.isNotEmpty(priKey))
            {
                jSch.addIdentity(priKey, password);
            }
            session = jSch.getSession(username, host, Integer.parseInt(port));
            if (null != session)
            {
                if (StringUtils.isNotEmpty(password))
                {
                    session.setPassword(password);
                }
                Properties config = new Properties();
                //第一次登陆
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect(10 * 60 * 1000);// 10分钟
                channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();

                getCountOfDirWithLinux(channelSftp, list, path);
            }
        }
        catch (Exception e)
        {
            logger.error("连接linux主机" + host + "失败！", e);
        }
        finally
        {
            channelSftp.disconnect();
            session.disconnect();
        }

        return list.size();
    }

    private void getCountOfDirWithLinux(ChannelSftp channelSftp, List<ChannelSftp.LsEntry> list, String path) throws SftpException
    {
        Vector ls = channelSftp.ls(path);
        Iterator iterator = ls.iterator();
        while (iterator.hasNext())
        {
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) iterator.next();
            if (!(lsEntry.getFilename().equalsIgnoreCase(".") || lsEntry.getFilename().equalsIgnoreCase("..")))
            {
                if (lsEntry.getAttrs().isDir())
                {
                    getCountOfDirWithLinux(channelSftp, list, path + "/" + lsEntry.getFilename());
                }
                else
                {
                    list.add(lsEntry);
                };
            }
        }
    }

    public static void main(String[] args)
    {
//        String path = "192.190.40.132/testshare_all/加密算法文件/";
//        int countOfDirWithSmb = FileSystemAnalyze.getCountOfDirWithSmb(path, "192.190.40.132", "administrator", "Spinfo0123");
//        String path = "192.190.10.110/share/train";
//        int countOfDirWithSmb = FileSystemAnalyze.getCountOfDirWithSmb(path, "192.190.10.110", "administrator", "Spinfo0");
//        System.out.println(countOfDirWithSmb);

//        String host = "192.190.10.110";
//        int countOfDirWithFtp = FileSystemAnalyze.getCountOfDirWithFtp("train", host, "21", "ftpTest", "Spinfo0123");
//        System.out.println(countOfDirWithFtp);

//        int countOfDirWithLinux = FileSystemAnalyze.getCountOfDirWithLinux("/root", "192.190.10.161", "root", "777", "C:\\Users\\Administrator\\Desktop\\id_rsa10.200", "8022");
//        System.out.println(countOfDirWithLinux);
    }
}