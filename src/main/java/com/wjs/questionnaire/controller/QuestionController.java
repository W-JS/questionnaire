package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IQuestionService;
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
 * 处理问题相关请求的控制器类
 */
@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点击问卷表格的问卷，显示问卷的问题
     *
     * @param qnId 需要查看当前问卷所有问题信息的问卷编号
     * @return 进入 调查问卷后台管理-问题
     */
    @GetMapping(value = "/QuestionByQNId")
    public String jumpQuestionPage1(@RequestParam("qnId") String qnId, Model model, PageUtil pageUtil) {
        redisTemplate.opsForValue().set(OnlineQNID, qnId);// 显示当前问卷的所有问题，将 qnId 存进Redis
        PageUtil page = questionService.setQuestionListPageByQNId(pageUtil);
        List<Map<String, Object>> questions = questionService.getQuestionListByQNId(page.getOffset(), page.getLimit());

        model.addAttribute("questions", questions);
        return "/site/question";
    }

    /**
     * 点击问题表格的分页按钮，显示问卷的问题
     *
     * @return 进入 调查问卷后台管理-问题
     */
    @GetMapping(value = "/Question")
    public String jumpQuestionPage2(Model model, PageUtil pageUtil) {
        PageUtil page = questionService.setQuestionListPageByQNId(pageUtil);
        List<Map<String, Object>> questions = questionService.getQuestionListByQNId(page.getOffset(), page.getLimit());

        model.addAttribute("questions", questions);
        return "/site/question";
    }

    /**
     * 点击侧边栏的所有问题按钮，显示所有问卷的所有问题
     *
     * @return 进入 调查问卷后台管理-问题
     */
    @GetMapping(value = "/AllQuestion")
    public String jumpQuestionPage3(Model model, PageUtil pageUtil) {
        PageUtil page = questionService.setAllQuestionListPage(pageUtil);
        List<Map<String, Object>> questions = questionService.getAllQuestionList(page.getOffset(), page.getLimit());

        model.addAttribute("questions", questions);
        return "/site/question";
    }

    /**
     * 根据搜索内容模糊查询问题信息
     *
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 进入 调查问卷后台管理-问题
     */
    @GetMapping(value = "/search")
    public String jumpQuestionSearchPage(Model model, PageUtil pageUtil, @RequestParam("searchWay") String searchWay, @RequestParam("searchContent") String searchContent) {
        PageUtil page = questionService.setLikeQuestionListPage(pageUtil, searchWay, searchContent);
        List<Map<String, Object>> questions = questionService.getLikeQuestionList(page.getOffset(), page.getLimit(), searchWay, searchContent);
        model.addAttribute("questions", questions);
        return "/site/question";
    }

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷的所有问题
     */
    @GetMapping("/getAllQuestionByQnId")
    @ResponseBody
    public JSONResult getAllQuestionByQnId() {
        return questionService.getAllQuestionByQnId();
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题的行数
     */
    @GetMapping("/getNoPrependedQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionRowsByQnId() {
        return questionService.getNoPrependedQuestionRowsByQnId();
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题
     */
    @GetMapping("/getNoPrependedQuestionPage1ByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionPage1ByQnId(String current) {
        return questionService.getNoPrependedQuestionPage1ByQnId(current);
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     *
     * @param qId     问题编号
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     */
    @GetMapping("/getNoPrependedQuestionPage2ByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionPage2ByQnId(String qId, String current) {
        return questionService.getNoPrependedQuestionPage2ByQnId(qId, current);
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题的行数
     */
    @GetMapping("/getPrependedQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getPrependedQuestionRowsByQnId() {
        return questionService.getPrependedQuestionRowsByQnId();
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题
     */
    @GetMapping("/getPrependedQuestionPageByQnId")
    @ResponseBody
    public JSONResult getPrependedQuestionPageByQnId(String current) {
        return questionService.getPrependedQuestionPageByQnId(current);
    }

    /**
     * 根据 qId 得到在线问题信息
     *
     * @return 问题信息
     */
    @GetMapping(value = "/getQuestion")
    @ResponseBody
    public JSONResult getQuestion() {
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        return questionService.getQuestionByQId(qId);
    }

    /**
     * 一个指定的问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题信息
     */
    @GetMapping("/getQuestionByQId")
    @ResponseBody
    public JSONResult getQuestionByQnIdAndQId(String qId) {
        return questionService.getQuestionByQId(qId);
    }

    /**
     * 一个指定的问题和前置问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题和前置问题信息
     */
    @GetMapping("/getQuestionAndPreQuestionByQId")
    @ResponseBody
    public JSONResult getQuestionAndPreQuestionByQId(String qId) {
        return questionService.getQuestionAndPreQuestionByQId(qId);
    }

    /**
     * 一个指定的问题、问题类型、前置问题和前置选项信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题、问题类型、前置问题和前置选项信息
     */
    @GetMapping("/getQuestionAndPreQuestionAndPreOptionByQId")
    @ResponseBody
    public JSONResult getQuestionAndPreQuestionAndPreOptionByQId(String qId) {
        return questionService.getQuestionAndPreQuestionAndPreOptionByQId(qId);
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @GetMapping("/getPrependedQuestionByQId")
    @ResponseBody
    public JSONResult getPrependedQuestionByQId(String qId) {
        return questionService.getPrependedQuestionByQId(qId);
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的连续前置问题
     * 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续前置问题 0: 最先一个前置问题
     * @return 问题信息
     */
    @GetMapping("/getFinallyPrependedQuestionByQId")
    @ResponseBody
    public JSONResult getFinallyPrependedQuestionByQId(String qId, int status) {
        return questionService.getFinallyPrependedQuestionByQId(qId, status);
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @GetMapping("/getRearQuestionByQId")
    @ResponseBody
    public JSONResult getRearQuestionByQId(String qId) {
        return questionService.getRearQuestionByQId(qId);
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的连续后置问题
     * 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    @GetMapping("/getFinallyRearQuestionByQId")
    @ResponseBody
    public JSONResult getFinallyRearQuestionByQId(String qId, int status) {
        return questionService.getFinallyRearQuestionByQId(qId, status);
    }

    /**
     * @return 无实际意义
     */
    @GetMapping("/getAsyncNull")
    @ResponseBody
    public JSONResult getAsyncNull() {
        return JSONResult.build();
    }

    /**
     * 保存问题信息
     *
     * @param qTitle       问题标题
     * @param qDescription 问题描述
     * @param qStatus      问题状态：0-非必填; 1-必填;
     * @param pQId         前置问题
     * @param pOId         前置选项
     * @param qtId         问题类型
     * @param qCreateTime  问题创建时间
     * @return 问题信息是否保存成功
     */
    @PostMapping(value = "/questionSubmit")
    @ResponseBody
    public JSONResult getQuestionSubmit(String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime) {
        return questionService.getQuestionSubmit(qTitle, qDescription, qStatus, pQId, pOId, qtId, qCreateTime);
    }

    /**
     * 更新问题信息
     *
     * @param qId          问题编号
     * @param qTitle       问题标题
     * @param qDescription 问题描述
     * @param qStatus      问题状态：0-非必填; 1-必填;
     * @param pQId         前置问题
     * @param pOId         前置选项
     * @param qtId         问题类型
     * @param qCreateTime  问题创建时间
     * @return 问题信息是否更新成功
     */
    @PostMapping(value = "/updateSubmit")
    @ResponseBody
    public JSONResult getUpdateSubmit(String qId, String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime) {
        return questionService.getUpdateSubmit(qId, qTitle, qDescription, qStatus, pQId, pOId, qtId, qCreateTime);
    }

    /**
     * 根据 qId 删除问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit1")
    @ResponseBody
    public JSONResult getDeleteSubmit1(String qId) {
        return questionService.getDeleteSubmit1(qId);
    }

    /**
     * 根据 qId 删除问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit2")
    @ResponseBody
    public JSONResult getDeleteSubmit2(String qId) {
        return questionService.getDeleteSubmit2(qId);
    }

    /**
     * 根据 qId 删除多个问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     *
     * @param question JSON格式的字符串，包含多个问题编号
     * @return 问题信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit3")
    @ResponseBody
    public JSONResult getDeleteSubmit3(String question) {
        return questionService.getDeleteSubmit3(question);
    }
}
