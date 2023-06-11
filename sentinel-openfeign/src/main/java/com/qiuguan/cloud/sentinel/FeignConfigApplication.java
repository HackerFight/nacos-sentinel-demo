package com.qiuguan.cloud.sentinel;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiuguan
 * @date 2023/06/11 14:22:01  星期日
 *
 * 排除nacos, 因为我的主pom中导入了nacos依赖，所以sentinel-feign直接就继承了，
 * 但是我这里可以不使用nacos(当然使用也没有问题),所以排除掉nacos的配置类
 * 还有一种方式，请看sentinel-flow的pom.xml
 */

@SpringBootApplication(exclude = {
        NacosDiscoveryAutoConfiguration.class,
        NacosDiscoveryClientConfiguration.class,
        NacosServiceRegistryAutoConfiguration.class
})
public class FeignConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignConfigApplication.class, args);
    }
}
