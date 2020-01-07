package com.permission.dto;

import com.google.common.collect.Lists;
import com.permission.model.SysDept;
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
public class DeptLevelDto extends SysDept {
  private List<DeptLevelDto> deptList = Lists.newArrayList();

  public static DeptLevelDto adapt(SysDept dept) {
    DeptLevelDto dto = new DeptLevelDto();
    BeanUtils.copyProperties(dept, dto);
    return dto;
  }

}
