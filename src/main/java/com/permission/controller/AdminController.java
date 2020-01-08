package com.permission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Package:com.permission.controller
 * Description:
 *
 * @Date:2020/1/8 21:51
 * @Author:xuyewei
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

  @RequestMapping("index.page")
  public ModelAndView index() {
    return new ModelAndView("admin");
  }
}
