package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionTypeEntity;
import com.wjs.questionnaire.mapper.QuestionTypeMapper;
import com.wjs.questionnaire.service.IQuestionTypeService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQTID;

@Service
public class QuestionTypeServiceImpl implements IQuestionTypeService {

    @Autowired
    private QuestionTypeMapper questiontypeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return JSON格式数据：所有问题类型
     */
    @Override
    public JSONResult getQuestionType() {
        String qtId = (String) redisTemplate.opsForValue().get(OnlineQTID);

        List<Map<String, Object>> data = new ArrayList<>();
        List<QuestionTypeEntity> list = questiontypeMapper.findAllQuestionTypeList();

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
}
