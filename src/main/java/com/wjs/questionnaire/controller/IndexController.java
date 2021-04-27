package com.wjs.questionnaire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 处理首页相关请求的控制器类
 */
@Controller
public class IndexController {

    /**
     * 访问URL：http://localhost:8080/questionnaire
     *
     * @return 进入首页
     */
    @GetMapping(value = "/")
    public String root() {
//        return "forward:/user/login";// 转发：不修改URL地址，进入login页面无法加载静态资源
//        return "redirect:/user/login";// 重定向：修改URL地址，进入login页面可以加载静态资源
        return "forward:/questionnaire/index";
    }

    /**
     * @return 进入 问卷
     */
    @GetMapping(value = "/questionnaire")
    public String jumpAddOptionsPage(Model model) {
        return "/site/questionnaire";
    }
}
