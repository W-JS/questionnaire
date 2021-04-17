package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements IOptionService {

    @Autowired
    private OptionMapper optionMapper;

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    @Override
    public List<OptionEntity> getAllOptionList() {
        return optionMapper.findAllOptionList();
    }

    /**
     * 根据 qnId 查询当前问题的所有选项
     *
     * @param qId 当前问题编号
     * @return 选项信息列表
     */
    @Override
    public List<OptionEntity> getOptionByQId(String qId) {
        return optionMapper.findOptionByQId(qId);
    }

    /**
     * 保存选项信息
     *
     * @param q 选项信息
     * @return 选项是否保存成功
     */
    @Override
    public int addOption(OptionEntity q) {
        return optionMapper.insertOption(q);
    }
}
