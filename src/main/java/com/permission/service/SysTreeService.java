package com.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.permission.dao.SysDeptMapper;
import com.permission.dto.DeptLevelDto;
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
}
