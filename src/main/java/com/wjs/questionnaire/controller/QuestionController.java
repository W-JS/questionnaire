package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.*;
import com.wjs.questionnaire.service.*;
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
    private IQuestionTypeService questionTypeService;

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
    @GetMapping("/getNoPrependedQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionService.getNoPrependedQuestionRowsByQnId(qnId));

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
    @GetMapping("/getNoPrependedQuestionPage1ByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionPage1ByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        List<Map<String, Object>> data = new ArrayList<>();

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionService.getNoPrependedQuestionRowsByQnId(qnId));

        // 未被前置的问题
        List<QuestionEntity> questionList = questionService.getNoPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        JSONResult jsonResult;
        if (questionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionList", questionList);
            data.add(map);
        }
        jsonResult = JSONResult.build(data);
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题
     */
    @GetMapping("/getNoPrependedQuestionPage2ByQnId")
    @ResponseBody
    public JSONResult getNoPrependedQuestionPage2ByQnId(String qId, String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        List<Map<String, Object>> data = new ArrayList<>();

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionService.getNoPrependedQuestionRowsByQnId(qnId));

        // 未被前置的问题
        List<QuestionEntity> questionList = questionService.getNoPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        // 当前问题的传递后置问题
        List<QuestionEntity> rearQuestionList = new ArrayList<>();
        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data1 = questionService.getRearQuestionByQId(qId);
        if (data1 != null) {
            rearQuestionList.add(data1);
        }
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        boolean flag = false;
        do {
            if (data1 != null) {// 当前问题有后置问题
                data2 = questionService.getRearQuestionByQId(data1.getQuestionId());
                if (data2 != null) {
                    data1 = data2;
                    flag = true;
                    // 需要所有的后置问题
                    rearQuestionList.add(data1);
                } else {
                    flag = false;
                }
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (questionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionList", questionList);
            data.add(map);
        }
        if (data1 != null && rearQuestionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("rearQuestionList", rearQuestionList);
            data.add(map);
        }
        jsonResult = JSONResult.build(data);
        return jsonResult;
    }

    /**
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题的行数
     */
    @GetMapping("/getPrependedQuestionRowsByQnId")
    @ResponseBody
    public JSONResult getPrependedQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionService.getPrependedQuestionRowsByQnId(qnId));

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
    @GetMapping("/getPrependedQuestionPageByQnId")
    @ResponseBody
    public JSONResult getPrependedQuestionPageByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionService.getPrependedQuestionRowsByQnId(qnId));

        List<QuestionEntity> data = questionService.getPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

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
    @GetMapping("/getQuestionByQId")
    @ResponseBody
    public JSONResult getQuestionByQnIdAndQId(String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionService.getQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            data.add(map);
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * @param qId 问题编号
     * @return 一个指定的问题和前置问题信息
     */
    @GetMapping("/getQuestionAndPreQuestionByQnIdAndQId")
    @ResponseBody
    public JSONResult getQuestionAndPreQuestionByQnIdAndQId(String qId) {

        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionService.getQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("question", question);
            data.add(map1);

            if (question.getPreQuestionId() != null && !"".equals(question.getPreQuestionId())) {
                QuestionEntity preQuestion = questionService.getQuestionByQId(question.getPreQuestionId());
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
     * @param qId 问题编号
     * @return 一个指定的问题、问题类型、前置问题和前置选项信息
     */
    @GetMapping("/getQuestionAndPreQuestionAndPreOptionByQnIdAndQId")
    @ResponseBody
    public JSONResult getQuestionAndPreQuestionAndPreOptionByQnIdAndQId(String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionService.getQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            data.add(map);

            if (question.getQuestiontypeId() != null && !"".equals(question.getQuestiontypeId())) {
                List<QuestionTypeEntity> list = questionTypeService.getAllQuestionTypeList();
                for (QuestionTypeEntity questionType : list) {
                    if (questionType.getQuestiontypeId().equals(question.getQuestiontypeId())) {
                        map = new HashMap<>();
                        map.put("questionType", questionType);
                        data.add(map);
                        break;
                    }
                }
            }
            if (question.getPreQuestionId() != null && !"".equals(question.getPreQuestionId())) {
                QuestionEntity preQuestion = questionService.getQuestionByQId(question.getPreQuestionId());
                if (preQuestion != null) {
                    map = new HashMap<>();
                    map.put("preQuestion", preQuestion);
                    data.add(map);
                }
            }
            if (question.getPreOptionId() != null && !"".equals(question.getPreOptionId())) {
                OptionEntity preOption = optionService.getOptionByOId(question.getPreOptionId());
                if (preOption != null) {
                    map = new HashMap<>();
                    map.put("preOption", preOption);
                    data.add(map);
                }
            }
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
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
        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data = questionService.getRearQuestionByQId(qId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     * 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @GetMapping("/getFinallyRearQuestionByPrependedByQnIdAndQId")
    @ResponseBody
    public JSONResult getFinallyRearQuestionByPrependedByQnIdAndQId(String qId, int status) {
        List<Map<String, Object>> data = new ArrayList<>();

        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data1 = questionService.getRearQuestionByQId(qId);
        Map<String, Object> map = new HashMap<>();
        if (status == 1) {
            map.put("question", data1);
            data.add(map);
        }
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        boolean flag = false;
        do {
            if (data1 != null) {// 当前问题有后置问题
                data2 = questionService.getRearQuestionByQId(data1.getQuestionId());
                if (data2 != null) {
                    data1 = data2;
                    flag = true;
                    if (status == 1) {
                        // 需要所有的后置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                } else {
                    flag = false;
                    if (status == 0) {
                        // 只需要最后一个的后置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                }
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
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
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        // 如果当前问题有前置问题，则找到当前问题的前置问题
        QuestionEntity data = questionService.getPrependedQuestionByQId(qId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
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
            flag = questionService.addQuestionS(question);
//            flag = questionService.addQuestion(question);
            System.out.println("No: pQId, pOId  " + question);
        } else {
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, StringToDate(qCreateTime));
            flag = questionService.addQuestionS(question);
//            flag = questionService.addQuestions(question);
            System.out.println("Have: pQId, pOId  " + question);
        }

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

        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        QuestionEntity question;
        int flag = 1;
        if ("not".equals(pQId) || "not".equals(pOId)) {// 前置问题和前置选项不做操作
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), qnId, qtId, StringToDate(qCreateTime));
            flag = questionService.modifyQuestionByQId(question);
//            flag = questionService.modifyQuestion(question);
            System.out.println("NoSet: pQId, pOId  " + question);
        } else if ("null".equals(pQId) || "null".equals(pOId)) {// 将前置问题和前置选项置为空
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), null, null, qnId, qtId, StringToDate(qCreateTime));
//            flag = questionService.modifyQuestionByQId(question);
            flag = questionService.modifyQuestions(question);
            System.out.println("SetNULL: pQId, pOId  " + question);
        } else {
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, StringToDate(qCreateTime));
            flag = questionService.modifyQuestionByQId(question);
//            flag = questionService.modifyQuestions(question);
            System.out.println("Set: pQId, pOId  " + question);
        }

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("问题信息更新失败！！！");
        }
        return jsonResult;
    }

    /**
     * 删除问题信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit")
    @ResponseBody
    public JSONResult getDeleteSubmit(String qId) {

        JSONResult jsonResult;
        // 1、根据 qId 得到当前问题的选项列表
        List<OptionEntity> optionList = optionService.getOptionByQId(qId);

        if (optionList != null) {
            // 2、循环删除选项
            boolean flag = true;
            for (OptionEntity option : optionList) {
                if (optionService.deleteOptionByOId(option.getOptionId()) == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //3、删除问题
                if (questionService.deleteQuestionByQId(qId) != 0) {
                    jsonResult = JSONResult.build();
                } else {
                    jsonResult = JSONResult.build("问题信息删除失败！！！");
                }
            } else {
                jsonResult = JSONResult.build("当前问题的选项信息删除失败！！！");
            }

        } else {
            jsonResult = JSONResult.build("当前问题的选项信息不存在！！！");
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
