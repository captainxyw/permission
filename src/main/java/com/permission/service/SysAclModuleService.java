package com.permission.service;

import com.google.common.base.Preconditions;
import com.permission.common.RequestHolder;
import com.permission.dao.SysAclModuleMapper;
import com.permission.exception.ParamException;
import com.permission.model.SysAclModule;
import com.permission.param.AclModuleParam;
import com.permission.util.BeanValidator;
import com.permission.util.IpUtil;
import com.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/9 20:44
 * @Author:xuyewei
 */
@Service
public class SysAclModuleService {
  @Resource
  private SysAclModuleMapper sysAclModuleMapper;

  public void save(AclModuleParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getParentId(), param.getName(), param.getId())) {
      throw new ParamException("同一层级下存在相同名称的权限模块");
    }
    SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
        .status(param.getStatus()).remark(param.getRemark()).build();

    aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
    aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
    aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    aclModule.setOperateTime(new Date());
    sysAclModuleMapper.insertSelective(aclModule);
  }

  public void update(AclModuleParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getParentId(), param.getName(), param.getId())) {
      throw new ParamException("同一层级下存在相同名称的权限模块");
    }
    SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
    Preconditions.checkNotNull(before, "待更新的权限模块不存在");

    SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
        .status(param.getStatus()).remark(param.getRemark()).build();
    after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
    after.setOperator(RequestHolder.getCurrentUser().getUsername());
    after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    after.setOperateTime(new Date());
    updatewithChild(before, after);

  }

  @Transactional
  public void updatewithChild(SysAclModule before, SysAclModule after) {
    String newLevelPrefix = after.getLevel();
    String oldLevelPrefix = before.getLevel();
    if(!after.getLevel().equals(before.getLevel())) {
      List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
      if(CollectionUtils.isNotEmpty(aclModuleList)) {
        for(SysAclModule aclModule : aclModuleList) {
          String level = aclModule.getLevel();
          if(level.indexOf(oldLevelPrefix) == 0) {
            level = newLevelPrefix + level.substring(oldLevelPrefix.length());
            aclModule.setLevel(level);
          }
        }
        sysAclModuleMapper.batchUpdateLevel(aclModuleList);
      }
    }
    sysAclModuleMapper.updateByPrimaryKeySelective(after);
  }

  private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
    return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
  }


  private String getLevel(Integer aclModuleId) {
    SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
    if (aclModule == null) {
      return null;
    }
    return aclModule.getLevel();
  }
}
