package com.permission.exception;

/**
 * Package:com.permission.exception
 * Description:
 *
 * @Date:2020/1/6 22:18
 * @Author:xuyewei
 */

public class PermisssionException extends RuntimeException {
  public PermisssionException() {
    super();
  }

  public PermisssionException(String message) {
    super(message);
  }

  public PermisssionException(String message, Throwable cause) {
    super(message, cause);
  }

  public PermisssionException(Throwable cause) {
    super(cause);
  }

  protected PermisssionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
