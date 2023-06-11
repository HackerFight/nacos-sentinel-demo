package com.qiuguan.cloud.sentinel.openfeign.fallback;

import com.qiuguan.cloud.sentinel.openfeign.client.Order2FeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author qiuguan
 * @date 2023/06/11 16:01:14  星期日
 *
 *  1. {@link FallbackFactory} 的泛型必须是 {@link org.springframework.cloud.openfeign.FeignClient} 注解标注的
 *      接口，否者该降级不会生效，还会报错。
 *  2.必须添加 @Component注解
 */
@Component
public class StockReduceFallbackFactory implements FallbackFactory<Order2FeignClient> {

    @Override
    public Order2FeignClient create(Throwable cause) {
        return () -> String.format("fallback factory 降级, cause: %s, time: %s", cause, LocalDateTime.now());
    }
}
