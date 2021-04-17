package com.wjs.questionnaire.service;

import com.wjs.questionnaire.QuestionnaireApplication;
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
public class UserServiceTests {

    @Autowired
    public IUserService userService;

    // 获取所有用户信息列表
    @Test
    public void getAllUserList(){
        List<UserEntity> list = userService.getAllUserList();
        System.out.println("count = " + list.size());
        for (UserEntity item : list) {
            System.out.println(item);
        }
    }

    // 根据 userId 查找 User
    @Test
    public void getUserByUserId(){
        UserEntity userByUserId = userService.getUserByUserId("852189a7ef48c31e");
        System.out.println(userByUserId.toString());
    }

    // 根据 userName 查找 User
    @Test
    public void getUserByUserName(){
        UserEntity userByUserName = userService.getUserByUserName("追忆似水年华01");
        System.out.println(userByUserName.toString());
    }

    // 根据 userPhone 查找 User
    @Test
    public void getUserByUserPhone(){
        UserEntity userByUserPhone = userService.getUserByUserPhone("18972503451");
        System.out.println(userByUserPhone.toString());
    }

    // 根据 userEmail 查找 User
    @Test
    public void getUserByUserEmail(){
        UserEntity userByUserEmail = userService.getUserByUserEmail("wanjinshengwjs01@163.com");
        System.out.println(userByUserEmail.toString());
    }

}
