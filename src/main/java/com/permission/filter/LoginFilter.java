package com.permission.filter;


import com.permission.common.RequestHolder;
import com.permission.model.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Package:com.permission.filter
 * Description:
 *
 * @Date:2020/1/8 23:14
 * @Author:xuyewei
 */

public class LoginFilter implements Filter {


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;
    String servletPath = req.getServletPath();

    SysUser sysUser = (SysUser) req.getSession().getAttribute("user");
    if (sysUser == null) {
      String path = "/signin.jsp";
      resp.sendRedirect(path);
      return;
    }
    RequestHolder.add(sysUser);
    RequestHolder.add(req);
    filterChain.doFilter(servletRequest, servletResponse);
    return;
  }

  @Override
  public void destroy() {

  }
}
