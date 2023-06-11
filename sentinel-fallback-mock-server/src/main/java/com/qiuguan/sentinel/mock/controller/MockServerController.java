package com.qiuguan.sentinel.mock.controller;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/10 23:14:21  星期六
 *
 * mock 服务，给 sentinel-openfeign 客户端调用的
 */
@RequestMapping("/order")
@RestController
public class MockServerController {


    @GetMapping("/getOrder")
    public String getOrder(){
        return JSON.toJSONString(Order.builder()
                .orderId("1001")
                .orderType("A")
                .price("2500.00")
                .buyer("秋官")
                .build());
    }

    @Builder
    static class Order {

        private String orderId;

        private String orderType;

        private String price;

        private String buyer;
    }
}
