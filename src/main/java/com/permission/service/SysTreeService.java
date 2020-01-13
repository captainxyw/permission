package com.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.permission.dao.SysAclMapper;
import com.permission.dao.SysAclModuleMapper;
import com.permission.dao.SysDeptMapper;
import com.permission.dto.AclDto;
import com.permission.dto.AclModuleLevelDto;
import com.permission.dto.DeptLevelDto;
import com.permission.model.SysAcl;
import com.permission.model.SysAclModule;
import com.permission.model.SysDept;
import com.permission.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
  @Resource
  private SysCoreService sysCoreService;
  @Resource
  private SysAclMapper sysAclMapper;


  public List<AclModuleLevelDto> userAclTree(int userId) {
    //当前用户已分配的权限点
    List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);

    List<AclDto> aclDtoList = Lists.newArrayList();
    for (SysAcl acl : userAclList) {
      AclDto dto = AclDto.adapt(acl);
      dto.setHasAcl(true);
      dto.setChecked(true);
      aclDtoList.add(dto);
    }
    return aclListToTree(aclDtoList);
  }


  public List<AclModuleLevelDto> roleTree(int roleId) {
    //当前用户已分配的权限点
    List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
    //当前角色已分配的权限点
    List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
    //当前系统所有的权限点
    List<SysAcl> allAclList = sysAclMapper.getAll();

    Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
    Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());


    List<AclDto> aclDtoList = Lists.newArrayList();
    for (SysAcl acl : allAclList) {
      AclDto dto = AclDto.adapt(acl);
      if (userAclIdSet.contains(acl.getId())) {
        dto.setHasAcl(true);
      }
      if (roleAclIdSet.contains(acl.getId())) {
        dto.setChecked(true);
      }

      aclDtoList.add(dto);
    }
    return aclListToTree(aclDtoList);
  }


  public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
    if (CollectionUtils.isEmpty(aclDtoList)) {
      return Lists.newArrayList();
    }
    List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

    Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
    for (AclDto acl : aclDtoList) {
      if (acl.getStatus() == 1) {
        moduleIdAclMap.put(acl.getAclModuleId(), acl);
      }
    }
    bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
    return aclModuleLevelList;
  }

  public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
    if (CollectionUtils.isEmpty(aclModuleLevelList)) {
      return;
    }
    for (AclModuleLevelDto dto : aclModuleLevelList) {
      List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
      if (!CollectionUtils.isEmpty(aclDtoList)) {
        aclDtoList.sort(aclSeqComparator);
        dto.setAclList(aclDtoList);
      }
      bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
    }
  }


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
      if (LevelUtil.ROOT.equals(dto.getLevel())) {
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
    for (int i = 0; i < aclModuleLevelList.size(); i++) {
      //遍历该层的每个元素
      AclModuleLevelDto aclModuleLevelDto = aclModuleLevelList.get(i);
      //处理当前
      String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
      //处理下一层
      List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) levelDeptMap.get(nextLevel);
      if (!CollectionUtils.isEmpty(tempAclModuleList)) {
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
      if (LevelUtil.ROOT.equals(dto.getLevel())) {
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
    for (int i = 0; i < deptLevelList.size(); i++) {
      //遍历该层的每个元素
      DeptLevelDto deptLevelDto = deptLevelList.get(i);
      //处理当前
      String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
      //处理下一层
      List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
      if (!CollectionUtils.isEmpty(tempDeptList)) {
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

  public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
    @Override
    public int compare(AclDto o1, AclDto o2) {
      return o1.getSeq() - o2.getSeq();
    }
  };
}
