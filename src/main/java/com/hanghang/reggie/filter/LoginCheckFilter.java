package com.hanghang.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hanghang.reggie.common.BaseContext;
import com.hanghang.reggie.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登陆
 * */
@WebFilter(filterName = "loginCheckFile" , urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Autowired
    BaseContext baseContext ;
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher() ;


    @Override
    public void doFilter(ServletRequest servletRequest
                       , ServletResponse servletResponse
                       , FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest ;
        HttpServletResponse response = (HttpServletResponse)servletResponse ;
        /*
        * 1.获取本次请求的url
        * 2.判断本次请求需不需要处理
        * 3.如果不需要直接放行
        * 4.如果不需要直接放行判断登陆状态，如果已登录直接放行
        * 5.如果未登陆返回登陆页面
        * */

        //1.获取本次请求的url
        String requestURI = request.getRequestURI();
        //写入不需要拦截的对象，静态页面都不需要拦截，只需要拦截想controller发送请求的对象
        String[] urls = new  String[] {
                "/employee/login" ,
                "/employee/logout" ,
                "/backend/**" ,
                "/front/**"
        };

        //2.判断本次请求需不需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要直接放行
        if(check){
            filterChain.doFilter(request , response);
            return;
        }

        //4.如果不需要直接放行判断登陆状态，如果已登录直接放行
        if(request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request , response);

            //为公共字段填充获取用户id
            Long empId = (Long) request.getSession().getAttribute("employee") ;
            baseContext.setId(empId);
            return;
        }
//        5.如果未登陆返回未登录结果，向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
    }

    public boolean check(String[] urls,String requestURL){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match) return  true ;
        }
        return false ;
    }
}
