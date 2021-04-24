package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IOptionService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.*;

@Service
public class OptionServiceImpl implements IOptionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    @Override
    public List<Map<String, Object>> getAllOptionList() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<OptionEntity> optionList = optionMapper.findAllOptionList();
        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    @Override
    public JSONResult getOptionByQId(String pQId) {
        List<OptionEntity> data = optionMapper.findOptionByQId(pQId);
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
    @Override
    public JSONResult oetOptionSubmit(String oContent, String oCreateTime) {
        String oId = UUIDGenerator.get16UUID();
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        OptionEntity option = new OptionEntity(oId, oContent, qId, StringToDate(oCreateTime));
        System.out.println(option);
        int flag = optionMapper.insertOption(option);

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
    @Override
    public Map<String, Object> GetOnlineUser() {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        UserEntity user = userMapper.findUserByUserId(OnlineUserID);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return map;
    }
}
