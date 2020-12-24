package org.dromara.hmily.demo.springcloud.inventory.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.dromara.hmily.spring.HmilyApplicationContextAware;
import org.dromara.hmily.spring.aop.SpringHmilyTransactionAspect;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class HmilyConfig implements WebMvcConfigurer {


    @Bean
    public HmilyApplicationContextAware hmilyApplicationContextAware(){
        return new HmilyApplicationContextAware();
    }

    @Bean(name = "hmilyTransactionAspect")
    public SpringHmilyTransactionAspect springHmilyTransactionAspect(){
        return new SpringHmilyTransactionAspect();
    }
}
