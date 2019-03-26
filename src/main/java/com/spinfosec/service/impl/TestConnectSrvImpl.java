package com.spinfosec.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.spinfosec.dao.entity.SpConfigProperties;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.ADConfig;
import com.spinfosec.dto.pojo.system.EmailConnectData;
import com.spinfosec.dto.pojo.system.LdapData;
import com.spinfosec.service.srv.IConfigPropertiesSrv;
import com.spinfosec.service.srv.ITestConnectSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Transport;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TestConnectSrvImpl
 * @Description: 〈测试连接业务接口实现类〉
 * @date 2018/10/30
 * All rights Reserved, Designed By SPINFO
 */
@Service("testConnectSrv")
public class TestConnectSrvImpl implements ITestConnectSrv
{
	private static int timeOut = 3000;

	private static final Logger log = LoggerFactory.getLogger(TestConnectSrvImpl.class);

	@Autowired
	private IConfigPropertiesSrv configPropertiesSrv;

	/**
	 * 测试连接邮箱
	 * @param connectData
	 * @return
	 */
	@Override
	public CodeRsp testEmail(EmailConnectData connectData)
	{
		Email email = new SimpleEmail();
		email.setHostName(connectData.getIp());
		email.setSmtpPort(Integer.parseInt(connectData.getPort()));
		String password = "";
		// 密码为空时从数据库中查询
		if (StringUtils.isEmpty(connectData.getPassword()))
		{
			List<SpConfigProperties> email_settings = configPropertiesSrv
					.getSettingByGroupName(Contants.EMAIL_SETTINGS);
			for (SpConfigProperties configProperties : email_settings)
			{
				if (configProperties.getName().equals("PASSWORD"))
				{
					password = configProperties.getValue();
					break;
				}
			}
		}
		else
		{
			password = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, connectData.getPassword()));
		}

		email.setAuthenticator(new DefaultAuthenticator(connectData.getEmail(), password));
		email.setSocketConnectionTimeout(timeOut);
		email.setSocketTimeout(timeOut);
		CodeRsp rsp = null;
		try
		{
			Transport transport = email.getMailSession().getTransport();
			transport.connect();
			boolean isConnect = transport.isConnected();
			transport.close();
			if (isConnect)
			{
				rsp = new CodeRsp(RspCode.SUCCESS);
			}
			else
			{
				rsp = new CodeRsp(RspCode.CONNECT_ERROR);
			}
		}
		catch (Exception e)
		{
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			e.printStackTrace(pw);
			if (StringUtils.containsIgnoreCase(e.toString(), "UnknownHostException"))
			{
				rsp = new CodeRsp(RspCode.CONNECT_HOST_ERROR);
			}
			else if (StringUtils.containsIgnoreCase(e.toString(), "ConnectException"))
			{
				rsp = new CodeRsp(RspCode.CONNECT_PORT_ERROR);
			}
			else if (StringUtils.containsIgnoreCase(e.toString(), "AuthenticationFailedException"))
			{
				rsp = new CodeRsp(RspCode.CONNECT_EMAIL_AUTH_ERROR);
			}
			else
			{
				rsp = new CodeRsp(RspCode.CONNECT_ERROR);
			}
			log.error("测试邮件服务器连接失败：" + writer.toString());
		}
		return rsp;
	}

	/**
	 * 测试连接Exchange
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Override
	public LdapContext connectExchange(ADConfig config) throws Exception
	{
		LdapContext ds = null;
		try
		{
			// 获取系统ad配置
			if (null == config)
			{
				config = getLdapConfig();
			}
			Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://" + config.getHost() + ":" + config.getPort());
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.REFERRAL, "ignore");
			String rootDN = config.getRootDN();
			StringBuilder sb = new StringBuilder();
			if (StringUtil.isNotEmpty(rootDN))
			{
				String[] split = rootDN.split(",");
				if (null != split && split.length > 0)
				{
					for (String info : split)
					{
                        if (info.contains("="))
                        {
                            String[] split2 = info.split("=");
                            if (sb.length() == 0)
                            {
                                sb.append(split2[1].trim());
                            }
                            else
                            {
                                sb.append(".").append(split2[1].trim());
                            }
                        }
                        else
                        {
                            throw new TMCException(RspCode.DOMAIN_CONNECT_FAIL);
                        }
					}
				}
			}
			else
			{
				throw new TMCException(RspCode.DOMAIN_CONNECT_FAIL);
			}
			String adminName = config.getAdminUser() + "@" + sb.toString();// username@domain
			env.put(Context.SECURITY_PRINCIPAL, adminName);
			env.put(Context.SECURITY_CREDENTIALS, config.getAdminPwd());
			// 是否使用SSL连接
			if (Boolean.valueOf(config.getUseSsl()))
			{
				env.put(Context.SECURITY_PROTOCOL, "ssl");
				env.put("java.naming.ldap.factory.socket", "com.spinfosec.utils.DummySSLSocketFactory");
			}
			ds = new InitialLdapContext(env, null);
			log.debug("ldap connected.");
		}
		catch (NamingException e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.DOMAIN_CONNECT_FAIL, e);
		}
		return ds;
	}

	/**
	 * Exchange连接获取信息
	 * @param ou
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<LdapData> getLdapUsers(String ou) throws Exception
	{
		LdapContext ds = null;
		try
		{
			ds = connectExchange(null);
			return searchUser(ds, ou, "*");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (null != ds)
			{
				ds.close();
			}
		}
	}

	/**
	 * 查询AD用户
	 * @param ds
	 * @param ou
	 * @param sAMAccountName
	 * @return
	 * @throws Exception
	 */
	private List<LdapData> searchUser(LdapContext ds, String ou, String sAMAccountName) throws Exception
	{

		log.debug("ldap Searching...");
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFilter = "(&(sAMAccountName=" + sAMAccountName + ")(mail=*))";

		ADConfig config = getLdapConfig();

		String searchBase = config.getRootDN();

		if (null != ou && !ou.isEmpty())
		{
			searchBase = ou + "," + searchBase;
		}
		String returnedAtts[] = { "cn", "uid", "sn", "telephonenumber", "name", "mail", "userPassword", "givenname",
				"address", "facsimiletelephonenumber", "displayName", "userPrincipalName", "sAMAccountName",
				"objectClass", "distinguishedName", "memberOf", "directReports" };
		searchCtls.setReturningAttributes(returnedAtts);

		int pageSize = 1000;
		byte[] cookie = null;
		List<LdapData> result = new ArrayList<LdapData>();
		ds.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.CRITICAL) });// 分页读取控制---因为LDAP
																										// 默认情况只能读取1000条数据
		do
		{
			NamingEnumeration<SearchResult> entries = ds.search(searchBase, searchFilter, searchCtls);
			while (entries.hasMoreElements())
			{
				SearchResult entry = entries.next();
				Attributes attributes = entry.getAttributes();
				Attribute attribute = attributes.get("objectclass");
				if (!attribute.contains("person"))
				{
					// 非用户则跳过
					continue;
				}
				Attributes attrs = entry.getAttributes();
				LdapData user = new LdapData();
				if (attrs != null)
				{
					for (NamingEnumeration<? extends Attribute> names = attrs.getAll(); names.hasMore();)
					{
						Attribute attr = names.next();
						String key = "";
						String value = "";
						for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();)
						{
							key = attr.getID();
							value = e.next().toString();
							if ("sAMAccountName".equals(key))
							{
								user.setsAMAccountName(value);
								user.setName(value);
							}
							else if ("cn".equals(key))
							{
								user.setCn(value);
							}
							else if ("mail".equals(key))
							{
								user.setEmail(value);
							}
							else if ("userPassword".equals(key))
							{
								user.setPassword(value);
							}
							else if ("distinguishedName".equals(key))
							{
								String[] split = value.split(",");
								for (String string : split)
								{
									if (string.contains("ou=") || string.contains("OU="))
									{
										user.setOu(string.split("=")[1]);
										break;
									}
								}
							}
						}
					}
				}

				result.add(user);
			}

			Control[] controls = ds.getResponseControls();
			if (controls != null)
			{
				for (int i = 0; i < controls.length; i++)
				{
					if (controls[i] instanceof PagedResultsResponseControl)
					{
						PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
						cookie = prrc.getCookie();
					}
					else
					{
					}
				}
			}

			ds.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
		}
		while (cookie != null);
		ds.close();
		log.debug("ldap Search complete.");
		return result;
	}

	/**
	 * 组装Exchange认证配置信息
	 * @return
	 */
	@Override
	public ADConfig getLdapConfig()
	{
		ADConfig config = new ADConfig();
		List<SpConfigProperties> confList = configPropertiesSrv.getSettingByGroupName("AUTHORSETTINGS");
		for (SpConfigProperties c : confList)
		{
			if ("ADDRESS".equals(c.getName()))
			{
				config.setHost(c.getValue());
			}
			else if ("PORT".equals(c.getName()))
			{
				config.setPort(Integer.parseInt(c.getValue()));
			}
			else if ("USERNAME".equals(c.getName()))
			{
				config.setAdminUser(c.getValue());
			}
			else if ("PASSWORD".equals(c.getName()))
			{
				config.setAdminPwd(new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, c.getValue())));
			}
			else if ("CONTEXT".equals(c.getName()))
			{
				config.setRootDN(c.getValue());
			}
			else if ("USE_SSL".equals(c.getName()))
			{
				config.setUseSsl(c.getValue());
			}
		}
		return config;
	}

}
