package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;

public class LoginFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        //ServletRequest是HttpServletRequest的父类，没有getSession()方法，需要向下转型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String uri = request.getRequestURI();
        //放行登录相关的请求
        if (uri.contains("login")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            //进行拦截判断（验证令牌）
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            //判断来访用户合法性
            if(user == null){
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
            filterChain.doFilter(servletRequest,servletResponse);  //放行
        }
    }

    @Override
    public void destroy() { }

}
