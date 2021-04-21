package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IOptionService;
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
import static com.wjs.questionnaire.util.QuestionnaireConstant.*;

/**
 * 处理选项相关请求的控制器类
 */
@Controller
@RequestMapping("/option")
public class OptionController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOptionService optionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 访问URL：http://localhost:8080/questionnaire/Options
     *
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/Option")
    public String jumpOptionPage(Model model) {
        List<Map<String, Object>> options = new ArrayList<>();
        List<OptionEntity> optionList = optionService.getAllOptionList();
        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
                options.add(map);
            }
        }

        model.addAttribute("options", options);
        model.addAttribute("map", GetOnlineUser());
        return "/site/Option";
    }

    /**
     * 访问URL：http://localhost:8080/questionnaire/addOptions
     *
     * @return 进入 调查问卷后台管理-新建选项
     */
    @GetMapping(value = "/addOption")
    public String jumpAddOptionPage(Model model) {
        model.addAttribute("map", GetOnlineUser());
        return "/site/addOption";
    }

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    @GetMapping("/getOptionByQId")
    @ResponseBody
    public JSONResult getOptionByQId(String pQId) {
        List<OptionEntity> data = optionService.getOptionByQId(pQId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * 保存选项信息
     *
     * @param oContent    选项内容
     * @param oCreateTime 选项创建时间
     * @return 选项信息是否保存成功
     */
    @PostMapping(value = "/optionSubmit")
    @ResponseBody
    public JSONResult oetOptionSubmit(String oContent, String oCreateTime) {

        String oId = UUIDGenerator.get16UUID();
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        OptionEntity option = new OptionEntity(oId, oContent, qId, StringToDate(oCreateTime));
        System.out.println(option);
        int flag = optionService.addOption(option);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineOID, oId);// 成功保存问题信息，将 qId 存进Redis
        } else {
            jsonResult = JSONResult.build("选项信息保存失败！！！");
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
