package com.qiuguan.cloud.sentinel.openfeign.controller;

import com.qiuguan.cloud.sentinel.openfeign.client.Order2FeignClient;
import com.qiuguan.cloud.sentinel.openfeign.client.OrderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/11 15:47:47  星期日
 */
@Slf4j
@RestController
public class Order2Controller {

    @Autowired
    private Order2FeignClient order2FeignClient;

    @GetMapping("/createOrder")
    public String create(){
        log.info("下单成功.....");
        String reduce = this.order2FeignClient.stockReduce();
        log.info(reduce);
        return "feign call success :" +reduce;
    }
}
