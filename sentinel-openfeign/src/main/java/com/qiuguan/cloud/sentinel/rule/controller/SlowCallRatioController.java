package com.qiuguan.cloud.sentinel.rule.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author qiuguan
 * @date 2023/06/10 00:02:07  星期六
 *
 * 慢调用比例
 */
@Slf4j
@RestController
public class SlowCallRatioController {

    @GetMapping("/slow")
    public String slowCall() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        log.info("慢调用正常访问");
        return "慢调用正常访问";
    }
}
