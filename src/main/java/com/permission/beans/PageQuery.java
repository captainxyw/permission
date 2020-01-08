package com.permission.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * Package:com.permission.beans
 * Description:
 *
 * @Date:2020/1/8 22:20
 * @Author:xuyewei
 */

public class PageQuery {
  @Getter
  @Setter
  @Min(value = 1, message = "当前页码不合法")
  private int pageNo = 1;

  @Getter
  @Setter
  @Min(value = 1, message = "每页展示数量不合法")
  private int pageSize = 10;

  @Setter
  private int offset;

  public int getOffset() {
    return (pageNo - 1) * pageSize;
  }
}
