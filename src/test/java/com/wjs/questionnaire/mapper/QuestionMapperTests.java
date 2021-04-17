package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.QuestionnaireApplication;
import com.wjs.questionnaire.entity.QuestionEntity;
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
public class QuestionMapperTests {

    @Autowired
    public QuestionMapper mapper;

    // 获取所有问卷信息列表
    @Test
    public void findAllQuestionList() {
        List<QuestionEntity> list = mapper.findAllQuestionList();
        System.out.println("count = " + list.size());
        for (QuestionEntity item : list) {
            System.out.println(item);
        }
    }

}
