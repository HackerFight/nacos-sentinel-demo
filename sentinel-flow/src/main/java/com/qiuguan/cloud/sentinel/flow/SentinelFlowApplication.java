package com.qiuguan.cloud.sentinel.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qiuguan
 * @date 2023/06/10 00:01:25  星期六
 *
 * 排除nacos, 因为我的主pom中导入了nacos依赖，所以sentinel-feign直接就继承了，
 * 但是我这里测试是sentinel,和nacos关系不大（持久化会用到），所以我这里把它排除掉,
 * 还有一种方式，请看sentinel-flow的pom.xml
 */
//@SpringBootApplication(exclude = {
//        NacosDiscoveryAutoConfiguration.class,
//        NacosDiscoveryClientConfiguration.class,
//        NacosServiceRegistryAutoConfiguration.class
//})
//@EnableDiscoveryClient
@SpringBootApplication
public class SentinelFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelFlowApplication.class, args);
    }
}
