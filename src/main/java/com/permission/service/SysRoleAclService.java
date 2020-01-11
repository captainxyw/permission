package com.permission.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.permission.common.RequestHolder;
import com.permission.dao.SysRoleAclMapper;
import com.permission.model.SysRoleAcl;
import com.permission.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/11 22:45
 * @Author:xuyewei
 */
@Service
public class SysRoleAclService {

  @Resource
  private SysRoleAclMapper sysRoleAclMapper;

  public void changRoleAcls(Integer roleId, List<Integer> aclIdList) {
    List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
    if(originAclIdList.size() == aclIdList.size()) {
      Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
      Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
      originAclIdSet.removeAll(aclIdList);
      if(CollectionUtils.isEmpty(originAclIdSet)) {
        return;
      }
    }
    updateRoleAcls(roleId, aclIdList);
  }


  @Transactional
  public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
    sysRoleAclMapper.deleteByRoleId(roleId);

    if(CollectionUtils.isEmpty(aclIdList)) {
      return;
    }
    List<SysRoleAcl> roleAclList = Lists.newArrayList();
    for(Integer aclId : aclIdList) {
      SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getCurrentUser().getUsername())
          .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
      roleAclList.add(roleAcl);
    }

    sysRoleAclMapper.batchInsert(roleAclList);
  }
}
