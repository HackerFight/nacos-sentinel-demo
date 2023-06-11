package com.qiuguan.cloud.sentinel.openfeign.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiuguan
 * @date 2023/06/11 16:02:28  星期日
 *
 *  在启动类的注解@EnableFeignClients上指定
 *  局部生效就在@FeignClient中指定,不能加@Configuration注解
 */
public class OpenFeignConfig {

    @Value("${client.auth.key:spring.application.name}")
    private String authKey;

    @Bean
    public Logger.Level feignLogLevel() {
        // 日志级别为BASIC
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("authKey", authKey);
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor(){
        return new FeignRequestInterceptor();
    }


    static final class FeignRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate requestTemplate) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            assert requestAttributes != null;
            HttpServletRequest request = requestAttributes.getRequest();
            //todo....

        }
    }

}


