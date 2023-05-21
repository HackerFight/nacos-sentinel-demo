package com.qiuguan.cloud.sentinel.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qiuguan
 * @date 2023/05/16 14:18:14  星期二
 */
//@EnableDiscoveryClient
@SpringBootApplication
public class SentinelFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelFlowApplication.class, args);
    }
}
