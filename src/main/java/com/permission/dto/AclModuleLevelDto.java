package com.permission.dto;

import com.google.common.collect.Lists;
import com.permission.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Package:com.permission.dto
 * Description:
 *
 * @Date:2020/1/7 21:11
 * @Author:xuyewei
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {
  private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

  public static AclModuleLevelDto adapt(SysAclModule aclModule) {
    AclModuleLevelDto dto = new AclModuleLevelDto();
    BeanUtils.copyProperties(aclModule, dto);
    return dto;
  }

}
