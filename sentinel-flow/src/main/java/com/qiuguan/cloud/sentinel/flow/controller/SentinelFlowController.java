package com.qiuguan.cloud.sentinel.flow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qiuguan
 * @date 2023/05/17 01:34:12  星期三
 */
@RestController
public class SentinelFlowController {

    /**
     * 1.请看 "1-资源.png"  --sentinel 控制台上的 "簇点链路" tab页上选择，也就是资源
     * 2.请看 "2-配置流控规则.png"
     * 3.请看 "3-默认的限流.png"
     * @return
     */
    @GetMapping("/helloSentinel")
    public String hello(){
        return String.format("hello sentinel, time: %s", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
    }
}
