package com.qiuguan.cloud.sentinel.flow.effect.controller;

/**
 * @author qiuguan
 * @date 2023/06/06 17:59:27  星期二
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 排队等待
 */
@RestController
@Slf4j
public class QueueUpController {

    @GetMapping("/queue")
    public String queueUp(){
        return "排队等待正常访问";
    }
}
