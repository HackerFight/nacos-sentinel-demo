package com.qiuguan.cloud.sentinel.flow.effect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/05 17:27:08  星期一
 */
@RestController
public class WarmUpController {

    @GetMapping("/warmup")
    public String warmUp(){
        return "正常访问";
    }
}
