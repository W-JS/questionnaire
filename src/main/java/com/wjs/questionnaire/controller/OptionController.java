package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IOptionService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQID;

/**
 * 处理选项相关请求的控制器类
 */
@Controller
@RequestMapping("/option")
public class OptionController {

    @Autowired
    private IOptionService optionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点击问题表格的问题，显示问题的选项
     *
     * @param qId 需要查看当前问题所有选项信息的问题编号
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/OptionByQId")
    public String jumpOptionPage1(@RequestParam("qId") String qId, Model model, PageUtil pageUtil) {
        redisTemplate.opsForValue().set(OnlineQID, qId);// 显示当前问题的所有选项，将 qId 存进Redis
        PageUtil page = optionService.setOptionListPageByQId(pageUtil);
        List<Map<String, Object>> options = optionService.getOptionListByQId(page.getOffset(), page.getLimit());

        model.addAttribute("options", options);
        return "/site/Option";
    }

    /**
     * 点击选项表格的分页按钮，显示问题的选项
     *
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/Option")
    public String jumpOptionPage2(Model model, PageUtil pageUtil) {
//        List<Map<String, Object>> options = optionService.getAllOptionList();
        PageUtil page = optionService.setOptionListPageByQId(pageUtil);
        List<Map<String, Object>> options = optionService.getOptionListByQId(page.getOffset(), page.getLimit());

        model.addAttribute("options", options);
        return "/site/Option";
    }

    /**
     * 点击侧边栏的所有选项按钮，显示所有问卷的所有问题的所有选项
     *
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/AllOption")
    public String jumpOptionPage3(Model model, PageUtil pageUtil) {
        PageUtil page = optionService.setAllOptionListPage(pageUtil);
        List<Map<String, Object>> options = optionService.getAllOptionList(page.getOffset(), page.getLimit());

        model.addAttribute("options", options);
        return "/site/Option";
    }

    /**
     * 根据搜索内容模糊查询选项信息
     *
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 进入 调查问卷后台管理-选项
     */
    @GetMapping(value = "/search")
    public String jumpOptionSearchPage(Model model, PageUtil pageUtil, @RequestParam("searchWay") String searchWay, @RequestParam("searchContent") String searchContent) {
        PageUtil page = optionService.setLikeOptionListPage(pageUtil, searchWay, searchContent);
        List<Map<String, Object>> options = optionService.getLikeOptionList(page.getOffset(), page.getLimit(), searchWay, searchContent);
        model.addAttribute("options", options);
        return "/site/Option";
    }

    /**
     * 根据 oId 得到选项信息
     *
     * @param oId 选项编号
     * @return JSON格式数据：选项信息
     */
    @GetMapping("/getOptionByOId")
    @ResponseBody
    public JSONResult getOptionByOId(String oId) {
        return optionService.getOptionByOId(oId);
    }

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    @GetMapping("/getOptionByPQId")
    @ResponseBody
    public JSONResult getOptionByPQId(String pQId) {
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
    public JSONResult getOptionSubmit(String oContent, String oCreateTime) {
        return optionService.oetOptionSubmit(oContent, oCreateTime);
    }

    /**
     * 更新选项信息
     *
     * @param oId
     * @param oContent
     * @param oCreateTime
     * @return 选项信息是否更新成功
     */
    @PostMapping(value = "/updateSubmit")
    @ResponseBody
    public JSONResult getUpdateSubmit(String oId, String oContent, String oCreateTime) {
        return optionService.getUpdateSubmit(oId, oContent, oCreateTime);
    }

    /**
     * 根据 oId 删除选项信息
     *
     * @param oId 选项编号
     * @return 选项信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit1")
    @ResponseBody
    public JSONResult getDeleteSubmit1(String oId) {
        return optionService.getDeleteSubmit1(oId);
    }

    /**
     * 根据 oId 删除多个选项信息
     *
     * @param option JSON格式的字符串，包含多个选项编号
     * @return 选项信息是否删除成功
     */
    @PostMapping(value = "/deleteSubmit2")
    @ResponseBody
    public JSONResult getDeleteSubmit2(String option) {
        return optionService.getDeleteSubmit2(option);
    }
}
