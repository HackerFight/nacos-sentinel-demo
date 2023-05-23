package com.qiuguan.cloud.sentinel.flow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author qiuguan
 * @date 2023/05/23 10:07:56  星期二
 *
 * 流控规则：线程数：
 *   并发线程数用于保护业务线程池不被慢调用耗尽。例如，当应用依赖的下游系统由于某种原因导致服务不稳定，响应时间增加。
 *   对于调用者来说，意味着吞吐量下降和更多线程数被占用，极端情况下导致线程池被耗尽。为应对太多线程被占用的情况，业内有
 *   使用线程池隔离的方案，比如不用的业务逻辑使用不同的线程池来隔离自身业务之间的资源争抢，这种隔离方案虽然隔离性比较好，
 *   但是代价就是线程数目太多，线程上下文的切换的开销比较大，特别是对低延迟的调用有较大的影响。
 *
 *   sentinel 的并发控制不负责创建和管理线程池，而是简单统计当前请求上下文的线程数(正在执行的调用数目)，如果超过阈值，则
 *   新的请求会被立刻拒绝，效果类似于信号量隔离。并发数控制通常在调用端进行配置。
 */
@RestController
public class SentinelFlowThreadController {

    /**
     * 设置线程数为1：假如有3个请求线程，只有其中1个线程返回了，另外的线程才可以被处理.
     *
     * 如何测试？
     * 我们可以开两个浏览器(chrome/firefox), 代表两个客户端(两个线程)
     * 第一个浏览器在访问的时候，立即访问第二个浏览器，可以看到第二个浏览器提示 "Blocked by Sentinel (flow limiting)"
     */
    @GetMapping("/flow/thread")
    public String threadControl() throws InterruptedException {
        //模拟线程阻塞，设置的线程数为1，如果当前线程正在处理中，其他线程是无法进来的
        TimeUnit.SECONDS.sleep(3);
        return String.format("线程控制流控.....time = %s", LocalDateTime.now());
    }
}
