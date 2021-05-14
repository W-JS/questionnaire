package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IUserCommentService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理用户留言相关请求的控制器类
 */
@Controller
@RequestMapping("/usercomment")
public class UserCommentController {

    @Autowired
    private IUserCommentService userCommentService;

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
}
