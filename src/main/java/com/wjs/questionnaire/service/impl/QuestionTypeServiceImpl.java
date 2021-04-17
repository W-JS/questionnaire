package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionTypeEntity;
import com.wjs.questionnaire.mapper.QuestionTypeMapper;
import com.wjs.questionnaire.service.IQuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTypeServiceImpl implements IQuestionTypeService {

    @Autowired
    private QuestionTypeMapper questiontypeMapper;

    /**
     * 获取所有题型信息列表
     *
     * @return 题型信息列表
     */
    @Override
    public List<QuestionTypeEntity> getAllQuestionTypeList() {
        return questiontypeMapper.findAllQuestionTypeList();
    }

    /**
     * 保存题型信息
     *
     * @param qt 题型信息
     * @return 题型是否保存成功
     */
    @Override
    public int addQuestionType(QuestionTypeEntity qt) {
        return questiontypeMapper.insertQuestionType(qt);
    }
}
