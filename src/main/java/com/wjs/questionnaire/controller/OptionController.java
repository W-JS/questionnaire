package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IOptionService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 处理选项相关请求的控制器类
 */
@Controller
@RequestMapping("/option")
public class OptionController {

    @Autowired
    private IOptionService optionService;

    /**
     * 访问URL：http://localhost:8080/questionnaire/Options
     *
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/Option")
    public String jumpOptionPage(Model model) {
        List<Map<String, Object>> options = optionService.getAllOptionList();
        Map<String, Object> onlineUser = optionService.GetOnlineUser();

        model.addAttribute("options", options);
        model.addAttribute("map", onlineUser);
        return "/site/Option";
    }

    /**
     * 访问URL：http://localhost:8080/questionnaire/addOptions
     *
     * @return 进入 调查问卷后台管理-新建选项
     */
    @GetMapping(value = "/addOption")
    public String jumpAddOptionPage(Model model) {
        Map<String, Object> onlineUser = optionService.GetOnlineUser();
        model.addAttribute("map", onlineUser);
        return "/site/addOption";
    }

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    @GetMapping("/getOptionByQId")
    @ResponseBody
    public JSONResult getOptionByQId(String pQId) {
        return optionService.getOptionByQId(pQId);
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
        return optionService.oetOptionSubmit(oContent, oCreateTime);
    }

}
