package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.OptionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OptionMapper {

    /**
     * 根据 qId 查询当前问题的所有选项（不分页）
     *
     * @param qId 当前问题编号
     * @return 选项信息列表
     */
    List<OptionEntity> findOptionByQId(String qId);

    /**
     * 根据 qId 查询当前问题的所有选项的行数
     *
     * @param qId 当前问题编号
     * @return 选项信息列表的行数
     */
    int findOptionRowsByQId(String qId);

    /**
     * 根据 qId 查询当前问题的所有选项（分页）
     *
     * @param qId    当前问题编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 选项信息列表
     */
    List<OptionEntity> findOptionPageByQId(String qId, int offset, int limit);

    /**
     * 获取所有问卷的所有问题的所有选项信息列表（不分页）
     *
     * @return 选项信息列表
     */
    List<OptionEntity> findAllOptionList();

    /**
     * 获取所有问卷的所有问题信息列表的行数
     *
     * @return 所有问卷的所有问题的行数
     */
    int findAllOptionRows();

    /**
     * 获取所有问卷的所有问题信息列表（分页）
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 所有问卷的所有问题
     */
    List<OptionEntity> findAllOptionPage(int offset, int limit);

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
     * 更新选项信息
     *
     * @param o 选项信息
     * @return 选项是否更新成功
     */
    int updateOptionByOId(OptionEntity o);

    /**
     * 删除选项信息
     *
     * @param oId 当前选项编号
     * @return 选项是否删除成功
     */
    int deleteOptionByOId(String oId);
}