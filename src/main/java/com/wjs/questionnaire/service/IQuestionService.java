package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * 处理问题信息数据的业务层接口
 */
public interface IQuestionService {

    /**
     * 设置所有问卷的所有问题信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setQuestionListPage(PageUtil page);

    /**
     * 获取所有问卷的所有问题信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 问题信息列表
     */
    List<Map<String, Object>> getQuestionList(int offset, int limit);

    /**
     * 设置当前问卷所有问题信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setQuestionListPageByQNId(PageUtil page);

    /**
     * 获取当前问卷所有问题信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 问题信息列表
     */
    List<Map<String, Object>> getQuestionListByQNId(int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷的所有问题
     */
    JSONResult getAllQuestionByQnId();

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题的行数
     */
    JSONResult getNoPrependedQuestionRowsByQnId();

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题
     */
    JSONResult getNoPrependedQuestionPage1ByQnId(String current);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     *
     * @param qId     问题编号
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     */
    JSONResult getNoPrependedQuestionPage2ByQnId(String qId, String current);


    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题的行数
     */
    JSONResult getPrependedQuestionRowsByQnId();

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题
     */
    JSONResult getPrependedQuestionPageByQnId(String current);

    /**
     * 一个指定的问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题信息
     */
    JSONResult getQuestionByQnIdAndQId(String qId);

    /**
     * 一个指定的问题和前置问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题和前置问题信息
     */
    JSONResult getQuestionAndPreQuestionByQId(String qId);

    /**
     * 一个指定的问题、问题类型、前置问题和前置选项信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题、问题类型、前置问题和前置选项信息
     */
    JSONResult getQuestionAndPreQuestionAndPreOptionByQId(String qId);

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    JSONResult getPrependedQuestionByQId(String qId);

    /**
     * 如果当前问题有前置问题，则找到当前问题的连续前置问题
     * 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    JSONResult getFinallyPrependedQuestionByQId(String qId, int status);

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    JSONResult getRearQuestionByQId(String qId);

    /**
     * 如果当前问题是被前置问题，则找到当前问题的连续后置问题
     * 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    JSONResult getFinallyRearQuestionByQId(String qId, int status);

    /**
     * 保存问题信息
     *
     * @param qTitle       问题标题
     * @param qDescription 问题描述
     * @param qStatus      问题状态：0-非必填; 1-必填;
     * @param pQId         前置问题
     * @param pOId         前置选项
     * @param qtId         问题类型
     * @param qCreateTime  问题创建时间
     * @return 问题信息是否保存成功
     */
    JSONResult getQuestionSubmit(String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime);

    /**
     * 更新问题信息
     *
     * @param qId          问题编号
     * @param qTitle       问题标题
     * @param qDescription 问题描述
     * @param qStatus      问题状态：0-非必填; 1-必填;
     * @param pQId         前置问题
     * @param pOId         前置选项
     * @param qtId         问题类型
     * @param qCreateTime  问题创建时间
     * @return 问题信息是否更新成功
     */
    JSONResult getUpdateSubmit(String qId, String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime);

    /**
     * 根据 qId 删除问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    JSONResult getDeleteSubmit1(String qId);

    /**
     * 根据 qId 删除问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    JSONResult getDeleteSubmit2(String qId);

    /**
     * 根据 qId 删除多个问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     * * @return 问题信息是否删除成功
     */
    JSONResult getDeleteSubmit3(String question);

    /**
     * @return 存了 user 信息的 map
     */
    Map<String, Object> GetOnlineUser();
}
