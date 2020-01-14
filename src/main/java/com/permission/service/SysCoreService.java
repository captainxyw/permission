package com.permission.service;

import com.google.common.collect.Lists;
import com.permission.common.RequestHolder;
import com.permission.dao.SysAclMapper;
import com.permission.dao.SysRoleAclMapper;
import com.permission.dao.SysRoleUserMapper;
import com.permission.model.SysAcl;
import com.permission.model.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    //定义超级管理员规则
    SysUser sysUser = RequestHolder.getCurrentUser();
    if(sysUser.getMail().contains("admin")) {
      return true;
    }
    return false;
  }

  public boolean hasUrlAcl(String url) {
    if(isSuperAdmin()) {
      return true;
    }
    List<SysAcl> aclList = sysAclMapper.getByUrl(url);
    //不拦截这个url
    if(CollectionUtils.isEmpty(aclList)) {
      return true;
    }


    List<SysAcl> userAclList = getCurrentUserAclList();
    Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
    //规则：只要有一个权限点有权限，那么我们就认为有访问权限
    // /sys/user/action.json

    boolean hasValidAcl = false;
    for(SysAcl acl : aclList) {
      //判断是否有某个权限点的访问权限
      if(acl == null || acl.getStatus() != 1) {
        continue;
      }
      hasValidAcl = true;
      if(userAclIdSet.contains(acl.getId())) {
        return true;
      }
    }
    if(!hasValidAcl) {
      return true;
    }
    return false;
  }
}
