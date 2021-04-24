package com.wjs.questionnaire;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.EmptyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.wjs.questionnaire.util.EncryptUtil.md5AndSha;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = QuestionnaireApplication.class)
public class MyTests {

    @Autowired
    public IUserService userService;

    @Test
    public void test01(){
        String s11 = "wjs123";
        String s111 = "wjs123";
        String s12 = "Wjs123";
        String s21 = "wanjinsheng12345";
        String s22 = "Wanjinsheng12345";
        System.out.println(md5AndSha(s11));
        System.out.println(md5AndSha(s111));
        System.out.println(md5AndSha(s12));
        System.out.println(md5AndSha(s21));
        System.out.println(md5AndSha(s22));
    }
}
