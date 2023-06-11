package com.qiuguan.cloud.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuguan
 * @date 2023/06/11 17:06:41  星期日
 */
@Slf4j
@RestController
public class HotParamController {


    /**
     * 热点规则必须要结合 {@link SentinelResource} 注解使用。使用接口的 RequestMapping 注解资源是不生效的。
     * @param id
     *   比如：id为1的请求量特别大，其他的都很少，那么我就对id=2的进行流控
     * @param card
     * @param phone
     * @return
     */
    @SentinelResource(
            value = "hotParamResource",
            blockHandler = "hotBlock"
    )
    @GetMapping(value = {"/get/{id}/{card}/{phone}", "/get/{id}", "/get/{id}/{card}"})
    public String hot(@PathVariable Integer id,
                      @PathVariable(required = false) String card,
                      String phone){
        log.info("热点参数正常访问......");
        return "热点参数正常访问......";
    }

    public String hotBlock(Integer id, String card, String phone, BlockException blockException) {
        log.info("热点参数流控规则生效..........");
        return "热点参数限流";
    }
}
