package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IDataAnalysisService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理数据分析相关请求的控制器类
 */
@Controller
@RequestMapping("/dataAnalysis")
public class DataAnalysisController {

    @Autowired
    private IDataAnalysisService dataAnalysisService;

    /**
     * @return 进入 数据分析页面
     */
    @GetMapping(value = "/dataAnalysisQN")
    public String dataAnalysis(@RequestParam("qnId") String qnId) {
        return "/site/dataAnalysisQN";
    }

    /**
     * 获取 总人数+填写该问卷的人数+未填写该问卷的人数
     *
     * @param qnId 问卷编号
     * @return 人数
     */
    @GetMapping("/getWriteQuestionnaireNumberByQNId")
    @ResponseBody
    public JSONResult getWriteQuestionnaireNumberByQNId(String qnId) {
        return dataAnalysisService.getWriteQuestionnaireNumberByQNId(qnId);
    }
}
