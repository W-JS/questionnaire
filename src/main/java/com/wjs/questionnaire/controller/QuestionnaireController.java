package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 处理问卷相关请求的控制器类
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    private IQuestionnaireService questionnaireService;

    /**
     * @return 进入 调查问卷后台管理-我的问卷
     */
    @GetMapping(value = "/index-example")
    public String jumpMyIndexPage() {
        return "/site/indexExample";
    }

    /**
     * @return 进入 调查问卷后台管理-问卷
     */
    @GetMapping(value = "/index")
    public String jumpIndexPage(Model model, PageUtil pageUtil) {
        pageUtil.setRows(questionnaireService.getQuestionnaireRows());
        pageUtil.setPath("/questionnaire/index");
//        List<Map<String, Object>> questionnaires = questionnaireService.getAllQuestionnaireList();
        List<Map<String, Object>> questionnaires = questionnaireService.findQuestionnairePage(pageUtil.getOffset(), pageUtil.getLimit());
        Map<String, Object> onlineUser = questionnaireService.GetOnlineUser();
        model.addAttribute("questionnaires", questionnaires);
        model.addAttribute("map", onlineUser);
        return "/site/index";
    }

    /**
     * @return 进入 调查问卷后台管理-新建问卷
     */
    @GetMapping(value = "/addQuestionnaire")
    public String jumpAddQuestionnairePage(Model model) {
        Map<String, Object> onlineUser = questionnaireService.GetOnlineUser();
        model.addAttribute("map", onlineUser);
        return "/site/addQuestionnaire";
    }

    /**
     * 保存问卷信息
     *
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷副标题
     * @param qnDescription 问卷描述
     * @param qnStatus      状态：-1-已删除; 0-编辑中; 1-已开启; 2-已结束;
     * @param qnCreateTime  问卷创建时间
     * @param userId        创建人的用户编号
     * @return 问卷信息是否保存成功
     */
    @PostMapping(value = "/questionnaireSubmit")
    @ResponseBody
    public JSONResult getQuestionnaireSubmit(String qnTitle, String qnFuTitle, String qnDescription, int qnStatus, String qnCreateTime, String userId) {
        return questionnaireService.getQuestionnaireSubmit(qnTitle, qnFuTitle, qnDescription, qnStatus, qnCreateTime, userId);
    }

    /**
     * 根据 qnId 得到问卷信息
     *
     * @return 问卷信息
     */
    @GetMapping(value = "/getQuestionnaireByQnId")
    @ResponseBody
    public JSONResult getQuestionnaireByQnId() {
        return questionnaireService.getQuestionnaireByQnId();
    }
}
