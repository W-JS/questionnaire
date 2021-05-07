package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.service.IOptionService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;
import com.wjs.questionnaire.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineOID;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQID;

@Service
public class OptionServiceImpl implements IOptionService {

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置当前问题所有选项信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setOptionListPageByQId(PageUtil page) {
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        page.setRows(optionMapper.findOptionRowsByQId(qId));
        page.setPath("/option/Option");
        return page;
    }

    /**
     * 获取当前问题所有选项信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 选项信息列表
     */
    @Override
    public List<Map<String, Object>> getOptionListByQId(int offset, int limit) {
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        List<Map<String, Object>> list = new ArrayList<>();

        // 根据 qId 查询当前问题的所有选项
        List<OptionEntity> optionList = optionMapper.findOptionPageByQId(qId, offset, limit);

        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 设置所有问卷的所有问题的所有选项信息列表分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setAllOptionListPage(PageUtil page) {
        page.setRows(optionMapper.findAllOptionRows());
        page.setPath("/option/AllOption");
        return page;
    }

    /**
     * 获取所有问卷的所有问题的所有选项信息列表
     *
     * @param offset 获取当前页的起始行
     * @param limit  显示记录条数
     * @return 选项信息列表
     */
    @Override
    public List<Map<String, Object>> getAllOptionList(int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<OptionEntity> optionList = optionMapper.findAllOptionPage(offset, limit);

        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
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
    public PageUtil setLikeOptionListPage(PageUtil page, String searchWay, String searchContent) {
        page.setRows(optionMapper.findLikeOptionRowsByoContent(searchContent));
        page.setPath("/option/search" + "?searchWay=" + searchWay + "&searchContent=" + searchContent);
        return page;
    }

    /**
     * 获取模糊查询 选项内容 的选项信息列表
     *
     * @param offset        从第几条数据查询
     * @param limit         需要查询的记录条数
     * @param searchWay     搜索方式
     * @param searchContent 搜索内容
     * @return 选项信息列表
     */
    @Override
    public List<Map<String, Object>> getLikeOptionList(int offset, int limit, String searchWay, String searchContent) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<OptionEntity> optionList = optionMapper.findLikeOptionPageByoContent(searchContent, offset, limit);

        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取所有选项信息列表
     *
     * @return 选项信息列表
     */
    @Override
    public List<Map<String, Object>> getAllOptionList() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<OptionEntity> optionList = optionMapper.findAllOptionList();
        if (optionList != null) {
            for (OptionEntity option : optionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("option", option);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 根据 oId 得到选项信息
     *
     * @param oId 选项编号
     * @return JSON格式数据：选项信息
     */
    @Override
    public JSONResult getOptionByOId(String oId) {
        OptionEntity option = optionMapper.findOptionByOId(oId);
        JSONResult jsonResult;
        if (option != null) {
            jsonResult = JSONResult.build(option);
        } else {
            jsonResult = JSONResult.build("当前选项信息不存在！！！");
        }
        return jsonResult;
    }

    /**
     * @param pQId 前置问题
     * @return JSON格式数据：根据前置问题得到的选项
     */
    @Override
    public JSONResult getOptionByQId(String pQId) {
        List<OptionEntity> data = optionMapper.findOptionByQId(pQId);
        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建选项！！！");
        }
        return jsonResult;
    }

    /**
     * 保存选项信息
     *
     * @param oContent    选项内容
     * @param oCreateTime 选项创建时间
     * @return 选项信息是否保存成功
     */
    @Override
    public JSONResult oetOptionSubmit(String oContent, String oCreateTime) {
        String oId = UUIDGenerator.get16UUID();
        String qId = (String) redisTemplate.opsForValue().get(OnlineQID);
        OptionEntity option = new OptionEntity(oId, oContent, qId, StringToDate(oCreateTime));
        System.out.println(option);
        int flag = optionMapper.insertOption(option);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineOID, oId);// 成功保存问题信息，将 qId 存进Redis
        } else {
            jsonResult = JSONResult.build("选项信息保存失败！！！");
        }
        return jsonResult;
    }

    /**
     * 更新选项信息
     *
     * @param oId
     * @param oContent
     * @param oCreateTime
     * @return 选项信息是否更新成功
     */
    @Override
    public JSONResult getUpdateSubmit(String oId, String oContent, String oCreateTime) {
        OptionEntity option = new OptionEntity(oId, oContent, StringToDate(oCreateTime));
        int flag = optionMapper.updateOptionByOId(option);

        JSONResult jsonResult;
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("选项信息更新失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 oId 删除选项信息
     *
     * @param oId 选项编号
     * @return 选项信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit1(String oId) {
        JSONResult jsonResult;
        int flag = optionMapper.deleteOptionByOId(oId);
        if (flag != 0) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("选项信息删除失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 oId 删除多个选项信息
     *
     * @param option JSON格式的字符串，包含多个选项编号
     * @return 选项信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit2(String option) {
        JSONArray json = JSONArray.fromObject(option);
        JSONResult jsonResult1;
        boolean flag = true;
        if (json.size() > 0) {
            for (int i = 0; i < json.size(); i++) {
                String oId = (String) json.get(i);
                String oContent = optionMapper.findOptionByOId(oId).getOptionContent();
                // 根据 oId 删除选项信息
                jsonResult1 = getDeleteSubmit1(oId);
                if (jsonResult1.getState() != 1) {
                    flag = false;
                    System.out.println("删除选项 失败：" + oContent);
                    break;
                } else {
                    System.out.println("删除选项 成功：" + oContent);
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
}
