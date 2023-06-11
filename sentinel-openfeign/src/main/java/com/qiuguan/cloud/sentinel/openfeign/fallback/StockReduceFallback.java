package com.qiuguan.cloud.sentinel.openfeign.fallback;

import com.qiuguan.cloud.sentinel.openfeign.client.Order2FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author qiuguan
 * @date 2023/06/11 15:46:13  星期日
 *
 * 1.必须加上 @Component注解
 * 2.必须实现 feign 接口
 */
@Component
public class StockReduceFallback implements Order2FeignClient {

    @Override
    public String stockReduce() {
        return "fallback服务降级......";
    }
}
