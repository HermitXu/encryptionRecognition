package com.spinfosec.intercept;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.SysOperateLogDao;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.system.BodyReaderHttpServletRequestWrapper;
import com.spinfosec.system.MemInfo;
import com.spinfosec.utils.DateUtil;
import com.spinfosec.utils.GenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ank
 * @version v 1.0
 * @title [操作日志拦截器]
 * @ClassName: com.spinfosec.intercept.OperatorLogIntercept
 * @description [操作日志拦截器]
 * @create 2018/11/22 12:02
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class OperatorLogIntercept implements HandlerInterceptor
{
    public static final String ACCETP_DATA_METHOD_PATH_VARIABLE = "PathVariable";
    public static final String ACCEPT_DATA_METHOD_REQUEST_BODY = "RequestBody";
    public static final String ACCETP_DATA_METHOD_FROM_REQUEST = "FromRequest";
    public static final String ACCEPT_DATA_METHOD_OTHER = "Other";
    public static final String REGEX_SPLIT_DATA = "\\$[\\w.]+\\$";
    public static final String REGEX_SPLIT_SESSION = "\\#[\\w.]+\\#";
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(OperatorLogIntercept.class);

    @Autowired
    private SysOperateLogDao sysOperateLogDao;

    private String userId;
    private String userName;
    private String roleId;
    private String roleName;

    private SpSystemOperateLogInfo spSystemOperateLogInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String uri = request.getRequestURI();
        if (uri.contains("swagger"))
        {
            return true;
        }
        logger.info("进入日志拦截器");
        userId = (String) request.getSession().getAttribute(SessionItem.userId.name());
        userName = (String) request.getSession().getAttribute(SessionItem.userName.name());
        roleId = (String) request.getSession().getAttribute(SessionItem.roleId.name());
        roleName = (String) request.getSession().getAttribute(SessionItem.roleName.name());

        try
        {
            // 拦截器，controller调用之后，渲染页面之前调用
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取请求的controller类
            Class<?> beanType = handlerMethod.getBeanType();
            String classFullName = beanType.getName();
            String methodName = handlerMethod.getMethod().getName();
            String opKey = classFullName + "." + methodName;
            logger.info("请求的方法：" + opKey);
            JSONObject jsonObject = MemInfo.getOperatorLogObj().getJSONObject(opKey);
            if (null != jsonObject)
            {
                // 只记录operatorLogDefinition.properties文件中定义的key的操作日志
                String msg = jsonObject.getString("msg");
                List<String> msgList = new ArrayList<String>();
                String acceptDataMethod = jsonObject.getString("acceptDataMethod");
                String acceptDataType = jsonObject.getString("acceptDataType");
                logger.info("请求的方式：" + acceptDataMethod);
                if (ACCETP_DATA_METHOD_PATH_VARIABLE.equalsIgnoreCase(acceptDataMethod))
                {
                    // 删除操作等
                    String key = jsonObject.getString("key");
                    Map valueMap = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    String value = (String) valueMap.get(key);
                    processIdsValueData(jsonObject, msg, msgList, value);
                }
                else if (ACCEPT_DATA_METHOD_REQUEST_BODY.equalsIgnoreCase(acceptDataMethod))
                {
                    // 新增，修改
                    BodyReaderHttpServletRequestWrapper bodyReaderHttpServletRequestWrapper = new BodyReaderHttpServletRequestWrapper(request);
                    String bodyString = bodyReaderHttpServletRequestWrapper.getBodyString();
                    List<JSONObject> jsonObjList = new ArrayList<JSONObject>();

                    if ("idArray".equalsIgnoreCase(acceptDataType))
                    {
                        processIdsValueData(jsonObject, msg, msgList, bodyString);
                    }
                    else if ("idArrayObject".equalsIgnoreCase(acceptDataType))
                    {
                        JSONObject object = JSONObject.parseObject(bodyString);
                        String key = jsonObject.getString("key");
                        JSONArray array = object.getJSONArray(key);
                        String value = "";
                        for (Object id : array)
                        {
                            String idStr = (String) id;
                            if (value.length() == 0)
                            {
                                value += idStr;
                            }
                            else
                            {
                                value += "," + idStr;
                            }
                        }
                        processIdsValueData(jsonObject, msg, msgList, value);
                    }
					else if ("id".equalsIgnoreCase(acceptDataType))
					{
						JSONObject object = JSONObject.parseObject(bodyString);
						String key = jsonObject.getString("key");
						String value = object.getString(key);
						processIdsValueData(jsonObject, msg, msgList, value);
					}
                    else
                    {
                        if ("Array".equalsIgnoreCase(acceptDataType))
                        {
                            JSONArray jsonArray = JSONObject.parseArray(bodyString);
                            for (int i = 0; i < jsonArray.size(); i++)
                            {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                jsonObjList.add(jsonObj);
                            }
                        }
                        else if ("Object".equalsIgnoreCase(acceptDataType))
                        {
                            JSONObject requestBodyJsonObj = JSONObject.parseObject(bodyString);
                            jsonObjList.add(requestBodyJsonObj);
                        }

                        if (null != jsonObjList && !jsonObjList.isEmpty())
                        {
							JSONObject srcJsonObj = new JSONObject();
                            for (JSONObject jsonObj : jsonObjList)
                            {
								srcJsonObj = jsonObj;
                                Pattern pattern = Pattern.compile(REGEX_SPLIT_DATA);
                                Matcher matcher = pattern.matcher(msg);
                                while (matcher.find())
                                {
                                    String group = matcher.group();
                                    String propertyName = group.substring(1, group.length() - 1);
                                    if (propertyName.contains("."))
                                    {
                                        String[] propertyNames = propertyName.split("\\.");
                                        for (int i = 0; i < propertyNames.length; i++)
                                        {
                                            String name = propertyNames[i];
                                            if (i == propertyNames.length - 1)
                                            {
                                                String propertyValue = jsonObj.getString(name);
                                                msg = msg.replace(group, propertyValue);
                                            }
                                            else
                                            {
                                                jsonObj = jsonObj.getJSONObject(name);
												if (jsonObj == null)
												{
													jsonObj = srcJsonObj.getJSONObject(name);
												}
                                            }
                                        }
                                    }
                                    else
                                    {
                                        String propertyValue = jsonObj.getString(propertyName);
                                        msg = msg.replace(group, propertyValue);
                                    }
                                }
                                msgList.add(msg);
                            }
                        }
                    }
                }
                else if (ACCETP_DATA_METHOD_FROM_REQUEST.equalsIgnoreCase(acceptDataMethod))
                {
                    // 获取请求中的操作日志 operatorMsg
                    msg = request.getParameter("operatorMsg");
                    msgList.add(msg);
                }
                else if (ACCEPT_DATA_METHOD_OTHER.equalsIgnoreCase(acceptDataMethod))
                {
                    // 从msg中读取，此处不做处理
                    msgList.add(msg);
                }

                if (null != msgList && !msgList.isEmpty())
                {
                    for (String msgStr : msgList)
                    {
                        msgStr = processDataFromSession(request, msgStr);
                        spSystemOperateLogInfo = new SpSystemOperateLogInfo();
                        spSystemOperateLogInfo.setId(GenUtil.getUUID());
                        spSystemOperateLogInfo.setOperation(msgStr);
                        spSystemOperateLogInfo.setAdminId(userId);
                        spSystemOperateLogInfo.setAdminName(userName);
                        spSystemOperateLogInfo.setGenerationTimeTs(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.DATETIME_FORMAT_PATTERN), DateUtil.DATETIME_FORMAT_PATTERN));
                        spSystemOperateLogInfo.setRoleId(roleId);
                        spSystemOperateLogInfo.setRoleName(roleName);
                        spSystemOperateLogInfo.setDiscriminator("discriminator");
                        spSystemOperateLogInfo.setTransactionId("transactionId");
                        spSystemOperateLogInfo.setIsLeaderForTx(1L);
                        sysOperateLogDao.saveOperateLog(spSystemOperateLogInfo);
                        logger.info("日志信息：" + msg);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("操作日志记录失败！" + request.getRequestURI(), e);
        }

        return true;
    }

    private String processDataFromSession(HttpServletRequest request, String msg)
    {
        // 对msg中需要引入session的数据处理
        Pattern pattern = Pattern.compile(REGEX_SPLIT_SESSION);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find())
        {
            String group = matcher.group();
            String propertyName = group.substring(1, group.length() - 1);
            String sessionValue = (String) request.getSession().getAttribute(propertyName);
            msg = msg.replace(group, sessionValue);
        }
        return msg;
    }

    private void processIdsValueData(JSONObject jsonObject, String msg, List<String> msgList, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        if (null != value && value.trim().length() > 0)
        {
            String daoBeanName = jsonObject.getString("daoBeanName");
            String daoMethod = jsonObject.getString("daoMethod");
            Object daoBean = WebApplicationContextUtils.getWebApplicationContext(MemInfo.getServletContext()).getBean(daoBeanName);
            Method declaredMethod = daoBean.getClass().getDeclaredMethod(daoMethod, String.class);
            String[] ids = value.split(",");
            for (String id : ids)
            {
                String msgTemp = msg;
                Object invoke = declaredMethod.invoke(daoBean, id);
                Pattern pattern = Pattern.compile(REGEX_SPLIT_DATA);
                Matcher matcher = pattern.matcher(msgTemp);
                while (matcher.find())
                {
                    String group = matcher.group();
                    String propertyName = group.substring(1, group.length() - 1);
                    String getMethodName = "get" + String.valueOf(propertyName.charAt(0)).toUpperCase() + propertyName.substring(1);
                    Method getMethod = invoke.getClass().getDeclaredMethod(getMethodName);
					String propertyValue = "";
					Object obj = getMethod.invoke(invoke);
					if (obj instanceof Date)
					{
						propertyValue = format.format(obj);
					}
					else
					{
						propertyValue = (String) getMethod.invoke(invoke);
					}

                    msgTemp = msgTemp.replace(group, propertyValue);
                }
                msgList.add(msgTemp);
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {

    }
}
