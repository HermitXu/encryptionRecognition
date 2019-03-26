package com.spinfosec.system;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dao.entity.SpSecPasswordPolicy;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.encryptAnalyze.EncryptParseServiceImpl;
import com.spinfosec.encryptAnalyze.analyor.AnalyzeTask;
import com.spinfosec.service.srv.IConfigPropertiesSrv;
import com.spinfosec.service.srv.ISecPasswordSrv;
import com.spinfosec.thrift.service.EncryptParseService;
import com.spinfosec.utils.Contants;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysInit
 * @Description: 〈系统初始化servlet〉
 * @date 2018/11/2
 * All rights Reserved, Designed By SPINFO
 */
@Component
public class SysInit implements ApplicationRunner
{

	private static final Logger log = LoggerFactory.getLogger(SysInit.class);

	@Autowired
	private ISecPasswordSrv secPasswordSrv;

	@Autowired
	private IConfigPropertiesSrv configPropertiesSrv;

    @Autowired
    ApplicationProperty property;

    @Autowired
    OperatorLogProperty operatorLogProperty;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Override
    public void run(ApplicationArguments args)
    {
		try
        {

			// resources文件路径
			ClassLoader classLoader = SysInit.class.getClassLoader();
			URL resources = classLoader.getResource("");
			String path = URLDecoder.decode(resources.getPath(), "utf-8").replaceFirst("/", "");

			// 初始化ServletContextPath
			MemInfo.setServletContextPath(path);

			// 初始化ServletContext
			MemInfo.setServletContext(webApplicationContext.getServletContext());

			// 保存邮箱设置
			List<SpConfigProperties> emailSetting = configPropertiesSrv.getSettingByGroupName(Contants.EMAIL_SETTINGS);
			for (SpConfigProperties spConfigProperties : emailSetting)
			{
				MemInfo.getEmailInfo().put(spConfigProperties.getName(), spConfigProperties.getValue());
			}

			// 保存密码安全策略信息
			SpSecPasswordPolicy spSecPasswordPolicy = secPasswordSrv.getSpSecPasswordPolicy();
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordValidity.name(),
					spSecPasswordPolicy.getPasswordValidity());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMin.name(),
					spSecPasswordPolicy.getPasswordLengthMin());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.passwordLengthMax.name(),
					spSecPasswordPolicy.getPasswordLengthMax());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.maxLoginTimes.name(),
					spSecPasswordPolicy.getMaxLoginTimes());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isModifyPasswordFirst.name(),
					spSecPasswordPolicy.getIsModifyPasswordFirst());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.ukeyEnable.name(),
					spSecPasswordPolicy.getUkeyEnable());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.isRepeatLogin.name(),
					spSecPasswordPolicy.getIsRepeatLogin());
			MemInfo.getSecPasswordPolicyInfo().put(SessionItem.sechostEnable.name(),
					spSecPasswordPolicy.getSechostEnable());

			// 初始化操作日志对象
            String operatorLogMap = operatorLogProperty.getOperatorLogMap();
            JSONObject jsonObject = JSONObject.parseObject(operatorLogMap);
            MemInfo.setOperatorLogObj(jsonObject);

			log.info("thrift通信服务端监听启动");
            Thread thriftAccept = new Thread()
            {
                @Override
                public void run()
                {
                    TServerTransport serverTransport = null;
                    TThreadPoolServer tThreadPoolServer = null;
                    try
                    {
                        int availableProcessors = Runtime.getRuntime().availableProcessors();
                        TProcessor tProcessor = new EncryptParseService.Processor<EncryptParseService.Iface>(new EncryptParseServiceImpl());
                        serverTransport = new TServerSocket(Integer.parseInt(property.getThriftSeInvokePort()));
                        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
                        args.processor(tProcessor);
                        args.protocolFactory(new TBinaryProtocol.Factory(true, true));
                        args.transportFactory(new TTransportFactory());
                        args.minWorkerThreads(availableProcessors);
                        args.maxWorkerThreads(256);
                        tThreadPoolServer = new TThreadPoolServer(args);
                        tThreadPoolServer.serve();
                    }
                    catch (TTransportException e)
                    {
                        log.error("启动thrift通信服务失败", e);
                    }
                    catch (Exception e)
                    {
                        log.error("启动thrift通信服务失败", e);
                    }
                    finally
                    {
                        if (null != tThreadPoolServer)
                        {
                            tThreadPoolServer.stop();
                        }
                        if (null != serverTransport)
                        {
                            serverTransport.close();
                        }
                    }
                }
            };
            thriftAccept.start();

            log.info("thrift通信服务端启动完成");

            log.info("解析器清理任务初始化");
            AnalyzeTask.doRun();

			log.info("系统初始化完成...");
		}
		catch (Exception e)
		{
			log.error("系统初始化失败...", e);
			System.exit(0);
		}

	}


}
