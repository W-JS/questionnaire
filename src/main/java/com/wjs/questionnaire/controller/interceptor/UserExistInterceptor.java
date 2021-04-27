package com.wjs.questionnaire.controller.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.wjs.questionnaire.util.QuestionnaireConstant.ONLINEUSERID;

/**
 * 进行 处理请求时 使用 拦截器 判断用户是否登录
 */
@Component
public class UserExistInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    // 处理请求之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        System.out.println("preHandle: " + handler.toString());
        if (!"null".equals(OnlineUserID)) {
            return true;
        } else {
            response.sendRedirect(request.getContextPath()+ "/user/login");
            return false;
        }
    }

    // 处理请求之后，渲染视图之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle: " + handler.toString());
    }

    // 渲染视图之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion: " + handler.toString() + "\n");
    }
}
