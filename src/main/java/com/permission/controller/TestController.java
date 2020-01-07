package com.permission.controller;

import com.permission.common.JsonData;
import com.permission.dao.SysAclModuleMapper;
import com.permission.model.SysAclModule;
import com.permission.param.TestVo;
import com.permission.common.ApplicationContextHelper;
import com.permission.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Package:com.permission.controller
 * Description:
 *
 * @Date:2020/1/6 17:53
 * @Author:xuyewei
 */

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

  @RequestMapping("/hello.page")
  @ResponseBody
  public JsonData hello() {
    throw new RuntimeException("test exception");
//    return JsonData.success("hello permission");
  }

  @RequestMapping("/validate.json")
  @ResponseBody
  public JsonData validate(TestVo vo) {
    log.info("validate");
    SysAclModuleMapper sysAclModuleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
    SysAclModule module = sysAclModuleMapper.selectByPrimaryKey(1);
    log.info(JsonMapper.obj2String(module));

    return JsonData.success("hello permission");
  }

}
