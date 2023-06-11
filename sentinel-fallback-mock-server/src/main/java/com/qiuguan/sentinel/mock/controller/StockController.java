package com.qiuguan.sentinel.mock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/10 23:14:21  星期六
 *
 * mock 服务，给 sentinel-openfeign 客户端调用的
 */
@Slf4j
@RequestMapping("/stock")
@RestController
public class StockController {


    /**
     * 扣减库存
     * @return
     */
    @GetMapping("/reduce")
    public String reduceStock(){
       log.info("扣款库存成功.....");
       return "库存扣减成功";
    }

    @GetMapping("/reduce2")
    public String reduceStock2(){
        //客户端调用这个接口时，出现异常，客户端会自己服务降级
        int x = 1 / 0;
        return "库存扣减成功";
    }
}
