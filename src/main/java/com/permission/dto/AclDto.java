package com.permission.dto;

import com.permission.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * Package:com.permission.dto
 * Description:
 *
 * @Date:2020/1/11 20:07
 * @Author:xuyewei
 */

@Getter
@Setter
@ToString
public class AclDto extends SysAcl {
  //是否默认选中
  private boolean checked = false;
  //是否有权限操作
  private boolean hasAcl = false;



  public static AclDto adapt(SysAcl acl) {
    AclDto dto = new AclDto();
    BeanUtils.copyProperties(acl, dto);
    return dto;
  }
}
