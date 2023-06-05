package com.qiuguan.cloud.sentinel.flow.pattern.controller.link;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author qiuguan
 * @date 2023/06/05 14:05:34  星期一
 *
 * 下面记录了资源之间的调用链路，这些资源通过调用关系，相互之间构成一颗调用树，这颗树的根节点是名为getUser的节点，调用链
 * 的入口都是他的子节点
 *
 *  /order/test1     /order/test2
 *        \\       //
 *         \\     //
 *          \\   //
 *           \\ //
 *          getUser
 *
 * 上面的 /order/test1 ，/order/test2接口都调用了资源getUser,sentinel允许只针对某个入口的统计信息对资源限流.
 *
 * 配置限流规则步骤：
 * 1.选择 getUser 作为资源
 * 2.选择 "关联" 模式
 * 3.选择 入口资源："/order/test1"(选择"/order/test2"也可以）
 * 4.设置QPS=3
 * 5.访问/order/test1被限流了，访问 /order/test2没有被限流
 *
 * 我的疑问？
 * 我可以选择/order/test1作为资源，然后选择"关联"模式，选择 getUser作为关联资源，效果是一样的呀。。。那链路的特点在哪里?
 *
 * 当然了，如果使用"直接模式"，对getUser进行限流，那么 /order/test1 和 /order/test2 都会受到影响！
 */
@RestController
@Slf4j
public class SentinelFlowLinkPatternController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/test1")
    public String test1(){
        return this.orderService.getUser(UUID.randomUUID().toString());
    }

    @GetMapping("/order/test2")
    public String test2(){
        return this.orderService.getUser(UUID.randomUUID().toString());
    }
}
