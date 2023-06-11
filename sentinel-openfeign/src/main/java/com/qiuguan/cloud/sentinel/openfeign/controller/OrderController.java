package com.qiuguan.cloud.sentinel.openfeign.controller;

import com.qiuguan.cloud.sentinel.openfeign.client.OrderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/11 14:55:38  星期日
 */
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/create")
    public String create(){
        log.info("下单成功.....");
        String reduce = this.orderFeignClient.stockReduce();
        log.info(reduce);
        return "feign call success :" +reduce;
    }
}
