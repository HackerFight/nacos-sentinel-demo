package com.qiuguan.cloud.sentinel.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author qiuguan
 * @date 2023/06/11 14:45:52  星期日
 */

/**
 * 这种方式调用，默认他会去服务注册中心，寻找application-name为 "mock-stock-application"的应用，然后以
 * [http://mock-stock-application/stock/reduce] 方式进行调用。但是由于我的mock服务并没有注册到注册中心
 * 所以这里是无法找不到的，这样就会报错了。
 *
 *一般来说，如果是公司微服务内部应用之间的调用，用这个完全没有问题的。这个 path 类似于Controller类上的 @RequestMapping注解指定的路径
 * 但是，openfeign既可以通过服务器名进行微服务内部之间的调用，也可以调用外部的服务的。我这里就演示调用外部的mock服务
 */
//@FeignClient(name = "mock-stock-application", path = "/stock")

/**
 * 模拟调用外部服务，此时name就随便写了，重点是这个url,就是我的 sentinel-fallback-mock-server服务
 */
@FeignClient(name = "mockServer", url = "localhost:9001/stock")
public interface OrderFeignClient {

    @GetMapping("/reduce")
    String stockReduce();
}
