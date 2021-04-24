package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.util.JSONResult;

import java.util.List;
import java.util.Map;

/**
 * 处理选项信息数据的业务层接口
 */
public interface IOptionService {

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    List<Map<String, Object>> getAllOptionList();

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    JSONResult getOptionByQId(String pQId);

    /**
     * 保存选项信息
     *
     * @param oContent    选项内容
     * @param oCreateTime 选项创建时间
     * @return 选项信息是否保存成功
     */
    JSONResult oetOptionSubmit(String oContent, String oCreateTime);

    /**
     * @return 存了 user 信息的 map
     */
    Map<String, Object> GetOnlineUser();
}
