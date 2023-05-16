package com.qiuguan.cloud.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qiuguan
 * @date 2023/05/17 01:34:12  星期三
 */
@RestController
public class SentinelController {

    @GetMapping("/sentinel")
    public String hello(){
        return String.format("hello sentinel, time: %s", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
    }
}
