package com.qiuguan.cloud.sentinel;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author qiuguan
 * @date 2023/06/11 17:06:09  星期日
 *
 * 排除nacos, 当然也可以配置，我这里就不配置了，所以要移除掉，不然从父模块继承了nacos依赖，启动时会报错
 */
@SpringBootApplication(exclude = {
        NacosDiscoveryAutoConfiguration.class,
        NacosDiscoveryClientConfiguration.class,
        NacosServiceRegistryAutoConfiguration.class
})
public class HotParamApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotParamApplication.class, args);
    }
}
