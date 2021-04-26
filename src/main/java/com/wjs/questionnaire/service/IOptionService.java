package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * 处理选项信息数据的业务层接口
 */
public interface IOptionService {

    /**
     * 设置当前问题所有选项信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setOptionListPageByQId(PageUtil page);

    /**
     * 获取当前问题所有选项信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 选项信息列表
     */
    List<Map<String, Object>> getOptionListByQId(int offset, int limit);

    /**
     * 设置所有问卷的所有问题的所有选项信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setAllOptionListPage(PageUtil page);

    /**
     * 获取所有问卷的所有问题的所有选项信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 选项信息列表
     */
    List<Map<String, Object>> getAllOptionList(int offset, int limit);

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    List<Map<String, Object>> getAllOptionList();

    /**
     * 根据 oId 得到选项信息
     *
     * @param oId 选项编号
     * @return JSON格式数据：选项信息
     */
    JSONResult getOptionByOId(String oId);

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
     * 更新选项信息
     *
     * @param oId
     * @param oContent
     * @param oCreateTime
     * @return 选项信息是否更新成功
     */
    JSONResult getUpdateSubmit(String oId, String oContent, String oCreateTime);

    /**
     * 根据 oId 删除选项信息
     *
     * @param oId 选项编号
     * @return 选项信息是否删除成功
     */
    JSONResult getDeleteSubmit1(String oId);

    /**
     * 根据 oId 删除多个选项信息
     *
     * @param option JSON格式的字符串，包含多个选项编号
     * @return 选项信息是否删除成功
     */
    JSONResult getDeleteSubmit2(String option);

    /**
     * @return 存了 user 信息的 map
     */
    Map<String, Object> GetOnlineUser();
}
