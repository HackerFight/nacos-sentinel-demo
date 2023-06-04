package com.qiuguan.cloud.sentinel.flow.controller.correlation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author qiuguan
 * @date 2023/05/24 11:41:23  星期三
 * <p>
 * 流控模式："关联"
 * 当两个资源之间具有资源争抢或者依赖关系的时候，这两个资源便具有了关联。
 * 比如对数据库同一个字段的读操作和写操作存在争抢，读的速度过高会影响写得速度，写的速度过高会影响读的速度。
 * 如果放任读写操作争抢资源，则争抢本身带来的开销会降低整体的吞吐量。可使用关联限流来避免具有关联关系的资源之间过度的净抢，
 * 举例来说，read_db和write_b这两个资源分别代表数据库读写，我们可以给read_db设置限流规则来达到写优先的目的：设置strategy为RuleConstant.STRATEGY_RELATE
 * 同时设置refResource为write_db。这样当写库操作过于频繁时，读数据的诗求会被限流。
 * </p>
 *
 * <img src = "sentinel-flow/src/main/resources/关联/关联资源限流.png"></img>
 *
 * 如何测试？
 * 1.要借助jmeter
 * 2.jmeter 访问 下单接口
 * 3.然后我们通过浏览器访问订单查询接口，看看是否被限流
 */
@Slf4j
@RestController
public class SentinelFlowCorrelationPatternController {

    //hh: 12小时制
    //HH: 24小时制
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 下单：生成订单触发查询订单的限流
     *
     * 生成订单的访问量比较大，就让查询订单限流
     */
    @GetMapping("/add")
    public String addOrder() {
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN));
        log.info("下单成功!, 时间: {}", orderTime);
        return String.format("订单生成: %s", orderTime);
    }

    /**
     * 查询订单
     * 限流资源，但是是由于下单触发的。所以在sentinel配置的时候要选择这个资源
     */
    @GetMapping("/get/{id}")
    public String getOrder(@PathVariable String id){
        log.info("查询订单, 订单id = {}", id);
        return String.format("查询订单，订单号：%s", id);
    }

}
