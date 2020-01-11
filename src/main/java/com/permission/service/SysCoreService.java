package com.permission.service;

import com.google.common.collect.Lists;
import com.permission.common.RequestHolder;
import com.permission.dao.SysAclMapper;
import com.permission.dao.SysRoleAclMapper;
import com.permission.dao.SysRoleUserMapper;
import com.permission.model.SysAcl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/11 20:16
 * @Author:xuyewei
 */
@Service
public class SysCoreService {

  @Resource
  private SysAclMapper sysAclMapper;
  @Resource
  private SysRoleUserMapper sysRoleUserMapper;
  @Resource
  private SysRoleAclMapper sysRoleAclMapper;


  public List<SysAcl> getCurrentUserAclList() {
    int userId = RequestHolder.getCurrentUser().getId();
    return getUserAclList(userId);
  }

  public List<SysAcl> getRoleAclList(int roleId) {
    List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
    if(CollectionUtils.isEmpty(aclIdList)) {
      return Lists.newArrayList();
    }
    return sysAclMapper.getByIdList(aclIdList);
  }

  public List<SysAcl> getUserAclList(int userId) {
    if (isSuperAdmin()) {
      return sysAclMapper.getAll();
    }
    List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
    if(CollectionUtils.isEmpty(userRoleIdList)) {
      return Lists.newArrayList();
    }

    List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
    if(CollectionUtils.isEmpty(userAclIdList)) {
      return Lists.newArrayList();
    }

    return sysAclMapper.getByIdList(userAclIdList);
  }

  public boolean isSuperAdmin() {
    return true;
  }
}
