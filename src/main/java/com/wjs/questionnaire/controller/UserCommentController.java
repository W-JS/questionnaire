package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.UserCommentEntity;
import com.wjs.questionnaire.service.IUserCommentService;
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
 * 处理用户留言相关请求的控制器类
 */
@Controller
@RequestMapping("/usercomment")
public class UserCommentController {

    @Autowired
    private IUserCommentService userCommentService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return 进入 用户留言-首页
     */
    @GetMapping(value = "/index")
    public String jumpIndexPage(Model model) {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        List<Map<String, Object>> treatedUserComments = userCommentService.getTreatedUserCommentLists(OnlineUserID);
        List<Map<String, Object>> untreatedUserComments = userCommentService.getAllUntreatedUserCommentLists();
        model.addAttribute("treatedUserComments", treatedUserComments);
        model.addAttribute("untreatedUserComments", untreatedUserComments);
        return "/site/userComment";
    }

    /**
     * @return 进入 用户留言-内容
     */
    @GetMapping(value = "/content")
    public String jumpContentPage(@RequestParam("ucId") String ucId, Model model) {
        UserCommentEntity userComment = userCommentService.getUserCommentByUCId(ucId);
        model.addAttribute("userComment", userComment);
        return "/site/usercommentContent";
    }

    /**
     * 保存回复内容
     *
     * @param ucId        用户留言编号
     * @param adminUserId 管理员用户编号
     * @param answer      回复的内容
     * @param answerTime  回复时间
     * @return 回复内容是否保存成功
     */
    @PostMapping(value = "/usercommentSubmit")
    @ResponseBody
    public JSONResult getUserCommentSubmit(String ucId, String adminUserId, String answer, String answerTime) {
        return userCommentService.getUserCommentSubmit(ucId, adminUserId, answer, answerTime);
    }

    /**
     * 得到所有的未处理的用户留言信息的行数
     *
     * @return
     */
    @GetMapping(value = "/getAllUntreatedUserCommentRows")
    @ResponseBody
    public JSONResult getAllUntreatedUserCommentRows() {
        return JSONResult.build(userCommentService.getAllUntreatedUserCommentRows());
    }


    @GetMapping(value = "/test")
    public String jumpTestPage(Model model) {
        return "/mail/answer";
    }
}