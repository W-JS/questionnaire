package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 处理首页相关请求的控制器类
 */
@Controller
public class IndexController {

    @Autowired
    private IIndexService indexService;

    @Autowired
    private IUserService userService;

    /**
     * 访问URL：http://localhost:8080/questionnaire
     *
     * @return 进入首页
     */
    @GetMapping(value = "/")
    public String root() {
        UserEntity user = (UserEntity) userService.getOnlineUser().getData();
        if (user.getUserType() == 0) {
            // 普通用户
            return "forward:/userIndex/userIndex";
        } else {
            // 管理员
//        return "forward:/user/login";// 转发：不修改URL地址
//        return "redirect:/user/login";// 重定向：修改URL地址
            return "forward:/questionnaire/index";
        }
    }
}
