package com.pansoft.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	FilterConfig filterConfig = null;
	private static final String DEFAULT_HOMEPAGE = "/login.jsp";
	private List<String> checkURLs = new ArrayList<String>();
	
	public void destroy() {
		// TODO Auto-generated method stub
		checkURLs.clear();
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpServletRequest request = (HttpServletRequest) arg0;
		
		String loginPage = null;
		String path = request.getContextPath();
		if(filterConfig.getInitParameter("loginPage") == null) {
			loginPage = path + DEFAULT_HOMEPAGE;
		} else {
			loginPage = path + filterConfig.getInitParameter("loginPage");
		}
		
		if (!checkRequestURIIntNotFilterList(request)) {
			HttpSession HttpSession = request.getSession(false);
			try {
				if(HttpSession == null) {
					//会话不存在，转向初始页面
					throw new Exception("会话不存在，转向初始页面");
				}
				Object user = HttpSession.getAttribute("user");
				if(user == null) {
					//用户没有登录，转向初始页面
					throw new Exception("用户没有登录，转向初始页面");
				}
			}catch (Exception e) {
				response.sendRedirect(loginPage);
				if(HttpSession != null) HttpSession.invalidate();
				return;
			}
			arg2.doFilter(request, response);
		}else{
        	arg2.doFilter(request, response);
        }
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		this.filterConfig = arg0;
		String checkURlList = filterConfig.getInitParameter("loginStrings");
		if(checkURlList != null) {
			StringTokenizer st = new StringTokenizer(checkURlList, ";");
			checkURLs.clear();
			while(st.hasMoreTokens()){
				checkURLs.add(st.nextToken());
			}
		}
	}
	
	private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {
//		String path = request.getServletPath();
		String uri=request.getServletPath() + (request.getQueryString() == null ? "" : "?"+request.getQueryString());
		boolean flag = false;
		for(String str : checkURLs){
			if(uri.startsWith(str)){
				flag = true;
			}
		}
        return flag;
	}

}
