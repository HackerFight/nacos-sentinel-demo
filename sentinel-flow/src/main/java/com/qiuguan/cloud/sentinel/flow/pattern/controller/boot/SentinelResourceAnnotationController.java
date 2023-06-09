package com.qiuguan.cloud.sentinel.flow.pattern.controller.boot;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

/**
 * @author qiuguan
 * @date 2023/05/21 23:17:13  星期日
 *
 * 前面我们测试 {@link HardCodeFlowController} 的时候说到，这种方式对业务代码的侵入性太强了，所以我们可以换一种方式
 * 也就是注解的方式。{@link com.alibaba.csp.sentinel.annotation.SentinelResource}
 *
 * <p>
 * 不要需要准备两个东西：
 * 1. 导入依赖：
 *    <dependency>
 *       <groupId>com.alibaba.csp</groupId>
 *       <artifactId>sentinel-annotation-aspectj</artifactId>
 *       <version>1.8.6</version>
 *       <scope>compile</scope>
 *    </dependency>
 *
 * 2.配置类中配置bean: {@link com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect}
 * </p>
 *
 * 注意：注解方式也是可以脱离spring cloud alibaba 的，和 {@link HardCodeFlowController} 一样。
 *
 *
 *
 */
//加上 @RestController 注解后，服务可以启动，但是在sentinel控制台中不显示当前应用
public class SentinelResourceAnnotationController implements InitializingBean {

    private static final String RESOURCE_NAME = "user";

    /**
     * @see SentinelResource 注解参数说明
     * value: 资源名称
     * blockHandler: 设置流控降级后的处理方法，就是{@link HardCodeFlowController} 中 BlockException 对应的内容
     *               注意：默认它声明的方法必须在同一个类中，而且必须是public的，同时返回值和源方法必须一致。
     *
     * blockHandlerClass: blockHandler必须要在同一个类中，而可以不在同一个类中，注意：虽然可以不在同一个类中，但是必须是static 方法
     *
     * fallback：当接口出现异常，就可以交给fallback降级处理
     * fallbackClass： 同上
     *
     * 如果 blockHandler 和 fallback 同时指定了，则 blockHandler 优先级更高。。。
     *
     * exceptionsToIgnore： 排除不需要处理的异常.
     *
     * 可是 {@link SentinelResource} 注解的限流规则在哪里配置嗯？比如QPS = 1
     *
     * 参考：https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
     *
     */
    @SentinelResource(value = RESOURCE_NAME, blockHandler = "getBlockedUser")
    @GetMapping("/user")
    public User getUser(){
        return new User("秋官", 30);
    }


    /**
     * 可以看下 {@link BlockException} 异常的实现，它有限流，降级，认证等等规则。
     */
    public User getBlockedUser(BlockException ex) {
        ex.printStackTrace();
        return new User("流控对象", 0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //流控规则
        initFlowRule();
        //降级规则
        initDegradeRule();
    }


    public void initFlowRule(){
        //流控规则
        FlowRule flowRule = new FlowRule();
        flowRule.setRefResource(RESOURCE_NAME);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护资源的阈值，qps = 1, 超过了这个阈值就要被流控
        flowRule.setCount(1);

        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }

    public void initDegradeRule(){
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(RESOURCE_NAME);
        //设置熔断规则：异常数
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        //触发熔断异常数
        degradeRule.setCount(2);
        //触发熔断最小请求数
        degradeRule.setMinRequestAmount(2);
        //统计时长：1分钟，默认是1s
        degradeRule.setStatIntervalMs(1000 * 60);
        //熔断持续时长，熔断发生后，10s内调用降级的方法，不在调用源方法，10s后再次调用源方法，如果10s后第一次就失败了，直接熔断，继续调用降级方法
        //10s后：半开状态
        degradeRule.setTimeWindow(10);


        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
    }

    @Getter
    @Setter
    private static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
