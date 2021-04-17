package com.wjs.questionnaire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class test {

    /**
     * 访问URL：http://localhost:8080/questionnaire/test01
     *
     * @return Jquery动态添加标签元素
     */
    @GetMapping(value = "/test01")
    public String test01() {
        return "/test/test01";
    }

    /**
     * 访问URL：http://localhost:8080/questionnaire/test02
     *
     * @return 全选全不选
     */
    @GetMapping(value = "/test02")
    public String test02() {
        return "/test/test02";
    }
}
