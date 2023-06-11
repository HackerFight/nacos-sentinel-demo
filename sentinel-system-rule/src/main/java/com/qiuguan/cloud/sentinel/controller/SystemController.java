package com.qiuguan.cloud.sentinel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qiuguan
 * @date 2023/06/11 21:29:38  星期日
 */
@Slf4j
@RestController
public class SystemController {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/system")
    public String system(){
        String format = String.format("系统自适应保护规则, 时间：%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN)));
        log.info(format);
        return format;
    }
}
