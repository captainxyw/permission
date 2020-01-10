package com.permission.controller;

import com.permission.common.JsonData;
import com.permission.param.RoleParam;
import com.permission.service.SysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

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
}
