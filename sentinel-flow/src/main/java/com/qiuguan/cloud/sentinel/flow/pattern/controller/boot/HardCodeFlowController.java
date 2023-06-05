package com.qiuguan.cloud.sentinel.flow.pattern.controller.boot;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collections;

/**
 * @author qiuguan
 * @date 2023/05/21 22:46:20  星期日
 *
 * 可以不依赖spring cloud alibaba 进行测试，需要导入：
 * <p>
 *     <dependency>
 *       <groupId>com.alibaba.csp</groupId>
 *       <artifactId>sentinel-core</artifactId>
 *       <version>1.8.6</version>
 *       <scope>compile</scope>
 *     </dependency>
 * </p>
 *
 * 我测试了一下并没有生效【因为QPS=1,所以快速刷新访问路径，就可以看到""流控规则生效!!!""，但是实际上没有】
 * 我想还是因为我这里已经整合了spring cloud, 如果想测试硬编码，可以直接使用springboot + sentinel-core 进行测试
 *
 * 这种硬编码的方式对业务代码切入性太强了，需要 SphU.entry(RESOURCE_NAME) 去管控资源。。
 *
 * @see SentinelResourceAnnotationController
 */
@Slf4j
//加上 @RestController 注解后，服务可以启动，但是在sentinel控制台中不显示当前应用
//@RestController
public class HardCodeFlowController implements InitializingBean {

    //资源名一般就是请求的路径名
    private static final String RESOURCE_NAME = "hello";


    @SentinelResource
    @GetMapping("/hello")
    public String hello(){
        try(Entry entry = SphU.entry(RESOURCE_NAME)) {
            log.info("welcome to beijing.....");
            return "hello beijing";
        } catch (BlockException e) {
            log.error("resource blocked..........");
            return "流控规则生效!!!";
        } catch (Exception e) {
            //如果需要配置降级规则，可以通过这种方式记录业务异常
            //Tracer.traceEntry(e, entry);
        }

        return ".....";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //流控规则
        FlowRule flowRule = new FlowRule();
        flowRule.setRefResource(RESOURCE_NAME);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护资源的阈值，qps = 1, 超过了这个阈值就要被流控
        flowRule.setCount(1);

        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }
}
