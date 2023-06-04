package com.qiuguan.cloud.sentinel.flow.controller.direct;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qiuguan
 * @date 2023/05/17 01:34:12  星期三
 *
 * 注意：一旦服务重启，限流，降级配置啥的就都没有了，后面可以配置持久化
 *
 * 流控规则：QPS: 每秒请求数
 *
 */
@RestController
public class SentinelFlowQPSController {

    /**
     * 1.请看 "1-资源.png"  --sentinel 控制台上的 "簇点链路" tab页上选择，也就是资源
     * 2.请看 "2-配置流控规则.png"
     * 3.请看 "3-默认的限流.png"
     */
    @GetMapping("/helloSentinel")
    public String hello(){
        return String.format("hello sentinel, time: %s", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
    }


    /**
     *
     * 对"flow/{id}" 资源配置的QPS=2, 快速刷新时提示的sentinel默认的blockerhandler信息
     * 但是我们可以通过 {@link SentinelResource} 注解自定义blockerhandler
     *
     * 还有这里的资源名不一定非要是 "flow/{id}", 可以是随便的，但是在sentinel控制台上的时候
     * 要先定位到请求路径 /flow/{id}, 然后找到其下面的资源，比如我将注解的value改成 flow2
     * 然后选择flow2,配置流控规则
     * @param id
     * @return
     */
    @SentinelResource(value = "flow/{id}", blockHandler = "flowByBlockHandler")
    @GetMapping("/flow/{id}")
    public String flow(@PathVariable Integer id){
        return String.format("sentinel flow test and custom define blockhandler, id = %s", id);
    }

    /**
     * 这个 blockHandler 对应的方法参数中，一定要加上 {@link BlockException} 异常
     */
    @SuppressWarnings("all")
    public String flowByBlockHandler(Integer id, BlockException ex){
        return String.format("自定义blockhander.....id = %s, ex msg: %s", id, ex.getMessage());
    }


    /**
     * 配置QPS = 1, 快速刷新浏览器，查看页面的显示信息，是否是 {@link com.qiuguan.cloud.sentinel.flow.ex.SentinelBlockExceptionHandler}
     * 里面的内容。
     *
     */
    @GetMapping("/flow/global")
    public String globalBlockHandler(){
        return String.format("请注意查看BlockException异常的统一处理，time:%s", LocalDateTime.now());
    }

}
