package com.permission.service;

import com.google.common.base.Preconditions;
import com.permission.common.RequestHolder;
import com.permission.dao.SysRoleMapper;
import com.permission.exception.ParamException;
import com.permission.model.SysRole;
import com.permission.param.RoleParam;
import com.permission.util.BeanValidator;
import com.permission.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/10 22:23
 * @Author:xuyewei
 */
@Service
public class SysRoleService {

  @Resource
  private SysRoleMapper sysRoleMapper;

  public void save(RoleParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getName(), param.getId())) {
      throw new ParamException("角色名称已经存在");
    }
    SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
        .remark(param.getRemark()).build();

    role.setOperator(RequestHolder.getCurrentUser().getUsername());
    role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    role.setOperateTime(new Date());
    sysRoleMapper.insertSelective(role);
  }

  public void update(RoleParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getName(), param.getId())) {
      throw new ParamException("角色名称已经存在");
    }
    SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
    Preconditions.checkNotNull(before, "待更新的角色不存在");

    SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
        .remark(param.getRemark()).build();

    after.setOperator(RequestHolder.getCurrentUser().getUsername());
    after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    after.setOperateTime(new Date());
    sysRoleMapper.updateByPrimaryKeySelective(after);
  }

  public List<SysRole> getAll() {
    return sysRoleMapper.getAll();
  }
  private boolean checkExist(String name, Integer id) {
    return sysRoleMapper.countByName(name, id) > 0;
  }
}
