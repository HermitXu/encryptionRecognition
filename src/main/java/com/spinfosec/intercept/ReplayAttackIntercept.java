package com.spinfosec.intercept;

import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.ResultUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ank
 * @version v 1.0
 * @title [重复攻击拦截器]
 * @ClassName: com.spinfosec.intercept.ReplayAttackIntercept
 * @description： 防重放攻击机制：客户端在请求的head中增加参数noncetimestamp，该参数的值为AES(timestamp#md5(uuid)),后台接收到该参数后，AES解密后获得时间戳和md5(uuid)，
 * 通过时间戳过滤掉60s以外的请求(前后端的时间需要同步)， 通过查询redis缓存来判断60s内的请求是否有效，判断方式：redis中存在传入的md5(uuid)，则是重复攻击，否则，
 * 不是并将md5(uuid)存入到redis缓存中，设置60s失效时间
 * @create 2018/10/9 14:16
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class ReplayAttackIntercept implements HandlerInterceptor
{

    public static final String SPLIT_CHAR = "#";
    public static final Long MINUTE = 60 * 1000L;
    public static final String NAME_FOR_PARAM_AND_REDIS_SET_KEY = "noncetimestamp";

    private Logger logger = LoggerFactory.getLogger(ReplayAttackIntercept.class);

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
		String uri = request.getRequestURI();
		// 不做鉴权的有：前端通过此isShowAuthCode接口来同步服务器时间戳，故此接口不列入重放攻击序列
		// 是否显示验证码 创建验证码 下载PDF 安全日志下载 Ukey驱动下载
		// TODO 异常抛出 会出现/error请求
		if (uri.contains("/auth/isShowAuthCode") || uri.contains("/auth/createAuthCode") || uri.contains("/error")
				|| uri.contains("/export") || uri.contains("/download") || uri.contains("swagger") || uri.contains("/logout"))
		{
			return true;
		}
        // 获取重放攻击参数
        String nonceTimestamp = request.getHeader(NAME_FOR_PARAM_AND_REDIS_SET_KEY);
        // 解密重复攻击参数
        if (StringUtils.isNotEmpty(nonceTimestamp))
        {
            logger.info("请求" + request.getRequestURI() + "的参数" + NAME_FOR_PARAM_AND_REDIS_SET_KEY + " : " + nonceTimestamp);
			String decryptString = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, nonceTimestamp));
            String[] split = decryptString.split(SPLIT_CHAR);
            String timestamp = split[0];
            String md5Value = split[1];
            // 比较当前时间和时间戳上的时间
            long currentTimeMillis = System.currentTimeMillis();
			Date date = new Date();
			date.setTime((Long.parseLong(timestamp)));
			logger.info("浏览器时间：" + format.format(date));
			logger.info("服务器时间：" + format.format(currentTimeMillis));
            long duringTime = Math.abs(currentTimeMillis - Long.parseLong(timestamp));
            logger.info("请求时间和当前时间差：" +duringTime);
            if (duringTime - MINUTE <= 0)
            {
                // 查询redis中数据
                Object val = redisTemplate.opsForValue().get(md5Value);
                if (null == val)
                {
                    // 将请求写入redis
                    redisTemplate.opsForValue().set(md5Value, System.currentTimeMillis());
                    redisTemplate.expire(md5Value, MINUTE, TimeUnit.MILLISECONDS);
                    logger.info("请求" + request.getRequestURI() + "非重放攻击，通过！");
                    return true;
                }
            }
        }
        logger.info("请求" + request.getRequestURI() + "属于重放攻击，被拦截");
        CodeRsp codeRsp = new CodeRsp(RspCode.REPLAY_ATTACK);
        ResponseBean failResult = ResultUtil.getFailResult(codeRsp);
        ResultUtil.response(response, JSONObject.toJSONString(failResult));
        return false;
    }

}
