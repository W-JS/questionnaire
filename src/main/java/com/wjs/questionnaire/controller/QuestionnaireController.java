package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQID;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQNID;

/**
 * 处理问卷相关请求的控制器类
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private RedisTemplate redisTemplate;

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
//        List<Map<String, Object>> questionnaires = questionnaireService.getAllQuestionnaireList();
        PageUtil page = questionnaireService.setQuestionnaireListPage(pageUtil);
        List<Map<String, Object>> questionnaires = questionnaireService.getQuestionnaireList(page.getOffset(), page.getLimit());
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
     * 根据搜索内容模糊查询问卷信息
     *
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 进入 调查问卷后台管理-问卷
     */
    @GetMapping(value = "/search")
    public String jumpIndexSearchPage(Model model, PageUtil pageUtil, @RequestParam("searchWay") String searchWay, @RequestParam("searchContent") String searchContent) {
        PageUtil page = questionnaireService.setLikeQuestionnaireListPage(pageUtil, searchWay, searchContent);
        List<Map<String, Object>> questionnaires = questionnaireService.getLikeQuestionnaireList(page.getOffset(), page.getLimit(), searchWay, searchContent);
        Map<String, Object> onlineUser = questionnaireService.GetOnlineUser();
        model.addAttribute("questionnaires", questionnaires);
        model.addAttribute("map", onlineUser);
        return "/site/index";
    }

    /**
     * 根据 qnId 得到在线问卷信息
     *
     * @return 问卷信息
     */
    @GetMapping(value = "/getQuestionnaire")
    @ResponseBody
    public JSONResult getQuestionnaire() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        return questionnaireService.getQuestionnaireByQnId(qnId);
    }

    /**
     * 根据 qnId 和 qId 得到 在线问卷信息 和 在线问题信息
     *
     * @return 问卷信息和问题信息
     */
    @GetMapping(value = "/getQuestionnaireAndQuestion")
    @ResponseBody
    public JSONResult getQuestionnaireAndQuestion() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        return questionnaireService.getQuestionnaireAndQuestionByQnIdAndQId(qnId, qId);
    }

    /**
     * 根据 qnId 得到问卷信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    @GetMapping(value = "/getQuestionnaireByQnId")
    @ResponseBody
    public JSONResult getQuestionnaireByQnId(String qnId) {
        return questionnaireService.getQuestionnaireByQnId(qnId);
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
     * 更新问卷信息
     *
     * @param qnId          问卷编号
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷标题
     * @param qnDescription 问卷描述
     * @param qnCreateTime  问卷创建时间
     * @return 问卷信息是否更新成功
     */
    @PostMapping(value = "/updateSubmit")
    @ResponseBody
    public JSONResult getUpdateSubmit(String qnId, String qnTitle, String qnFuTitle, String qnDescription, String qnCreateTime) {
        return questionnaireService.getUpdateSubmit(qnId, qnTitle, qnFuTitle, qnDescription, qnCreateTime);
    }

    /**
     * 根据 qnId 删除问卷信息、问题信息及关联的选项信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit1")
    @ResponseBody
    public JSONResult getDeleteSubmit1(String qnId) {
        return questionnaireService.getDeleteSubmit1(qnId);
    }

    /**
     * 根据 qnId 删除多个问卷信息、问题信息及关联的选项信息
     *
     * @param questionnaire JSON格式的字符串，包含多个问卷编号
     * @return 问卷信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit2")
    @ResponseBody
    public JSONResult getDeleteSubmit2(String questionnaire) {
        return questionnaireService.getDeleteSubmit2(questionnaire);
    }

    /**
     * 生成测试问卷信息
     */
    @GetMapping(value = "/GenerateQuestionnaire")
    @ResponseBody
    public JSONResult GenerateQuestionnaire() throws Exception {
        return questionnaireService.GenerateQuestionnaire();
    }
}
