package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.QuestionnaireApplication;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = QuestionnaireApplication.class)
public class QuestionnaireMapperTests {

    @Autowired
    public QuestionnaireMapper mapper;

    // 获取所有问卷信息列表
    @Test
    public void findAllQuestionnaireList() {
        List<QuestionnaireEntity> list = mapper.findAllQuestionnaireList();
        System.out.println("count = " + list.size());
        for (QuestionnaireEntity item : list) {
            System.out.println(item);
        }
    }

}
