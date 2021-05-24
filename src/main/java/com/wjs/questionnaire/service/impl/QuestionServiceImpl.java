package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionTypeEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.mapper.QuestionMapper;
import com.wjs.questionnaire.mapper.QuestionTypeMapper;
import com.wjs.questionnaire.service.IQuestionService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import com.wjs.questionnaire.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.*;

@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private QuestionTypeMapper questionTypeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置当前问卷所有问题信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setQuestionListPageByQNId(PageUtil page) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        page.setRows(questionMapper.findQuestionRowsByQnId(qnId));
        page.setPath("/question/Question");
        return page;
    }

    /**
     * 获取当前问卷所有问题信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 问题信息列表
     */
    @Override
    public List<Map<String, Object>> getQuestionListByQNId(int offset, int limit) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        List<Map<String, Object>> list = new ArrayList<>();

        // 根据 qnId 查询当前问卷的所有问题
        List<QuestionEntity> questionList = questionMapper.findQuestionPageByQnId(qnId, offset, limit);

        if (questionList != null) {
            for (QuestionEntity question : questionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("question", question);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 设置所有问卷的所有问题信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setAllQuestionListPage(PageUtil page) {
        page.setRows(questionMapper.findAllQuestionRows());
        page.setPath("/question/AllQuestion");
        return page;
    }

    /**
     * 获取所有问卷的所有问题信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 问题信息列表
     */
    @Override
    public List<Map<String, Object>> getAllQuestionList(int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<QuestionEntity> questionList = questionMapper.findAllQuestionPage(offset, limit);

        if (questionList != null) {
            for (QuestionEntity question : questionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("question", question);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 设置分页参数
     *
     * @param page          分页对象参数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 分页结果
     */
    @Override
    public PageUtil setLikeQuestionListPage(PageUtil page, String searchWay, String searchContent) {
        page.setRows(questionMapper.findLikeQuestionRowsByQTitle(searchContent));
        page.setPath("/question/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent);
        return page;
    }

    /**
     * 获取模糊查询 问题标题 的问题信息列表
     *
     * @param offset        从第几条数据查询
     * @param limit         需要查询的记录条数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 问题信息列表
     */
    @Override
    public List<Map<String, Object>> getLikeQuestionList(int offset, int limit, String searchWay, String searchContent) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<QuestionEntity> questionList = questionMapper.findLikeQuestionPageByQTitle(searchContent, offset, limit);

        if (questionList != null) {
            for (QuestionEntity question : questionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("question", question);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷的所有问题
     */
    @Override
    public JSONResult getAllQuestionByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        List<QuestionEntity> data = questionMapper.findAllQuestionByQnId(qnId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题的行数
     */
    @Override
    public JSONResult getNoPrependedQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionMapper.findNoPrependedQuestionRowsByQnId(qnId));

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题
     */
    @Override
    public JSONResult getNoPrependedQuestionPage1ByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        List<Map<String, Object>> data = new ArrayList<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionMapper.findNoPrependedQuestionRowsByQnId(qnId));

        // 未被前置的问题
        List<QuestionEntity> questionList = questionMapper.findNoPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());
        JSONResult jsonResult;
        if (questionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionList", questionList);
            data.add(map);
        }
        jsonResult = JSONResult.build(data);
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     *
     * @param qId     问题编号
     * @param current 当前页码
     * @return JSON格式数据：根据 qnId 查询当前问卷未被前置的问题和连续后置问题
     */
    @Override
    public JSONResult getNoPrependedQuestionPage2ByQnId(String qId, String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        List<Map<String, Object>> data = new ArrayList<>();

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionMapper.findNoPrependedQuestionRowsByQnId(qnId));

        // 未被前置的问题
        List<QuestionEntity> questionList = questionMapper.findNoPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        // 当前问题的传递后置问题
        List<QuestionEntity> rearQuestionList = new ArrayList<>();
        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        boolean flag = false;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionMapper.findRearQuestionByQId(qId);
            if (data2 != null) {
                // 需要所有的后置问题
                rearQuestionList.add(data2);
                data1 = data2;
                flag = true;
            } else {
                if (!flag) {
                    data1 = null;
                }
                flag = false;
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (questionList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionList", questionList);
            data.add(map);
        }
        if (data1 != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("rearQuestionList", rearQuestionList);
            data.add(map);
        }
        jsonResult = JSONResult.build(data);
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题的行数
     */
    @Override
    public JSONResult getPrependedQuestionRowsByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil data = new PageUtil();
        data.setRows(questionMapper.findPrependedQuestionRowsByQnId(qnId));

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @return JSON格式数据：根据 qnId 查询当前问卷被前置的问题
     */
    @Override
    public JSONResult getPrependedQuestionPageByQnId(String current) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);

        PageUtil pageUtil = new PageUtil();
        pageUtil.setCurrent(Integer.valueOf(current));
        pageUtil.setRows(questionMapper.findPrependedQuestionRowsByQnId(qnId));

        List<QuestionEntity> data = questionMapper.findPrependedQuestionPageByQnId(qnId, pageUtil.getOffset(), pageUtil.getLimit());

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建问题！！！");
        }
        return jsonResult;
    }

    /**
     * 一个指定的问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题信息
     */
    @Override
    public QuestionEntity getQuestion(String qId) {
        QuestionEntity question = questionMapper.findQuestionByQId(qId);
        if (question != null) {
            return question;
        } else {
            return new QuestionEntity();
        }
    }

    /**
     * 一个指定的问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题信息
     */
    @Override
    public JSONResult getQuestionByQId(String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionMapper.findQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            data.add(map);
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * 一个指定的问题和前置问题信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题和前置问题信息
     */
    @Override
    public JSONResult getQuestionAndPreQuestionByQId(String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionMapper.findQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("question", question);
            data.add(map1);

            if (question.getPreQuestionId() != null && !"".equals(question.getPreQuestionId())) {
                QuestionEntity preQuestion = questionMapper.findQuestionByQId(question.getPreQuestionId());
                if (preQuestion != null) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("preQuestion", preQuestion);
                    data.add(map2);
                }
                jsonResult = JSONResult.build(data);
            } else {
                jsonResult = JSONResult.build(data);
            }
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * 一个指定的问题、问题类型、前置问题和前置选项信息
     *
     * @param qId 问题编号
     * @return 一个指定的问题、问题类型、前置问题和前置选项信息
     */
    @Override
    public JSONResult getQuestionAndPreQuestionAndPreOptionByQId(String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionEntity question = questionMapper.findQuestionByQId(qId);

        JSONResult jsonResult;
        if (question != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            data.add(map);

            if (question.getQuestiontypeId() != null && !"".equals(question.getQuestiontypeId())) {
                List<QuestionTypeEntity> list = questionTypeMapper.findAllQuestionTypeList();
                for (QuestionTypeEntity questionType : list) {
                    if (questionType.getQuestiontypeId().equals(question.getQuestiontypeId())) {
                        map = new HashMap<>();
                        map.put("questionType", questionType);
                        data.add(map);
                        break;
                    }
                }
            }
            if (question.getPreQuestionId() != null && !"".equals(question.getPreQuestionId())) {
                QuestionEntity preQuestion = questionMapper.findQuestionByQId(question.getPreQuestionId());
                if (preQuestion != null) {
                    map = new HashMap<>();
                    map.put("preQuestion", preQuestion);
                    data.add(map);
                }
            }
            if (question.getPreOptionId() != null && !"".equals(question.getPreOptionId())) {
                OptionEntity preOption = optionMapper.findOptionByOId(question.getPreOptionId());
                if (preOption != null) {
                    map = new HashMap<>();
                    map.put("preOption", preOption);
                    data.add(map);
                }
            }
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @Override
    public JSONResult getPrependedQuestionByQId(String qId) {
        // 如果当前问题有前置问题，则找到当前问题的前置问题
        QuestionEntity data = questionMapper.findPrependedQuestionByQId(qId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("当前问题没有前置问题！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的连续前置问题
     * 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    @Override
    public JSONResult getFinallyPrependedQuestionByQId(String qId, int status) {
        List<Map<String, Object>> data = new ArrayList<>();

        // 如果当前问题有前置问题，则找到当前问题的前置问题
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的前置问题也有前置问题，则找到当前问题的前置问题的前置问题（循环），最终找到的前置问题是没有前置问题的问题
        QuestionEntity data2;

        Map<String, Object> map;
        boolean flag = true;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionMapper.findPrependedQuestionByQId(qId);
            if (data2 != null) {
                if (status == 1) {
                    // 需要所有的前置问题
                    map = new HashMap<>();
                    map.put("question", data2);
                    data.add(map);
                }
                data1 = data2;
            } else {
                if (flag) {
                    if (status == 0) {
                        // 只需要最后一个的前置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                }
                flag = false;
            }
        } while (flag);// 当前问题无前置问题

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(OBJECT_EXISTENCE, "当前问题的连续前置问题", data);
        } else {
            jsonResult = JSONResult.build("当前问题没有连续前置问题！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    @Override
    public JSONResult getRearQuestionByQId(String qId) {
        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data = questionMapper.findRearQuestionByQId(qId);

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("当前问题没有后置问题！！！");
        }
        return jsonResult;
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的连续后置问题
     * 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
     *
     * @param qId    当前问题编号
     * @param status 1: 连续后置问题 0: 最后一个后置问题
     * @return 问题信息
     */
    @Override
    public JSONResult getFinallyRearQuestionByQId(String qId, int status) {
        List<Map<String, Object>> data = new ArrayList<>();

        // 如果当前问题是被前置问题，则找到当前问题的后置问题
        QuestionEntity data1 = new QuestionEntity();
        // 如果当前问题的后置问题也是被前置问题，则找到当前问题的后置问题的后置问题（循环），最终找到的后置问题是未被前置的问题
        QuestionEntity data2;

        Map<String, Object> map;
        boolean flag = true;
        do {
            if (data1.getQuestionId() != null) {
                qId = data1.getQuestionId();
            }
            data2 = questionMapper.findRearQuestionByQId(qId);
            if (data2 != null) {
                if (status == 1) {
                    // 需要所有的后置问题
                    map = new HashMap<>();
                    map.put("question", data2);
                    data.add(map);
                }
                data1 = data2;
            } else {
                if (flag) {
                    if (status == 0) {
                        // 只需要最后一个的后置问题
                        map = new HashMap<>();
                        map.put("question", data1);
                        data.add(map);
                    }
                }
                flag = false;
            }
        } while (flag);// 当前问题无后置问题

        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(OBJECT_EXISTENCE, "当前问题的连续后置问题", data);
        } else {
            jsonResult = JSONResult.build("当前问题没有连续后置问题！！！");
        }
        return jsonResult;
    }

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
    @Override
    public JSONResult getQuestionSubmit(String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime) {
        String qId = UUIDGenerator.get16UUID();
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        QuestionEntity question;
        int flag;
        if ("null".equals(pQId) || "null".equals(pOId)) {
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), qnId, qtId, StringToDate(qCreateTime));
            flag = questionMapper.insertQuestionS(question);
//            flag = questionService.addQuestion(question);
            System.out.println("No: pQId, pOId  " + question);
        } else {
            Date date = questionMapper.findQuestionByQId(pQId).getQuestionCreateTime();
            date.setTime(date.getTime() + 1000);
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, date);
//            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, StringToDate(qCreateTime));
            flag = questionMapper.insertQuestionS(question);
//            flag = questionService.addQuestions(question);
            System.out.println("Have: pQId, pOId  " + question);
        }

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineQTID, qtId);// 成功保存问题类型信息，将 qtId 存进Redis
            redisTemplate.opsForValue().set(OnlineQID, qId);// 成功保存问题信息，将 qId 存进Redis
        } else {
            jsonResult = JSONResult.build("问题信息保存失败！！！");
        }
        return jsonResult;
    }

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
    @Override
    public JSONResult getUpdateSubmit(String qId, String qTitle, String qDescription, String qStatus, String pQId, String pOId, String qtId, String qCreateTime) {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        QuestionEntity question;
        int flag = 1;
        if ("not".equals(pQId) || "not".equals(pOId)) {// 前置问题和前置选项不做操作
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), qnId, qtId, StringToDate(qCreateTime));
            flag = questionMapper.updateQuestionByQId(question);
//            flag = questionService.modifyQuestion(question);
            System.out.println("NoSet: pQId, pOId  " + question);
        } else if ("null".equals(pQId) || "null".equals(pOId)) {// 将前置问题和前置选项置为空
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), null, null, qnId, qtId, StringToDate(qCreateTime));
//            flag = questionService.modifyQuestionByQId(question);
            flag = questionMapper.updateQuestions(question);
            System.out.println("SetNULL: pQId, pOId  " + question);
        } else {
            Date date = questionMapper.findQuestionByQId(pQId).getQuestionCreateTime();
            date.setTime(date.getTime() + 1000);
            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, date);
//            question = new QuestionEntity(qId, qTitle, qDescription, Integer.valueOf(qStatus), pQId, pOId, qnId, qtId, StringToDate(qCreateTime));
            flag = questionMapper.updateQuestionByQId(question);
//            flag = questionService.modifyQuestions(question);
            System.out.println("Set: pQId, pOId  " + question);

            // 循环更新当前问题的连续后置问题的时间
            boolean rearFlag = true;
            do {
                question = questionMapper.findRearQuestionByQId(question.getQuestionId());
                if (question != null) {
                    date.setTime(date.getTime() + 1000);
                    question.setQuestionCreateTime(date);
                    questionMapper.updateQuestionByQId(question);
                } else {
                    rearFlag = false;
                }
            } while (rearFlag);// 当前问题无后置问题
        }

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("问题信息更新失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qId 删除问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit1(String qId) {
        JSONResult jsonResult;
        // 1、根据 qId 得到当前问题的选项列表
        List<OptionEntity> optionList = optionMapper.findOptionByQId(qId);

        if (optionList != null) {
            // 2、循环删除选项
            boolean flag = true;
            for (OptionEntity option : optionList) {
                if (optionMapper.deleteOptionByOId(option.getOptionId()) == 0) {
                    System.out.println("删除选项 失败：" + option.getOptionContent());
                    flag = false;
                    break;
                } else {
                    System.out.println("删除选项 成功：" + option.getOptionContent());
                }
            }
            if (flag) {
                //3、删除问题
                if (questionMapper.deleteQuestionByQId(qId) != 0) {
                    jsonResult = JSONResult.build();
                } else {
                    jsonResult = JSONResult.build("问题信息删除失败！！！");
                }
            } else {
                jsonResult = JSONResult.build("当前问题的选项信息删除失败！！！");
            }

        } else {
            jsonResult = JSONResult.build("当前问题的选项信息不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qId 删除问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     *
     * @param qId 问题编号
     * @return 问题信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit2(String qId) {
        JSONResult jsonResult1;
        boolean flag1 = true;
        // 1、如果当前问题有前置问题，则找到当前问题的连续前置问题
        jsonResult1 = getFinallyPrependedQuestionByQId(qId, 1);
        if (jsonResult1.getState() == 1) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) jsonResult1.getData();
            if (data != null) {
                Collections.reverse(data);// List数据反转
                JSONResult jsonResult11;
                for (Map<String, Object> map : data) {
                    QuestionEntity question = (QuestionEntity) map.get("question");
                    if (question != null) {
                        // 根据 qId 删除问题信息及关联的选项信息
                        jsonResult11 = getDeleteSubmit1(question.getQuestionId());
                        if (jsonResult11.getState() != 1) {
                            flag1 = false;
                            System.out.println("删除前置问题 失败：" + question.getQuestionTitle());
                            break;
                        } else {
                            System.out.println("删除前置问题 成功：" + question.getQuestionTitle());
                        }
                    }

                }
            }
        }

        boolean flag2 = true;
        JSONResult jsonResult2;
        // 2、如果当前问题是被前置问题，则找到当前问题的连续后置问题
        jsonResult2 = getFinallyRearQuestionByQId(qId, 1);
        if (jsonResult2.getState() == 1) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) jsonResult2.getData();
            if (data != null) {
                JSONResult jsonResult21;
                for (Map<String, Object> map : data) {
                    QuestionEntity question = (QuestionEntity) map.get("question");
                    if (question != null) {
                        // 根据 qId 删除问题信息及关联的选项信息
                        jsonResult21 = getDeleteSubmit1(question.getQuestionId());
                        if (jsonResult21.getState() != 1) {
                            flag2 = false;
                            System.out.println("删除后置问题 失败：" + question.getQuestionTitle());
                            break;
                        } else {
                            System.out.println("删除后置问题 成功：" + question.getQuestionTitle());
                        }
                    }
                }
            }
        }
        JSONResult jsonResult3;
        if (flag1 && flag2) {
            String qTitle = questionMapper.findQuestionByQId(qId).getQuestionTitle();
            // 根据 qId 删除问题信息及关联的选项信息
            jsonResult3 = getDeleteSubmit1(qId);
            if (jsonResult3.getState() == 1) {
                System.out.println("删除问题 成功：" + qTitle);
                jsonResult3 = JSONResult.build();
            } else {
                System.out.println("删除问题 失败：" + qTitle);
                jsonResult3 = JSONResult.build("问题信息删除失败！！！");
            }
        } else if (!flag1) {
            jsonResult3 = JSONResult.build("连续前置问题信息删除失败！！！");
        } else if (!flag2) {
            jsonResult3 = JSONResult.build("连续后置问题信息删除失败！！！");
        } else {
            jsonResult3 = JSONResult.build("删除失败！！！");
        }
        return jsonResult3;
    }

    /**
     * 根据 qId 删除多个问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
     *
     * @param question JSON格式的字符串，包含多个问题编号
     * @return 问题信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit3(String question) {
        JSONArray json = JSONArray.fromObject(question);
        JSONResult jsonResult1;
        boolean flag = true;
        if (json.size() > 0) {
            for (int i = 0; i < json.size(); i++) {
                String qId = (String) json.get(i);
                String qTitle = questionMapper.findQuestionByQId(qId).getQuestionTitle();
                // 根据 qId 删除问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
                jsonResult1 = getDeleteSubmit2(qId);
                if (jsonResult1.getState() != 1) {
                    flag = false;
                    System.out.println("删除问题 失败：" + qTitle);
                    break;
                } else {
                    System.out.println("删除问题 成功：" + qTitle);
                }

            }
        }
        JSONResult jsonResult2;
        if (flag) {
            jsonResult2 = JSONResult.build();
        } else {
            jsonResult2 = JSONResult.build("问题信息删除失败！！！");
        }
        return jsonResult2;
    }
}
