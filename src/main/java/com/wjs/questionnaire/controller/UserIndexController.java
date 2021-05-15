package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IUserIndexService;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.QuestionnaireConstant.ONLINEUSERID;

/**
 * 处理用户首页相关请求的控制器类
 */
@Controller
@RequestMapping("/userIndex")
public class UserIndexController {

    @Autowired
    private IUserIndexService userIndexService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return 进入 个人首页
     */
    @GetMapping(value = "/userIndex")
    public String getUserIndex(Model model) {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        List<Map<String, Object>> completeQuestionnaires = userIndexService.getCompleteQuestionnaireByUserId(OnlineUserID);
        List<Map<String, Object>> notCompleteQuestionnaires = userIndexService.getNotCompleteQuestionnaireByUserId(OnlineUserID);
        model.addAttribute("completeQuestionnaires", completeQuestionnaires);
        model.addAttribute("notCompleteQuestionnaires", notCompleteQuestionnaires);
        return "/site/userIndex";
    }

    /**
     * @return 进入 个人中心
     */
    @GetMapping(value = "/personalCenter")
    public String getPersonalCenter(Model model) {
        UserEntity user = (UserEntity) userService.getOnlineUser().getData();
        model.addAttribute("user", user);
        return "/site/personalCenter";
    }

    /**
     * @return 进入 问卷
     */
    @GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaire", userIndexService.getAnswerAndQuestionnaire(qnId));

        model.addAttribute("scQuestion", userIndexService.getQuestionByQnIdAndQtId1(qnId, "singleChoice"));// 单项选择题
        model.addAttribute("mcQuestion", userIndexService.getQuestionByQnIdAndQtId1(qnId, "multipleChoice"));// 多项选择题
        model.addAttribute("jmQuestion", userIndexService.getQuestionByQnIdAndQtId1(qnId, "judgment"));// 判断题
        model.addAttribute("fbQuestion", userIndexService.getQuestionByQnIdAndQtId2(qnId, "fillBlank"));// 填空题
        model.addAttribute("sQuestion", userIndexService.getQuestionByQnIdAndQtId2(qnId, "score"));// 评分题

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
        return JSONResult.build(userIndexService.getQuestionByQnIdAndQtId1(qnId, "singleChoice"));
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
        return userIndexService.getRearQuestionByQIdAndOId(qId, oId);
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
        return userIndexService.saveSubmit(userId, qnId, JSONsc, JSONmc, JSONjm, JSONfb, JSONs, userComments);
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
        return userIndexService.getAllAnswerByUserIdAndQNId(userId, qnId);
    }

    /**
     * 根据 userId 和 qnId 删除已填写的问卷信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 已填写的问卷信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit")
    @ResponseBody
    public JSONResult getDeleteSubmit(String userId, String qnId) {
        return userIndexService.getDeleteSubmit(userId, qnId);
    }
}
