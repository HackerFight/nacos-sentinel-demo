package com.qiuguan.cloud.sentinel.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author qiuguan
 * @date 2023/06/11 14:45:52  星期日
 */
@FeignClient(name = "order", path = "")
public interface OrderFeignClient {

}
