package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.mapper.QuestionMapper;
import com.wjs.questionnaire.mapper.QuestionnaireMapper;
import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import com.wjs.questionnaire.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQNID;

@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private QuestionServiceImpl questionService;

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    @Override
    public List<Map<String, Object>> getAllQuestionnaireList() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findAllQuestionnaireList();
        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 设置分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setQuestionnaireListPage(PageUtil page) {
        page.setRows(questionnaireMapper.findAllQuestionnaireRows());
        page.setPath("/questionnaire/index");
        return page;
    }

    /**
     * 获取所有问卷信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问卷信息列表
     */
    @Override
    public List<Map<String, Object>> getQuestionnaireList(int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findAllQuestionnairePage(offset, limit);

        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
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
    public PageUtil setLikeQuestionnaireListPage(PageUtil page, String searchWay, String searchContent) {
        page.setRows(questionnaireMapper.findLikeQuestionnaireRowsByQnTitle(searchContent));
        page.setPath("/questionnaire/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent);
        return page;
    }

    /**
     * 获取模糊查询 问卷标题 的问卷信息列表
     *
     * @param offset        从第几条数据查询
     * @param limit         需要查询的记录条数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 问卷信息列表
     */
    @Override
    public List<Map<String, Object>> getLikeQuestionnaireList(int offset, int limit, String searchWay, String searchContent) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findLikeQuestionnairePageByQnTitle(searchContent, offset, limit);

        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 根据 qnId 得到问卷信息
     *
     * @param qnId 问卷编号
     * @return JSON格式数据：问卷信息
     */
    @Override
    public JSONResult getQuestionnaireByQnId(String qnId) {
        QuestionnaireEntity questionnaire = questionnaireMapper.findQuestionnaireByQnId(qnId);
        JSONResult jsonResult;
        if (questionnaire != null) {
            jsonResult = JSONResult.build(questionnaire);
        } else {
            jsonResult = JSONResult.build("当前问卷信息不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 和 qId 得到 在线问卷信息 和 在线问题信息
     *
     * @param qnId 在线问卷编号
     * @param qId  在线问题编号
     * @return 问卷信息和问题信息
     */
    @Override
    public JSONResult getQuestionnaireAndQuestionByQnIdAndQId(String qnId, String qId) {
        List<Map<String, Object>> data = new ArrayList<>();
        QuestionnaireEntity questionnaire = questionnaireMapper.findQuestionnaireByQnId(qnId);
        QuestionEntity question = questionMapper.findQuestionByQId(qId);

        JSONResult jsonResult;
        boolean flag = true;
        if (questionnaire != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("questionnaire", questionnaire);
            data.add(map);
        } else {
            flag = false;
        }
        if (question != null && flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            data.add(map);
        } else {
            flag = false;
        }
        if (flag) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未选择问卷和问题！！！");
        }
        return jsonResult;
    }

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
    @Override
    public JSONResult getQuestionnaireSubmit(String qnTitle, String qnFuTitle, String qnDescription, int qnStatus, String qnCreateTime, String userId) {
        String qnId = UUIDGenerator.get16UUID();
        QuestionnaireEntity questionnaire = new QuestionnaireEntity(qnId, qnTitle, qnFuTitle, qnDescription, qnStatus, StringToDate(qnCreateTime), userId);
        int flag = questionnaireMapper.insertQuestionnaire(questionnaire);
        JSONResult jsonResult;
        if (flag == 1) {
            System.out.println(questionnaire);
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineQNID, qnId);// 成功保存问卷信息，将 qnId 存进Redis
        } else {
            jsonResult = JSONResult.build("问卷信息保存失败！！！");
        }
        return jsonResult;
    }

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
    @Override
    public JSONResult getUpdateSubmit(String qnId, String qnTitle, String qnFuTitle, String qnDescription, String qnCreateTime) {
        QuestionnaireEntity questionnaire = new QuestionnaireEntity(qnId, qnTitle, qnFuTitle, qnDescription, StringToDate(qnCreateTime));
        int flag = questionnaireMapper.updateQuestionnaireByQNId(questionnaire);
        System.out.println("Set: qnFuTitle  " + questionnaire);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("问卷信息更新失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 删除问卷信息、问题信息及关联的选项信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit1(String qnId) {
        JSONResult jsonResult1;
        // 1、根据 qnId 得到当前问卷的问题列表
        List<QuestionEntity> questionList = questionMapper.findAllQuestionByQnId(qnId);

        if (questionList != null) {
            // 2、循环删除问题及关联的选项信息
            JSONResult jsonResult2;
            boolean flag = true;
            for (QuestionEntity question : questionList) {
                // 根据 qId 删除问题信息及关联的选项信息
                jsonResult2 = questionService.getDeleteSubmit1(question.getQuestionId());
                if (jsonResult2.getState() != 1) {
                    System.out.println("删除问题 失败：" + question.getQuestionTitle());
                    flag = false;
                    break;
                } else {
                    System.out.println("删除问题 成功：" + question.getQuestionTitle());
                }
            }
            if (flag) {
                //3、删除问卷
                if (questionnaireMapper.deleteQuestionnaireByQNId(qnId) != 0) {
                    jsonResult1 = JSONResult.build();
                } else {
                    jsonResult1 = JSONResult.build("问卷信息删除失败！！！");
                }
            } else {
                jsonResult1 = JSONResult.build("当前问卷的问题信息删除失败！！！");
            }

        } else {
            jsonResult1 = JSONResult.build("当前问卷的问题信息不存在！！！");
        }
        return jsonResult1;
    }

    /**
     * 根据 qnId 删除多个问卷信息、问题信息及关联的选项信息
     *
     * @param questionnaire JSON格式的字符串，包含多个问卷编号
     * @return 问卷信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit2(String questionnaire) {
        JSONArray json = JSONArray.fromObject(questionnaire);
        JSONResult jsonResult1;
        boolean flag = true;
        if (json.size() > 0) {
            for (int i = 0; i < json.size(); i++) {
                String qnId = (String) json.get(i);
                String qnTitle = questionnaireMapper.findQuestionnaireByQnId(qnId).getQuestionnaireTitle();
                // 根据 qId 删除问题信息及关联的选项信息和关联的前置问题信息及关联的选项信息和后置问题信息及关联的选项信息
                jsonResult1 = getDeleteSubmit1(qnId);
                if (jsonResult1.getState() != 1) {
                    flag = false;
                    System.out.println("删除问卷 失败：" + qnTitle);
                    break;
                } else {
                    System.out.println("删除问卷 成功：" + qnTitle);
                }

            }
        }
        JSONResult jsonResult2;
        if (flag) {
            jsonResult2 = JSONResult.build();
        } else {
            jsonResult2 = JSONResult.build("问卷信息删除失败！！！");
        }
        return jsonResult2;
    }

    /**
     * 生成测试问卷信息: http://localhost:8080/questionnaire/questionnaire/GenerateQuestionnaire
     */
    @Override
    public JSONResult GenerateQuestionnaire() throws Exception {
        Format f = new DecimalFormat("000");
        int code = 5;// 问卷编号
        String str1 = "我的调查问卷" + f.format(code);
        System.out.println(str1);
        String qnId = UUIDGenerator.get16UUID();
        QuestionnaireEntity questionnaire = new QuestionnaireEntity(qnId, str1, str1, str1, 0, new Date(), "93464e3143a102bc");
        questionnaireMapper.insertQuestionnaire(questionnaire);

        String str2 = str1 + "的问题";
        for (int i = 1; i <= 30; i++) {
            String str3 = str2 + f.format(i);
            String qId = UUIDGenerator.get16UUID();
            QuestionEntity question = new QuestionEntity(qId, str3, str3, 0, qnId, "score", new Date());
            questionMapper.insertQuestionS(question);
            System.out.println(str3);

            String str4 = str3 + "的选项";
            for (int j = 1; j <= 4; j++) {
                String str5 = str4 + f.format(j);
                OptionEntity option = new OptionEntity(UUIDGenerator.get16UUID(), str5, qId, new Date());
                optionMapper.insertOption(option);
                System.out.println(str5);
                TimeUnit.MILLISECONDS.sleep(500);// 延时0.5秒
            }

        }
        return JSONResult.build();
    }
}
