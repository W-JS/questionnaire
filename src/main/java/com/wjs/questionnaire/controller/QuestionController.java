package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IOptionService;
import com.wjs.questionnaire.service.IQuestionService;
import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import com.wjs.questionnaire.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.*;

/**
 * 处理问题相关请求的控制器类
 */
@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IOptionService optionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return 进入 调查问卷后台管理-问题
     */
    @GetMapping(value = "/Question")
    public String jumpQuestionPage(Model model, PageUtil pageUtil) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        QuestionnaireEntity questionnaire = questionnaireService.getQuestionnaireByQnId(qnId);

        pageUtil.setRows(questionService.getQuestionRowsByQnId(qnId));
        pageUtil.setPath("/question/Question");

        List<Map<String, Object>> questions = new ArrayList<>();

        // 根据 qnId 查询当前问卷的所有问题
        List<QuestionEntity> questionList = questionService.getQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        if (questionList != null) {
            for (QuestionEntity question : questionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("question", question);
                questions.add(map);
            }
        }

        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("questions", questions);
        model.addAttribute("map", GetOnlineUser());
        return "/site/Question";
    }

    /**
     * @return 进入 调查问卷后台管理-新建问题
     */
    @GetMapping(value = "/addQuestion")
    public String jumpAddOptionsPage(Model model) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        QuestionnaireEntity questionnaire = questionnaireService.getQuestionnaireByQnId(qnId);

        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("map", GetOnlineUser());
        return "/site/addQuestion";
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷的所有问题
     */
    @GetMapping("/getAllQuestionByQnId")
    @ResponseBody
    public JSONResult getAllQuestionByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        List<QuestionEntity> data = questionService.getAllQuestionByQnId(qnId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题的行数
     */
    @GetMapping("/getNotFrontQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getNotFrontQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionService.getNotFrontQuestionRowsByQnId(qnId));

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题
     */
    @GetMapping("/getNotFrontQuestionPageByQnId")
    @ResponseBody
    public JSONResult getNotFrontQuestionPageByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionService.getNotFrontQuestionRowsByQnId(qnId));

        List<QuestionEntity> data = questionService.getNotFrontQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题的行数
     */
    @GetMapping("/getFrontQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getFrontQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionService.getFrontQuestionRowsByQnId(qnId));

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题
     */
    @GetMapping("/getFrontQuestionPageByQnId")
    @ResponseBody
    public JSONResult getFrontQuestionPageByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionService.getFrontQuestionRowsByQnId(qnId));

        List<QuestionEntity> data = questionService.getFrontQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * @param qId 问题编号
     * @return 一个指定的问题信息
     */
    @GetMapping("/getQuestionByQnIdAndQId")
    @ResponseBody
    public JSONResult getQuestionByQnIdAndQId(String qId) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionService.getQuestionByQnIdAndQId(qnId, qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("question", question);
            data.add(map1);

            if (question.getPreQuestionId() != null && !"".equals(question.getPreQuestionId())) {
                QuestionEntity preQuestion = questionService.getQuestionByQnIdAndQId(qnId, question.getPreQuestionId());
                if (preQuestion != null) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("preQuestion", preQuestion);
                    data.add(map2);
                }
                jsonResult = JSONResult.build(data);
            } else {
                jsonResult = JSONResult.build(data);
            }
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }


    /**
     * @return 无实际意义
     */
    @GetMapping("/getAsyncNull")
    @ResponseBody
    public JSONResult getAsyncNull() {
        JSONResult jsonResult;
        jsonResult = JSONResult.build();
        return jsonResult;
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

        String qId = UUIDGenerator.get16UUID();
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        QuestionEntity question;
        int flag;
        if ("null".equals(pQId) || "null".equals(pOId)) {
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), qnId, qtId, StringToDate(qCreateTime));
            flag = questionService.addQuestion(question);
        } else {
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, StringToDate(qCreateTime));
            flag = questionService.addQuestions(question);
        }
        System.out.println(question);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineQTID, qtId);// 成功保存问题类型信息，将 qtId 存进Redis
            redisTemplate.opsForValue().set(OnlineQID, qId);// 成功保存问题信息，将 qId 存进Redis
        } else {
            jsonResult = JSONResult.build("问题信息保存失败！！！");
        }
        return jsonResult;
    }

    /**
     * @return 存了 user 信息的 map
     */
    public Map<String, Object> GetOnlineUser() {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        UserEntity user = userService.getUserByUserId(OnlineUserID);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return map;
    }
}
