package com.qiuguan.cloud.sentinel.openfeign.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/10 17:27:47  星期六
 *
 * 异常数
 */
@Slf4j
@RestController
public class ExceptionCountController {

    @GetMapping("/exCount")
    public String exceptionRatio(){
        //异常
        int x = 1 / 0;
        return "Exception Count";
    }
}
