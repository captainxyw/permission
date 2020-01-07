package com.permission.controller;

import com.permission.common.JsonData;
import com.permission.dto.DeptLevelDto;
import com.permission.param.DeptParam;
import com.permission.service.SysDeptService;
import com.permission.service.SysTreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Package:com.permission.controller
 * Description:
 *
 * @Date:2020/1/7 20:41
 * @Author:xuyewei
 */
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {
  @Resource
  private SysDeptService sysDeptService;
  @Resource
  private SysTreeService sysTreeService;

  @RequestMapping("page.json")
  public ModelAndView page() {
    return new ModelAndView("dept");
  }

  @RequestMapping("/save.json")
  @ResponseBody
  public JsonData saveDept(DeptParam param) {
    sysDeptService.save(param);
    return JsonData.success();
  }

  @RequestMapping("/tree.json")
  @ResponseBody
  public JsonData tree() {
    List<DeptLevelDto> dtoList = sysTreeService.deptTree();
    return JsonData.success(dtoList);
  }

  @RequestMapping("/update.json")
  @ResponseBody
  public JsonData updateDept(DeptParam param) {
    sysDeptService.update(param);
    return JsonData.success();
  }
}
