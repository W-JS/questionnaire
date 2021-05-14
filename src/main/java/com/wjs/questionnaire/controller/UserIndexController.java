package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IUserIndexService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理用户首页相关请求的控制器类
 */
@Controller
@RequestMapping("/userIndex")
public class UserIndexController {

    @Autowired
    private IUserIndexService userIndexService;

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
