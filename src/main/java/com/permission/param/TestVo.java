package com.permission.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Package:com.permission.param
 * Description:
 *
 * @Date:2020/1/7 0:00
 * @Author:xuyewei
 */
@Getter
@Setter
public class TestVo {
  @NotBlank(message = "not blank")
  private String msg;
  @NotNull(message = "not null")
  private Integer id;
}
