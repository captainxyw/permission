package com.permission.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Package:com.permission.util
 * Description:
 *
 * @Date:2020/1/7 12:27
 * @Author:xuyewei
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    applicationContext = context;
  }

  public static <T> T popBean(Class<T> clazz) {
    if (applicationContext == null)
      return null;
    return applicationContext.getBean(clazz);
  }

  public static <T> T popBean(String name, Class<T> clazz) {
    if (applicationContext == null)
      return null;
    return applicationContext.getBean(name, clazz);
  }
}
