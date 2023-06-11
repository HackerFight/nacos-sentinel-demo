package com.qiuguan.cloud.sentinel.openfeign.client;

import com.qiuguan.cloud.sentinel.openfeign.fallback.StockReduceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author qiuguan
 * @date 2023/06/11 15:43:41  星期日
 *
 * fallback 和 fallbackFactory 是用来降级的，这里需要导入hystrix
 * 如果二者同时指定，则fallback的优先级更高。
 *
 * 为了从sentinel控制台演示效果，我用factory进行测试，页面上的异常信息不一样。
 */
@FeignClient(
        name = "mock2Server",
        url = "localhost:9001/stock",
        /*fallback = StockReduceFallback.class,*/
        fallbackFactory = StockReduceFallbackFactory.class
)
public interface Order2FeignClient {

    @GetMapping("/reduce2")
    String stockReduce();
}
