package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.util.JSONResult;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理首页相关请求的控制器类
 */
@Controller
public class IndexController {

    @Autowired
    private IIndexService indexService;

    /**
     * 访问URL：http://localhost:8080/questionnaire
     *
     * @return 进入首页
     */
    @GetMapping(value = "/")
    public String root() {
//        return "forward:/user/login";// 转发：不修改URL地址，进入login页面无法加载静态资源
//        return "redirect:/user/login";// 重定向：修改URL地址，进入login页面可以加载静态资源
        return "forward:/questionnaire/index";
    }

    /**
     * @return 进入 问卷
     */
    /*@GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(Model model) {
        return "/site/questionnaire";
    }*/

    /**
     * @return 进入 问卷
     */
    @GetMapping(value = "/questionnaire")
    public String jumpQuestionnairePage(@RequestParam("qnId") String qnId, Model model) {
        model.addAttribute("questionnaire", indexService.getQuestionnaire(qnId));

        model.addAttribute("scQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "singleChoice"));// 单项选择题
        model.addAttribute("mcQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "multipleChoice"));// 多项选择题
        model.addAttribute("jmQuestion", indexService.findQuestionByQnIdAndQtId1(qnId, "judgment"));// 判断题
        model.addAttribute("fbQuestion", indexService.findQuestionByQnIdAndQtId2(qnId, "fillBlank"));// 填空题
        model.addAttribute("sQuestion", indexService.findQuestionByQnIdAndQtId2(qnId, "score"));// 评分题

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
        return JSONResult.build(indexService.findQuestionByQnIdAndQtId1(qnId, "singleChoice"));
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
        return indexService.findRearQuestionByQIdAndOId(qId, oId);
    }

    /**
     * 保存用户填写的问卷信息
     *
     * @param JSONsc 单项选择题
     * @param JSONmc 多项选择题
     * @param JSONjm 判断题
     * @param JSONfb 填空题
     * @param JSONs  评分题
     * @return 用户填写的问卷信息是否保存成功
     */
    @PostMapping(value = "/saveSubmit")
    @ResponseBody
    public JSONResult saveSubmit(String JSONsc, String JSONmc, String JSONjm, String JSONfb, String JSONs, String userComments) {
        System.out.println("单项选择题");
        JSONArray sc = JSONArray.fromObject(JSONsc);
        if (sc.size() > 0) {
            for (int i = 0; i < sc.size(); i++) {
                String qId = (String) sc.getJSONObject(i).get("qId");
                String oId = (String) sc.getJSONObject(i).get("oId");
                System.out.println("qId: " + qId + "  oId: " + oId);
            }
        }

        System.out.println("多项选择题");
        JSONArray mc = JSONArray.fromObject(JSONmc);
        if (mc.size() > 0) {
            for (int i = 0; i < mc.size(); i++) {
                String oId = (String) mc.getJSONObject(i).get("oId");
                String qId = (String) mc.getJSONObject(i).get("qId");
                System.out.println("oId: " + oId + "  qId: " + qId);
            }
        }

        System.out.println("判断题");
        JSONArray jm = JSONArray.fromObject(JSONjm);
        if (jm.size() > 0) {
            for (int i = 0; i < jm.size(); i++) {
                String qId = (String) jm.getJSONObject(i).get("qId");
                String oId = (String) jm.getJSONObject(i).get("oId");
                System.out.println("qId: " + qId + "  oId: " + oId);
            }
        }

        System.out.println("填空题");
        JSONArray fb = JSONArray.fromObject(JSONfb);
        if (fb.size() > 0) {
            for (int i = 0; i < fb.size(); i++) {
                String oId = (String) fb.getJSONObject(i).get("oId");
                String value = (String) fb.getJSONObject(i).get("value");
                System.out.println("oId: " + oId + "  value: " + value);
            }
        }

        System.out.println("评分题");
        JSONArray s = JSONArray.fromObject(JSONs);
        if (s.size() > 0) {
            for (int i = 0; i < s.size(); i++) {
                String oId = (String) s.getJSONObject(i).get("oId");
                Double value = Double.parseDouble((String) s.getJSONObject(i).get("value"));
                System.out.println("oId: " + oId + "  value: " + value);
            }
        }

        System.out.println("用户留言: ");
        System.out.println(userComments);

        return JSONResult.build();
    }


    @GetMapping("/JSONResultTest")
    @ResponseBody
    public JSONResult test(String qnId) {
        return JSONResult.build(indexService.findQuestionByQnIdAndQtId2(qnId, "fillBlank"));
//        return indexService.test(qId);
    }
}
