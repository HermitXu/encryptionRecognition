package com.spinfosec.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ResponseFilter
 * @Description: 〈编码过滤器〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
@WebFilter(urlPatterns = "/*", filterName = "charactorFilter")
public class Filter0_CharactorFilter implements Filter
{
	private String encoding = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding(encoding);
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy()
	{

	}
}
