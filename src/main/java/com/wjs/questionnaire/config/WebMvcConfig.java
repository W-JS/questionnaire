package com.wjs.questionnaire.config;

import com.wjs.questionnaire.controller.interceptor.UserExistInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserExistInterceptor userExistInterceptor;

    /**
     * addPathPatterns：需要拦截的访问路径
     * excludePathPatterns：不需要拦截的路径
     * String数组类型可以写多个用","分割
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userExistInterceptor)
                .addPathPatterns(
                        "/questionnaire/**",
                        "/questiontype/**",
                        "/question/**",
                        "/option/**"
                )
                .excludePathPatterns(
                        "/user/login"
                );
    }
}
