package com.qiuguan.cloud.sentinel.openfeign.ex;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author qiuguan
 * @date 2023/05/23 22:18:50  星期二
 *
 * {@link BlockException} 异常的统一处理。之前我们都是通过 {@link com.alibaba.csp.sentinel.annotation.SentinelResource}
 * 注解的 【blockHandler】 属性来自定义流控返回结果，现在我们统一在这个类中实现。当然了，如果每个资源的流控逻辑都不一样，那么
 * 此时使用统一的方式就不好用了，此时还是用注解好一点。
 *
 * springmvc接口资源限流入口在 {@link org.springframework.web.servlet.HandlerInterceptor} 实现类
 * {@link com.alibaba.csp.sentinel.adapter.spring.webmvc.AbstractSentinelInterceptor} 的 【 preHandle() 】方法中
 * 对异常的处理类是 {@link BlockExceptionHandler} 实现类
 *
 * sentinel1.7 引入了 sentinel-spring-webmvc-adapter.jar
 */
@Slf4j
@Component
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        //e.getRule(): 资源，规则的详细信息
        log.info("自定义BlockExceptionHandler处理BlockException异常，rule = {}", e.getRule());

        R<?> r = null;
        if (e instanceof FlowException) {
            r = R.error(100, "接口限流了....");
        } else if (e instanceof DegradeException) {
            r = R.error(101, "服务降级了....");
        } else if (e instanceof ParamFlowException) {
            r = R.error(102, "热点参数被限流了....");
        } else if (e instanceof SystemBlockException) {
            r = R.error(103, "触发系统保护规则了....");
        } else if (e instanceof AuthorityException) {
            r = R.error(104, "授权规则不通过....");
        }

        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), r);
    }
}
