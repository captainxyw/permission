package com.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.permission.dao.SysAclModuleMapper;
import com.permission.dao.SysDeptMapper;
import com.permission.dto.AclModuleLevelDto;
import com.permission.dto.DeptLevelDto;
import com.permission.model.SysAclModule;
import com.permission.model.SysDept;
import com.permission.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/7 21:16
 * @Author:xuyewei
 */
@Service
public class SysTreeService {

  @Resource
  private SysDeptMapper sysDeptMapper;

  @Resource
  private SysAclModuleMapper sysAclModuleMapper;

  public List<AclModuleLevelDto> aclModuleTree() {
    List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
    List<AclModuleLevelDto> dtoList = Lists.newArrayList();
    for (SysAclModule aclModule : aclModuleList) {
      AclModuleLevelDto dto = AclModuleLevelDto.adapt(aclModule);
      dtoList.add(dto);
    }

    return aclModuleListtoTree(dtoList);
  }

  public List<AclModuleLevelDto> aclModuleListtoTree(List<AclModuleLevelDto> aclModuleLevelList) {
    if (CollectionUtils.isEmpty(aclModuleLevelList)) {
      return Lists.newArrayList();
    }
    // level -> [aclModule1, aclModule2]
    Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
    List<AclModuleLevelDto> rootList = Lists.newArrayList();
    for (AclModuleLevelDto dto : aclModuleLevelList) {
      levelAclModuleMap.put(dto.getLevel(), dto);
      if(LevelUtil.ROOT.equals(dto.getLevel())) {
        rootList.add(dto);
      }
    }
    //按照seq从小到大排序
    Collections.sort(rootList, aclModuleSeqComparator);
    //递归生成树
    transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
    return rootList;
  }


  public void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelList, String level, Multimap<String, AclModuleLevelDto> levelDeptMap) {
    for (int i = 0; i< aclModuleLevelList.size(); i++) {
      //遍历该层的每个元素
      AclModuleLevelDto aclModuleLevelDto = aclModuleLevelList.get(i);
      //处理当前
      String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
      //处理下一层
      List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) levelDeptMap.get(nextLevel);
      if(!CollectionUtils.isEmpty(tempAclModuleList)) {
        //排序
        Collections.sort(tempAclModuleList, aclModuleSeqComparator);
        //设置下一层部门
        aclModuleLevelDto.setAclModuleList(tempAclModuleList);
        //进入到下一层处理
        transformAclModuleTree(tempAclModuleList, nextLevel, levelDeptMap);
      }
    }
  }













  public List<DeptLevelDto> deptTree() {
    List<SysDept> deptList = sysDeptMapper.getAllDept();

    List<DeptLevelDto> dtoList = Lists.newArrayList();
    for (SysDept dept : deptList) {
      DeptLevelDto dto = DeptLevelDto.adapt(dept);
      dtoList.add(dto);
    }

    return deptListtoTree(dtoList);
  }


  public List<DeptLevelDto> deptListtoTree(List<DeptLevelDto> deptLevelList) {
    if (CollectionUtils.isEmpty(deptLevelList)) {
      return Lists.newArrayList();
    }
    // level -> [dept1, dept2]
    Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
    List<DeptLevelDto> rootList = Lists.newArrayList();
    for (DeptLevelDto dto : deptLevelList) {
      levelDeptMap.put(dto.getLevel(), dto);
      if(LevelUtil.ROOT.equals(dto.getLevel())) {
        rootList.add(dto);
      }
    }
    //按照seq从小到大排序
    Collections.sort(rootList, deptSeqComparator);
    //递归生成树
    transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
    return rootList;
  }

  public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
    for (int i=0; i<deptLevelList.size(); i++) {
      //遍历该层的每个元素
      DeptLevelDto deptLevelDto = deptLevelList.get(i);
      //处理当前
      String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
      //处理下一层
      List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
      if(!CollectionUtils.isEmpty(tempDeptList)) {
        //排序
        Collections.sort(tempDeptList, deptSeqComparator);
        //设置下一层部门
        deptLevelDto.setDeptList(tempDeptList);
        //进入到下一层处理
        transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
      }
    }
  }

  public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
    @Override
    public int compare(DeptLevelDto o1, DeptLevelDto o2) {
      return o1.getSeq() - o2.getSeq();
    }
  };


  public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
    @Override
    public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
      return o1.getSeq() - o2.getSeq();
    }
  };
}
