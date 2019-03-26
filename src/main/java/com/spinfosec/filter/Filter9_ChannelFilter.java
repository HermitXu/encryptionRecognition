package com.spinfosec.filter;

import com.spinfosec.system.BodyReaderHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ank
 * @version v 1.0
 * @title [将Request请求中的RequestBody流继续写下去，避免操作日志拦截器读取流后数据无法传递到controller的问题]
 * @ClassName: com.spinfosec.Filter.ChannelFilter
 * @description [将Request请求中的RequestBody流继续写下去，避免操作日志拦截器读取流后数据无法传递到controller的问题]
 * @create 2018/11/26 19:51
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@WebFilter(urlPatterns = "/*", filterName = "channelFilter")
public class Filter9_ChannelFilter implements Filter
{
	private Logger logger = LoggerFactory.getLogger(Filter9_ChannelFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		// 防止流读取一次后就没有了, 所以需要将流继续写出去
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);

		// 禁用缓存
		HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
		httpResp.setHeader("X-Frame-Options", "SAMEORIGIN");
		httpResp.setHeader("Pragma", "no-cache");
		httpResp.setHeader("Cache-Control", "no-cache");
		httpResp.addHeader("Cache-Control", "no-store");
		httpResp.setHeader("expires", "-1");

		filterChain.doFilter(requestWrapper, servletResponse);
	}

	@Override
	public void destroy()
	{

	}
}
