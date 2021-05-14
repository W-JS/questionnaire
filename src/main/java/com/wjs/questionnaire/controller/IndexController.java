package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理首页相关请求的控制器类
 */
@Controller
public class IndexController {

    @Autowired
    private IIndexService indexService;

    /**
     * 访问URL：http://localhost:8080/questionnaire
     *
     * @return 进入首页
     */
    @GetMapping(value = "/")
    public String root() {
//        return "forward:/user/login";// 转发：不修改URL地址
//        return "redirect:/user/login";// 重定向：修改URL地址
        return "forward:/questionnaire/index";
    }

    /**
     * @return 进入 问卷
     */
    @GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaire", indexService.getAnswerAndQuestionnaire(qnId));

        model.addAttribute("scQuestion", indexService.getQuestionByQnIdAndQtId1(qnId, "singleChoice"));// 单项选择题
        model.addAttribute("mcQuestion", indexService.getQuestionByQnIdAndQtId1(qnId, "multipleChoice"));// 多项选择题
        model.addAttribute("jmQuestion", indexService.getQuestionByQnIdAndQtId1(qnId, "judgment"));// 判断题
        model.addAttribute("fbQuestion", indexService.getQuestionByQnIdAndQtId2(qnId, "fillBlank"));// 填空题
        model.addAttribute("sQuestion", indexService.getQuestionByQnIdAndQtId2(qnId, "score"));// 评分题

        return "/site/questionnaire";
    }

    /**
     * @return 进入 数据分析页面
     */
    @GetMapping(value = "/dataAnalysis")
    public String dataAnalysis(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaireId", qnId);
        return "/site/dataAnalysis";
    }

    /**
     * 根据 qnId 查询当前问卷（问卷信息 + 问题信息 + 选项信息）
     *
     * @param qnId 问卷编号
     * @return JSON格式数据：根据 qnId 查询当前问卷
     */
    @GetMapping("/getQuestionnaireByQnId")
    @ResponseBody
    public JSONResult getQuestionnaireByQnId(String qnId) {
        return JSONResult.build(indexService.getQuestionByQnIdAndQtId1(qnId, "singleChoice"));
//        return JSONResult.build(indexService.findQuestionByQnIdAndQtId2(qnId, "score"));
//        return indexService.getQuestionnaireByQnId(qnId);
    }

    /**
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return JSON格式数据
     */
    @GetMapping("/getRearQuestionByQIdAndOId")
    @ResponseBody
    public JSONResult getRearQuestionByQIdAndOId(String qId, String oId) {
        return indexService.getRearQuestionByQIdAndOId(qId, oId);
    }

    /**
     * 保存用户填写的问卷信息
     *
     * @param userId       用户编号
     * @param qnId         问卷编号
     * @param JSONsc       单项选择题
     * @param JSONmc       多项选择题
     * @param JSONjm       判断题
     * @param JSONfb       填空题
     * @param JSONs        评分题
     * @param userComments 用户留言
     * @return 用户填写的问卷信息是否保存成功
     */
    @PostMapping(value = "/saveSubmit")
    @ResponseBody
    public JSONResult saveSubmit(String userId, String qnId, String JSONsc, String JSONmc, String JSONjm, String JSONfb, String JSONs, String userComments) {
        return indexService.saveSubmit(userId, qnId, JSONsc, JSONmc, JSONjm, JSONfb, JSONs, userComments);
    }

    /**
     * 获取当前登录用户填写的该问卷的回答信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 回答信息列表
     */
    @GetMapping("/getAllAnswerByUserIdAndQNId")
    @ResponseBody
    public JSONResult getAllAnswerByUserIdAndQNId(String userId, String qnId) {
        return indexService.getAllAnswerByUserIdAndQNId(userId, qnId);
    }


    @GetMapping("/JSONResultTest")
    @ResponseBody
    public JSONResult test(String userId, String qnId) {
        return JSONResult.build(indexService.getQuestionByQnIdAndQtId2(qnId, "fillBlank"));
//        return indexService.test(qId);
    }
}
