package org.dromara.hmily.demo.springcloud.order.configuration;

import org.dromara.hmily.spring.HmilyApplicationContextAware;
import org.dromara.hmily.spring.aop.SpringHmilyTransactionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
