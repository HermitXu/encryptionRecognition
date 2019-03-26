package com.spinfosec.system;

import com.spinfosec.utils.AESPython;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ApplicationProperty
 * @Description: 〈获取application.properties配置数据〉
 * @date 2018/10/25
 * All rights Reserved, Designed By SPINFO
 */

@Component
public class ApplicationProperty
{

	private static final String key = "crelync0356kk88z";

	/**
	 * 数据库名
	 */
	@Value("${datasource.name}")
	private String dataName;
	/**
	 * 数据库用户名
	 */
	@Value("${datasource.username}")
	private String dataUserName;

	/**
	 * 数据库密码
	 */
	@Value("${datasource.password}")
	private String dataPassWord;

	/**
	 * #安全日志归档，系统日志归档，数据备份恢复超时时间，单位：毫秒
	 */
	@Value("${logArchiveTimeOut}")
	private String logArchiveTimeOut;

	/**
	 * activeMQ-activemq.broker-url
	 */
	@Value("${activemq.broker-url}")
	private String activemqBrokerUrl;

	/**
	 * activeMQ-activemq.user
	 */
	@Value("${activemq.user}")
	private String activemqUser;

	/**
	 * activeMQ-activemq.password
	 */
	@Value("${activemq.password}")
	private String activemqPassword;

	/**
	 * activeMQ-activemq.ssl.key.store
	 */
	@Value("${activemq.ssl.key.store}")
	private String activemqSslKeyStore;

	/**
	 * activeMQ-activemq.ssl.key.password
	 */
	@Value("${activemq.ssl.key.password}")
	private String activemqSslKeyPassword;

	/**
	 * activeMQ-activemq.ssl.trust.store
	 */
	@Value("${activemq.ssl.trust.store}")
	private String activemqSslTrustStore;

	/**
	 * activeMQ-activemq.ssl.trust.password
	 */
	@Value("${activemq.ssl.trust.password}")
	private String activemqSslTrustPassword;

    /**
     * 供SE调用的thrift解析服务的端口
     */
    @Value("${thrift.se.invoke.port}")
    private String thriftSeInvokePort;

    /**
     * 调用文件解析服务的ip
     */
    @Value("${thrift.fileParse.ip}")
    private String thriftFileParseIp;

    /**
     * 调用文件解析服务的端口
     */
    @Value("${thrift.fileParse.port}")
    private String thriftFileParsePort;

    /**
     * 调用文件解析服务的超时时间
     */
    @Value("${thrift.fileParse.timeout}")
    private String thriftFileParseTimeout;

    /**
     * 加密文件分析器执行中线程池中线程等待时间，单位minute
     */
    @Value("${encrypt.analyze.thread.timeout}")
    private String encryptAnalyzeThreadTimeout;

	@Value("${archiveSyslogAlarmLimit}")
	private String archiveSyslogAlarmLimit;

    @Value("${thrift.ciphertext.recognition.ip}")
    private String thriftCiphertextRecognitionIp;

    @Value("${thrift.ciphertext.recognition.port}")
    private String thriftCiphertextRecognitionPort;

    @Value("${thrift.ciphertext.recognition.timeout}")
    private String thriftCiphertextRecognitionTimeout;

    @Value("${database.textdata.limit}")
    private String databaseTextdataLimit;

    @Value("${isAnalyzeByDirOfFileSystem}")
    private String isAnalyzeByDirOfFileSystem;

	public String getArchiveSyslogAlarmLimit()
	{
		return archiveSyslogAlarmLimit;
	}

	public void setArchiveSyslogAlarmLimit(String archiveSyslogAlarmLimit)
	{
		this.archiveSyslogAlarmLimit = archiveSyslogAlarmLimit;
	}

	public String getDataUserName()
	{
		return new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY,dataUserName));
	}

	public void setDataUserName(String dataUserName)
	{
		this.dataUserName = dataUserName;
	}

	public String getDataPassWord()
	{
		return new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY,dataPassWord));
	}

	public void setDataPassWord(String dataPassWord)
	{
		this.dataPassWord = dataPassWord;
	}

	public String getDataName()
	{
		return dataName;
	}

	public void setDataName(String dataName)
	{
		this.dataName = dataName;
	}

	public String getActivemqBrokerUrl()
	{
		return activemqBrokerUrl;
	}

	public void setActivemqBrokerUrl(String activemqBrokerUrl)
	{
		this.activemqBrokerUrl = activemqBrokerUrl;
	}

	public String getActivemqUser() throws Exception
	{
		return AESPython.decrypt(activemqUser, key);
	}

	public void setActivemqUser(String activemqUser)
	{
		this.activemqUser = activemqUser;
	}

	public String getActivemqPassword() throws Exception
	{
		return AESPython.decrypt(activemqPassword, key);
	}

	public void setActivemqPassword(String activemqPassword)
	{
		this.activemqPassword = activemqPassword;
	}

	public String getActivemqSslKeyStore()
	{
		return activemqSslKeyStore;
	}

	public void setActivemqSslKeyStore(String activemqSslKeyStore)
	{
		this.activemqSslKeyStore = activemqSslKeyStore;
	}

	public String getActivemqSslKeyPassword() throws Exception
	{
		return AESPython.decrypt(activemqSslKeyPassword, key);
	}

	public void setActivemqSslKeyPassword(String activemqSslKeyPassword)
	{
		this.activemqSslKeyPassword = activemqSslKeyPassword;
	}

	public String getActivemqSslTrustPassword() throws Exception
	{
		return AESPython.decrypt(activemqSslTrustPassword, key);
	}

	public void setActivemqSslTrustPassword(String activemqSslTrustPassword)
	{
		this.activemqSslTrustPassword = activemqSslTrustPassword;
	}

	public String getActivemqSslTrustStore()
	{
		return activemqSslTrustStore;
	}

	public void setActivemqSslTrustStore(String activemqSslTrustStore)
	{
		this.activemqSslTrustStore = activemqSslTrustStore;
	}

    public String getThriftSeInvokePort()
    {
        return thriftSeInvokePort;
    }

    public void setThriftSeInvokePort(String thriftSeInvokePort)
    {
        this.thriftSeInvokePort = thriftSeInvokePort;
    }

    public String getThriftFileParseIp()
    {
        return thriftFileParseIp;
    }

    public void setThriftFileParseIp(String thriftFileParseIp)
    {
        this.thriftFileParseIp = thriftFileParseIp;
    }

    public String getThriftFileParsePort()
    {
        return thriftFileParsePort;
    }

    public void setThriftFileParsePort(String thriftFileParsePort)
    {
        this.thriftFileParsePort = thriftFileParsePort;
    }

    public String getThriftFileParseTimeout()
    {
        return thriftFileParseTimeout;
    }

    public void setThriftFileParseTimeout(String thriftFileParseTimeout)
    {
        this.thriftFileParseTimeout = thriftFileParseTimeout;
    }

    public String getEncryptAnalyzeThreadTimeout()
    {
        return encryptAnalyzeThreadTimeout;
    }

    public void setEncryptAnalyzeThreadTimeout(String encryptAnalyzeThreadTimeout)
    {
        this.encryptAnalyzeThreadTimeout = encryptAnalyzeThreadTimeout;
    }

    public String getThriftCiphertextRecognitionIp()
    {
        return thriftCiphertextRecognitionIp;
    }

    public void setThriftCiphertextRecognitionIp(String thriftCiphertextRecognitionIp)
    {
        this.thriftCiphertextRecognitionIp = thriftCiphertextRecognitionIp;
    }

    public String getThriftCiphertextRecognitionPort()
    {
        return thriftCiphertextRecognitionPort;
    }

    public void setThriftCiphertextRecognitionPort(String thriftCiphertextRecognitionPort)
    {
        this.thriftCiphertextRecognitionPort = thriftCiphertextRecognitionPort;
    }

    public String getThriftCiphertextRecognitionTimeout()
    {
        return thriftCiphertextRecognitionTimeout;
    }

    public void setThriftCiphertextRecognitionTimeout(String thriftCiphertextRecognitionTimeout)
    {
        this.thriftCiphertextRecognitionTimeout = thriftCiphertextRecognitionTimeout;
    }


    public String getDatabaseTextdataLimit()
    {
        return databaseTextdataLimit;
    }

    public void setDatabaseTextdataLimit(String databaseTextdataLimit)
    {
        this.databaseTextdataLimit = databaseTextdataLimit;
    }

	public String getLogArchiveTimeOut()
	{
		return logArchiveTimeOut;
	}

	public void setLogArchiveTimeOut(String logArchiveTimeOut)
	{
		this.logArchiveTimeOut = logArchiveTimeOut;
	}

    public String getIsAnalyzeByDirOfFileSystem()
    {
        return isAnalyzeByDirOfFileSystem;
    }

    public void setIsAnalyzeByDirOfFileSystem(String isAnalyzeByDirOfFileSystem)
    {
        this.isAnalyzeByDirOfFileSystem = isAnalyzeByDirOfFileSystem;
    }
}
