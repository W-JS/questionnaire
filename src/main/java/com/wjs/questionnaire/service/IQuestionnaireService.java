package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * 处理问卷信息数据的业务层接口
 */
public interface IQuestionnaireService {

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    List<Map<String, Object>> getAllQuestionnaireList();

    /**
     * 设置分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setQuestionnaireListPage(PageUtil page);

    /**
     * 获取所有问卷信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问卷信息列表
     */
    List<Map<String, Object>> getQuestionnaireList(int offset, int limit);

    /**
     * 设置分页参数
     *
     * @param page          分页对象参数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 分页结果
     */
    PageUtil setLikeQuestionnaireListPage(PageUtil page, String searchWay, String searchContent);

    /**
     * 获取模糊查询 问卷标题 的问卷信息列表
     *
     * @param offset        从第几条数据查询
     * @param limit         需要查询的记录条数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 问卷信息列表
     */
    List<Map<String, Object>> getLikeQuestionnaireList(int offset, int limit, String searchWay, String searchContent);

    /**
     * 根据 qnId 得到问卷信息
     *
     * @param qnId 问卷编号
     * @return JSON格式数据：问卷信息
     */
    JSONResult getQuestionnaireByQnId(String qnId);

    /**
     * 根据 qnId 和 qId 得到 在线问卷信息 和 在线问题信息
     *
     * @param qnId 在线问卷编号
     * @param qId  在线问题编号
     * @return 问卷信息和问题信息
     */
    JSONResult getQuestionnaireAndQuestionByQnIdAndQId(String qnId, String qId);

    /**
     * 保存问卷信息
     *
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷副标题
     * @param qnDescription 问卷描述
     * @param qnStatus      状态：-1-已删除; 0-编辑中; 1-已开启; 2-已结束;
     * @param qnCreateTime  问卷创建时间
     * @param userId        创建人的用户编号
     * @return 问卷信息是否保存成功
     */
    JSONResult getQuestionnaireSubmit(String qnTitle, String qnFuTitle, String qnDescription, int qnStatus, String qnCreateTime, String userId);

    /**
     * 更新问卷信息
     *
     * @param qnId          问卷编号
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷标题
     * @param qnDescription 问卷描述
     * @param qnCreateTime  问卷创建时间
     * @return 问卷信息是否更新成功
     */
    JSONResult getUpdateSubmit(String qnId, String qnTitle, String qnFuTitle, String qnDescription, String qnCreateTime);

    /**
     * 根据 qnId 删除问卷信息、问题信息及关联的选项信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息是否删除成功
     */
    JSONResult getDeleteSubmit1(String qnId);

    /**
     * 根据 qnId 删除多个问卷信息、问题信息及关联的选项信息
     *
     * @param questionnaire JSON格式的字符串，包含多个问卷编号
     * @return 问卷信息是否删除成功
     */
    JSONResult getDeleteSubmit2(String questionnaire);

    /**
     * 生成测试问卷信息
     */
    JSONResult GenerateQuestionnaire() throws Exception;

    /**
     * @return 存了 user 信息的 map
     */
    Map<String, Object> GetOnlineUser();
}
