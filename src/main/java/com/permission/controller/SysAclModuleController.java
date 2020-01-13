package com.permission.controller;

import com.permission.common.JsonData;
import com.permission.dto.AclModuleLevelDto;
import com.permission.param.AclModuleParam;
import com.permission.service.SysAclModuleService;
import com.permission.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
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
 * @Date:2020/1/9 20:34
 * @Author:xuyewei
 */
@Controller
@RequestMapping("sys/aclModule")
@Slf4j
public
class SysAclModuleController {
  @Resource
  private SysAclModuleService sysAclModuleService;
  @Resource
  private SysTreeService sysTreeService;

  @RequestMapping("acl.page")
  public ModelAndView page() {
    return new ModelAndView("acl");
  }

  @RequestMapping("/save.json")
  @ResponseBody
  public JsonData saveAclModule(AclModuleParam param) {
    sysAclModuleService.save(param);
    return JsonData.success();
  }


  @RequestMapping("/update.json")
  @ResponseBody
  public JsonData updateAclModule(AclModuleParam param) {
    sysAclModuleService.update(param);
    return JsonData.success();
  }

  @RequestMapping("/tree.json")
  @ResponseBody
  public JsonData tree() {
    List<AclModuleLevelDto> dtoList = sysTreeService.aclModuleTree();
    return JsonData.success(dtoList);
  }


  @RequestMapping("/delete.json")
  @ResponseBody
  public JsonData delete(@RequestParam("id") int id) {
    sysAclModuleService.delete(id);
    return JsonData.success();
  }

}
