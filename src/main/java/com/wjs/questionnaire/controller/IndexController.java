package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
//        return "forward:/user/login";// 转发：不修改URL地址，进入login页面无法加载静态资源
//        return "redirect:/user/login";// 重定向：修改URL地址，进入login页面可以加载静态资源
        return "forward:/questionnaire/index";
    }

    /**
     * @return 进入 问卷
     */
    /*@GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(Model model) {
        return "/site/questionnaire";
    }*/

    /**
     * @return 进入 问卷
     */
    @GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaire", indexService.getQuestionnaire(qnId));

        model.addAttribute("scQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "singleChoice"));// 单项选择题
        model.addAttribute("mcQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "multipleChoice"));// 多项选择题
        model.addAttribute("jmQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "judgment"));// 判断题
        model.addAttribute("fbQuestion", indexService.findQuestionByQnIdAndQtId2(qnId, "fillBlank"));// 填空题
        model.addAttribute("sQuestion", indexService.findQuestionByQnIdAndQtId2(qnId, "score"));// 评分题

        return "/site/questionnaire";
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
        return JSONResult.build(indexService.findQuestionByQnIdAndQtId1(qnId, "singleChoice"));
//        return JSONResult.build(indexService.findQuestionByQnIdAndQtId2(qnId, "score"));
//        return indexService.getQuestionnaireByQnId(qnId);
    }
}
