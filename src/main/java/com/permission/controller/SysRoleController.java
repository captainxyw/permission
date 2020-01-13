package com.permission.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.permission.common.JsonData;
import com.permission.model.SysUser;
import com.permission.param.RoleParam;
import com.permission.service.*;
import com.permission.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Package:com.permission.controller
 * Description:
 *
 * @Date:2020/1/10 22:23
 * @Author:xuyewei
 */

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
  @Resource
  private SysRoleService sysRoleService;
  @Resource
  private SysTreeService sysTreeService;
  @Resource
  private SysRoleAclService sysRoleAclService;
  @Resource
  private SysRoleUserService sysRoleUserService;

  @Resource
  private SysUserService sysUserService;


  @RequestMapping("/role.page")
  public ModelAndView page() {
    return new ModelAndView("role");
  }


  @RequestMapping("/save.json")
  @ResponseBody
  public JsonData saveRole(RoleParam param) {
    sysRoleService.save(param);
    return JsonData.success();
  }

  @RequestMapping("/update.json")
  @ResponseBody
  public JsonData updateRole(RoleParam param) {
    sysRoleService.update(param);
    return JsonData.success();
  }

  @RequestMapping("/list.json")
  @ResponseBody
  public JsonData list() {
    return JsonData.success(sysRoleService.getAll());
  }

  @RequestMapping("/roleTree.json")
  @ResponseBody
  public JsonData roleTree(@RequestParam("roleId") int roleId) {
    return JsonData.success(sysTreeService.roleTree(roleId));
  }

  @RequestMapping("/changeAcls.json")
  @ResponseBody
  public JsonData changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
    List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
    sysRoleAclService.changRoleAcls(roleId, aclIdList);
    return JsonData.success();
  }


  @RequestMapping("/changeUsers.json")
  @ResponseBody
  public JsonData changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
    List<Integer> userIdList = StringUtil.splitToListInt(userIds);
    sysRoleUserService.changeRoleUsers(roleId, userIdList);
    return JsonData.success();
  }

  @RequestMapping("/users.json")
  @ResponseBody
  public JsonData users(@RequestParam("roleId") int roleId) {
    List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
    List<SysUser> allUserList = sysUserService.getAll();
    ArrayList<SysUser> unselectedUserList = Lists.newArrayList();


// List<SysUser> selectedUserList  →  Set<Integer> selectedUserIdSet，Set 效率高
    Set<Integer> selectedUserIdSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
    for (SysUser sysUser : allUserList) {
      if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
        unselectedUserList.add(sysUser);
      }
    }

//    selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() ==1).collect(Collectors.toList());
    Map<String, List<SysUser>> map = Maps.newHashMap();
    map.put("selected", selectedUserList);
    map.put("unselected", unselectedUserList);

    return JsonData.success(map);
  }


}
