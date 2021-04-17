package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.OptionEntity;

import java.util.List;

/**
 * 处理选项信息数据的业务层接口
 */
public interface IOptionService {

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    List<OptionEntity> getAllOptionList();

    /**
     * 根据 qId 查询当前问题的所有选项
     *
     * @param qId 当前问题编号
     * @return 选项信息列表
     */
    List<OptionEntity> getOptionByQId(String qId);

    /**
     * 保存选项信息
     *
     * @param q 选项信息
     * @return 选项是否保存成功
     */
    int addOption(OptionEntity q);
}
