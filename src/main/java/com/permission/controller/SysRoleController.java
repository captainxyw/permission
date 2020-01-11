package com.permission.controller;

import com.permission.common.JsonData;
import com.permission.param.RoleParam;
import com.permission.service.SysRoleAclService;
import com.permission.service.SysRoleService;
import com.permission.service.SysTreeService;
import com.permission.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

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
}
