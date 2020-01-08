package com.permission.beans;

import lombok.*;

import java.util.Set;

/**
 * Package:com.permission.beans
 * Description:
 *
 * @Date:2020/1/8 23:42
 * @Author:xuyewei
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

  private String subject;
  private String message;
  private Set<String> receivers;
}
