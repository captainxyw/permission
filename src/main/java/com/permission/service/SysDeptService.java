package com.permission.service;

import com.google.common.base.Preconditions;
import com.permission.dao.SysDeptMapper;
import com.permission.exception.ParamException;
import com.permission.model.SysDept;
import com.permission.param.DeptParam;
import com.permission.util.BeanValidator;
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
 * @Date:2020/1/7 20:43
 * @Author:xuyewei
 */
@Service
public class SysDeptService {

  @Resource
  private SysDeptMapper sysDeptMapper;

  public void save(DeptParam param) {
    BeanValidator.check(param);
    if(checkExist(param.getParentId(), param.getName(), param.getId())) {
      throw new ParamException("同一层级下存在相同名称的部门");
    }
    SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
        .seq(param.getSeq()).remark(param.getRemark()).build();

    dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
    dept.setOperator("system");//TODO
    dept.setOperateIp("127.0.0.1");//TODO
    dept.setOperateTime(new Date());//TODO
    sysDeptMapper.insertSelective(dept);
  }

  public void update(DeptParam param) {
    BeanValidator.check(param);
    if(checkExist(param.getParentId(), param.getName(), param.getId())) {
      throw new ParamException("同一层级下存在相同名称的部门");
    }

    SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
    Preconditions.checkNotNull(before, "待更新的部门不存在");

    SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
        .seq(param.getSeq()).remark(param.getRemark()).build();
    after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
    after.setOperator("system-update");//TODO
    after.setOperateIp("127.0.0.1");//TODO
    after.setOperateTime(new Date());//TODO
    updatewithChild(before, after);
  }

  @Transactional
  public void updatewithChild(SysDept before, SysDept after) {
    String newLevelPrefix = after.getLevel();
    String oldLevelPrefix = before.getLevel();
    if(!after.getLevel().equals(before.getLevel())) {
      List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
      if(CollectionUtils.isNotEmpty(deptList)) {
        for(SysDept dept : deptList) {
          String level = dept.getLevel();
          if(level.indexOf(oldLevelPrefix) == 0) {
            level = newLevelPrefix + level.substring(oldLevelPrefix.length());
            dept.setLevel(level);
          }
        }
        sysDeptMapper.batchUpdateLevel(deptList);
      }
    }

    sysDeptMapper.updateByPrimaryKey(after);
  }



  private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
    return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
  }

  private String getLevel(Integer deptId) {
    SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
    if(dept == null) {
      return null;
    }
      return dept.getLevel();
  }
}
