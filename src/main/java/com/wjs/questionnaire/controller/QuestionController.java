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

import java.util.*;

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
     * 根据 qnId 查询当前问卷的所有问题
     *
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
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
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
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param current 当前页码
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
     * 根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     *
     * @param qId     问题编号
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题和连续后置问题
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
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        boolean flag = false;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionService.getRearQuestionByQId(qId);
            if (data2 != null) {
                // 需要所有的后置问题
                rearQuestionList.add(data2);
                data1 = data2;
                flag = true;
            } else {
                if (!flag) {
                    data1 = null;
                }
                flag = false;
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (questionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionList", questionList);
            data.add(map);
        }
        if (data1 != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("rearQuestionList", rearQuestionList);
            data.add(map);
        }
        jsonResult = JSONResult.build(data);
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
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
     * 根据 qnId 查询当前问卷被前置的问题
     *
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
     * 一个指定的问题信息
     *
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
     * 一个指定的问题和前置问题信息
     *
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
     * 一个指定的问题、问题类型、前置问题和前置选项信息
     *
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
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @GetMapping("/getPrependedQuestionByQId")
    @ResponseBody
    public JSONResult getPrependedQuestionByQId(String qId) {
        // 如果当前问题有前置问题，则找到当前问题的前置问题
        QuestionEntity data = questionService.getPrependedQuestionByQId(qId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("当前问题没有前置问题！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的连续前置问题
     * 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    @GetMapping("/getFinallyPrependedQuestionByQId")
    @ResponseBody
    public JSONResult getFinallyPrependedQuestionByQId(String qId, int status) {
        List<Map<String, Object>> data = new ArrayList<>();

        // 如果当前问题有前置问题，则找到当前问题的前置问题
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
        QuestionEntity data2;

        Map<String, Object> map;
        boolean flag = false;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionService.getPrependedQuestionByQId(qId);
            if (data2 != null) {
                if (status == 1) {
                    // 需要所有的前置问题
                    map = new HashMap<>();
                    map.put("question", data2);
                    data.add(map);
                }
                data1 = data2;
                flag = true;
            } else {
                if (flag) {
                    if (status == 0) {
                        // 只需要最后一个的前置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                }
                flag = false;
            }
        } while (flag);// 当前问题无前置问题

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(OBJECT_EXISTENCE, "当前问题的连续前置问题", data);
        } else {
            jsonResult = JSONResult.build("当前问题没有连续前置问题！！！");
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
            jsonResult = JSONResult.build("当前问题没有后置问题！！！");
        }
        return jsonResult;
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
        List<Map<String, Object>> data = new ArrayList<>();

        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        Map<String, Object> map;
        boolean flag = false;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionService.getRearQuestionByQId(qId);
            if (data2 != null) {
                if (status == 1) {
                    // 需要所有的后置问题
                    map = new HashMap<>();
                    map.put("question", data2);
                    data.add(map);
                }
                data1 = data2;
                flag = true;
            } else {
                if (flag) {
                    if (status == 0) {
                        // 只需要最后一个的后置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                }
                flag = false;
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(OBJECT_EXISTENCE, "当前问题的连续后置问题", data);
        } else {
            jsonResult = JSONResult.build("当前问题没有连续后置问题！！！");
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
     * 根据 qId 删除问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit1")
    @ResponseBody
    public JSONResult getDeleteSubmit1(String qId) {

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
        jsonResult = JSONResult.build();
        return jsonResult;
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

        JSONResult jsonResult;
        boolean flag1 = true;
        // 1、如果当前问题有前置问题，则找到当前问题的连续前置问题
        jsonResult = getFinallyPrependedQuestionByQId(qId, 1);
        if (jsonResult.getState() == 1) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) jsonResult.getData();
            Collections.reverse(data);// List数据反转
            if (data != null) {
                for (Map<String, Object> map : data) {
                    QuestionEntity question = (QuestionEntity) map.get("question");
                    if (question != null) {
                        // 根据 qId 删除问题信息及关联的选项信息
                        jsonResult = getDeleteSubmit1(question.getQuestionId());
                        if (jsonResult.getState() != 1) {
                            flag1 = false;
                            System.out.println("删除前置问题 失败：" + question.getQuestionTitle());
                            break;
                        } else {
                            System.out.println("删除前置问题 成功：" + question.getQuestionTitle());
                        }
                    }

                }
            }
        }

        boolean flag2 = true;
        // 2、如果当前问题是被前置问题，则找到当前问题的连续后置问题
        jsonResult = getFinallyRearQuestionByQId(qId, 1);
        if (jsonResult.getState() == 1) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) jsonResult.getData();
            if (data != null) {
                for (Map<String, Object> map : data) {
                    QuestionEntity question = (QuestionEntity) map.get("question");
                    if (question != null) {
                        // 根据 qId 删除问题信息及关联的选项信息
                        jsonResult = getDeleteSubmit1(question.getQuestionId());
                        if (jsonResult.getState() != 1) {
                            flag2 = false;
                            System.out.println("删除后置问题 失败：" + question.getQuestionTitle());
                            break;
                        } else {
                            System.out.println("删除后置问题 成功：" + question.getQuestionTitle());
                        }
                    }
                }
            }
        }
        if (flag1 && flag2) {
            // 根据 qId 删除问题信息及关联的选项信息
            String qTitle = questionService.getQuestionByQId(qId).getQuestionTitle();
            jsonResult = getDeleteSubmit1(qId);
            if (jsonResult.getState() == 1) {
                System.out.println("删除问题 成功：" + qTitle);
                jsonResult = JSONResult.build();
            } else {
                System.out.println("删除问题 失败：" + qTitle);
                jsonResult = JSONResult.build("问题信息删除失败！！！");
            }
        } else if (!flag1) {
            jsonResult = JSONResult.build("连续前置问题信息删除失败！！！");
        } else if (!flag2) {
            jsonResult = JSONResult.build("连续后置问题信息删除失败！！！");
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
