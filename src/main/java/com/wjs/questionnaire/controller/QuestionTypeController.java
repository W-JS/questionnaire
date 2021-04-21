package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.entity.QuestionTypeEntity;
import com.wjs.questionnaire.service.IQuestionTypeService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineOID;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQTID;

/**
 * 处理问题相关请求的控制器类
 */
@Controller
@RequestMapping("/questiontype")
public class QuestionTypeController {

    @Autowired
    private IQuestionTypeService questionTypeService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return JSON格式数据：所有问题类型
     */
    @GetMapping("getQuestionType")
    @ResponseBody
    public JSONResult getQuestionType() {
        String qtId = (String) redisTemplate.opsForValue().get(OnlineQTID);

        List<Map<String, Object>> data = new ArrayList<>();
        List<QuestionTypeEntity> list = questionTypeService.getAllQuestionTypeList();

        if (list != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            data.add(map);
        }

        if (qtId != null && !"".equals(qtId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("qtId", qtId);
            data.add(map);
        }

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建数据类型！！！");
        }
        return jsonResult;
    }

    /**
     * 测试JSON返回的数据
     *
     * @return JSON格式数据：测试数据
     */
    @GetMapping("test")
    @ResponseBody
    public JSONResult test() {
        String qtId = (String) redisTemplate.opsForValue().get(OnlineQTID);

        List<Map<String, Object>> data = new ArrayList<>();
        List<QuestionTypeEntity> list = questionTypeService.getAllQuestionTypeList();
        // 1、列表和其他数据存进不同map
//        if (list != null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("list", list);
//            data.add(map);
//        }
//
//        if (qtId != null && !"".equals(qtId)) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("qtId", qtId);
//            data.add(map);
//        }

        // 2、列表和其他数据存进相同map
//        Map<String, Object> map = new HashMap<>();
//        if (list != null && qtId != null && !"".equals(qtId)) {
//            map.put("list", list);
//            map.put("qtId", qtId);
//            data.add(map);
//        }

        // 3、列表中的对象和其他数据存进不同map
//        if (list != null) {
//            for (QuestionTypeEntity questionType : list) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("question", questionType);
//                data.add(map);
//            }
//        }
//        if (qtId != null && !"".equals(qtId)) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("qtId", qtId);
//            data.add(map);
//        }

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建数据类型！！！");
        }
        return jsonResult;
    }

}
