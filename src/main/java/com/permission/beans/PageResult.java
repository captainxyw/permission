package com.permission.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Package:com.permission.beans
 * Description:
 *
 * @Date:2020/1/8 22:20
 * @Author:xuyewei
 */
@Getter
@Setter
@Builder
public class PageResult<T> {

  private List<T> data = Lists.newArrayList();

  private int total = 0;
}
