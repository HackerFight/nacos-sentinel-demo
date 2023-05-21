package com.qiuguan.cloud.sentinel.flow.controller.boot;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class SentinelResourceAnnotationController {

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
     * exceptionsToIgnore： 排除不需要处理的异常
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
