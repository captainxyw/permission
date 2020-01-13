package com.permission.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.permission.common.RequestHolder;
import com.permission.dao.SysRoleUserMapper;
import com.permission.dao.SysUserMapper;
import com.permission.model.SysRoleUser;
import com.permission.model.SysUser;
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
 * @Date:2020/1/13 20:06
 * @Author:xuyewei
 */
@Service
public class SysRoleUserService {

  @Resource
  private SysRoleUserMapper sysRoleUserMapper;

  @Resource
  private SysUserMapper sysUserMapper;

  public List<SysUser> getListByRoleId(int roleId) {
    List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
    if (CollectionUtils.isEmpty(userIdList)) {
      return Lists.newArrayList();
    }
    return sysUserMapper.getByIdList(userIdList);
  }

  public void changeRoleUsers(int roleId, List<Integer> userIdList) {
    List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
    if (originUserIdList.size() == userIdList.size()) {
      Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
      Set<Integer> userIdSet = Sets.newHashSet(userIdList);
      originUserIdSet.removeAll(userIdSet);
      if (CollectionUtils.isEmpty(originUserIdSet)) {
        return;
      }
    }
    updateRoleUsers(roleId, userIdList);
  }

  @Transactional
  public void updateRoleUsers(int roleId, List<Integer> userIdList) {
    sysRoleUserMapper.deleteByRoleId(roleId);

    if (CollectionUtils.isEmpty(userIdList)) {
      return;
    }
    List<SysRoleUser> roleUserList = Lists.newArrayList();
    for (Integer userId : userIdList) {
      SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername())
          .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
      roleUserList.add(roleUser);
    }

    sysRoleUserMapper.batchInsert(roleUserList);
  }
}
