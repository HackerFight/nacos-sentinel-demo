package com.qiuguan.cloud.sentinel;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiuguan
 * @date 2023/06/11 21:29:04  星期日
 *
 * 奇怪，sentinel-flow, sentinel-fallback-mock-server, sentinel-hotparam 等模块
 * 都没有使用nacos, 但是由于从父模块继承了nacos的依赖，所以也就当于有了。
 *
 * 同样的，sentinel-system-rule 模块也会继承的。
 *
 * 但是我发现，如果我要在application.yaml中配置 sentinel, 不配置nacos, 启动的时候也会报错
 * 报错的内容就是nacos, 就很奇怪，配置sentinel 和nacos有啥关系。。。
 *
 * 所以如果在application.yaml中配置了sentinel, 则需要手动排除nacos
 */
@SpringBootApplication(exclude = {
        NacosDiscoveryAutoConfiguration.class,
        NacosDiscoveryClientConfiguration.class,
        NacosServiceRegistryAutoConfiguration.class
})
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
