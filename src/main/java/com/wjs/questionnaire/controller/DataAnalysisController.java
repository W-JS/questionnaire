package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IDataAnalysisService;
import com.wjs.questionnaire.service.IQuestionService;
import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private IQuestionService questionService;

    /**
     * @return 进入 问卷数据分析页面
     */
    @GetMapping(value = "/dataAnalysisQN")
    public String getDataAnalysisQNPage(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaire", questionnaireService.getQuestionnaire(qnId));
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


    /**
     * @return 进入 问题数据分析页面
     */
    @GetMapping(value = "/dataAnalysisQ")
    public String getDataAnalysisQPage(@RequestParam("qnId") String qnId, @RequestParam("qId") String qId, Model model) {
        model.addAttribute("question",questionService.getQuestion(qId));
        return "/site/dataAnalysisQ";
    }

    /**
     * 获取 用户对该问题的回答情况（该问题的每个选项分别有多少人选择）
     *
     * @param qnId 问卷编号
     * @param qId  问题编号
     * @return 单选选择题每个选项的数量
     */
    @GetMapping("/getOptionsNumberByQNIdAndQId")
    @ResponseBody
    public JSONResult getOptionsNumberByQNIdAndQId(String qnId, String qId) {
        return dataAnalysisService.getOptionsNumberByQNIdAndQId(qnId, qId);
    }
}
