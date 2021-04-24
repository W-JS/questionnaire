package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IQuestionTypeService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理问题相关请求的控制器类
 */
@Controller
@RequestMapping("/questiontype")
public class QuestionTypeController {

    @Autowired
    private IQuestionTypeService questionTypeService;

    /**
     * @return JSON格式数据：所有问题类型
     */
    @GetMapping("getQuestionType")
    @ResponseBody
    public JSONResult getQuestionType() {
        return questionTypeService.getQuestionType();
    }
}
