package com.permission.common;

import com.permission.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Package:com.permission.common
 * Description:
 *
 * @Date:2020/1/8 23:06
 * @Author:xuyewei
 */

public class RequestHolder {

  private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

  private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

  public static void add(SysUser sysUser) {
    userHolder.set(sysUser);
  }

  public static void add(HttpServletRequest request) {
    requestHolder.set(request);
  }

  public static SysUser getCurrentUser() {
    return userHolder.get();
  }

  public static HttpServletRequest getCurrentRequest() {
    return requestHolder.get();
  }

  public static void remove() {
    userHolder.remove();
    requestHolder.remove();
  }
}
