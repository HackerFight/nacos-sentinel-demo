package com.qiuguan.cloud.sentinel.openfeign.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/10 17:09:39  星期六
 *
 * 异常比例
 */
@Slf4j
@RestController
public class ExceptionRatioController {

    @GetMapping("/ex")
    public String exceptionRatio(){
        //异常
        int x = 1 / 0;
        return "Exception Ratio";
    }
}
