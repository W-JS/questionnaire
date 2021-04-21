package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.JSONResult;
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
import static com.wjs.questionnaire.util.QuestionnaireConstant.ONLINEUSERID;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQNID;

/**
 * 处理问卷相关请求的控制器类
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    private IUserService userService;

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
    public String jumpIndexPage(Model model) {
        List<Map<String, Object>> questionnaires = new ArrayList<>();
        List<QuestionnaireEntity> questionnaireList = questionnaireService.getAllQuestionnaireList();
        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
                questionnaires.add(map);
            }
        }
        model.addAttribute("questionnaires", questionnaires);
        model.addAttribute("map", GetOnlineUser());

        return "/site/index";
    }

    /**
     * @return 进入 调查问卷后台管理-新建问卷
     */
    @GetMapping(value = "/addQuestionnaire")
    public String jumpAddQuestionnairePage(Model model) {
        model.addAttribute("map", GetOnlineUser());
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

        String qnId = UUIDGenerator.get16UUID();
        QuestionnaireEntity questionnaire = new QuestionnaireEntity(qnId, qnTitle, qnFuTitle, qnDescription, qnStatus, StringToDate(qnCreateTime), userId);

        System.out.println(questionnaire.toString());
        int flag = questionnaireService.addQuestionnaire(questionnaire);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineQNID, qnId);// 成功保存问卷信息，将 qnId 存进Redis
        } else {
            jsonResult = JSONResult.build("问卷信息保存失败！！！");
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
