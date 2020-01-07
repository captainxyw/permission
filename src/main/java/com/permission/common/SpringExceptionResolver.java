package com.permission.common;

import com.permission.exception.ParamException;
import com.permission.exception.PermisssionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Package:com.permission.common
 * Description:
 *
 * @Date:2020/1/6 22:13
 * @Author:xuyewei
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    String url = request.getRequestURL().toString();
    ModelAndView mv = new ModelAndView();
    String defaultMsg = "System error";

    // .json 数据请求
    // .page 页面请求
    if (url.endsWith(".json")) {
      if (ex instanceof PermisssionException || ex instanceof ParamException) {
        JsonData result = JsonData.fail(ex.getMessage());
        mv = new ModelAndView("jsonView", result.toMap());
      } else {
        log.error("unknown json exception, url:" + url, ex);
        JsonData result = JsonData.fail(defaultMsg);
        mv = new ModelAndView("jsonView", result.toMap());
      }
    } else if (url.endsWith(".page")) {
      log.error("unknown page exception, url:" + url, ex);
      JsonData result = JsonData.fail(defaultMsg);
      mv = new ModelAndView("exception", result.toMap());
    } else {
      log.error("unknown exception, url:" + url, ex);
      JsonData result = JsonData.fail(defaultMsg);
      mv = new ModelAndView("jsonView", result.toMap());
    }
    return mv;
  }
}
