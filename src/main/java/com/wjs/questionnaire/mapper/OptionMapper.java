package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.OptionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OptionMapper {

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    List<OptionEntity> findAllOptionList();

    /**
     * 根据 qId 查询当前问题的所有选项
     *
     * @param qId 当前问题编号
     * @return 选项信息列表
     */
    List<OptionEntity> findOptionByQId(String qId);

    /**
     * 根据 oId 查询选项信息
     *
     * @param oId 当前选项编号
     * @return 指定的选项信息
     */
    OptionEntity findOptionByOId(String oId);

    /**
     * 保存选项信息
     *
     * @param q 选项信息
     * @return 选项是否保存成功
     */
    int insertOption(OptionEntity q);

    /**
     * 删除选项信息
     *
     * @param oId 当前选项编号
     * @return 选项是否删除成功
     */
    int deleteOptionByOId(String oId);
}