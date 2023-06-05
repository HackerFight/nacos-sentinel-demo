package com.qiuguan.cloud.sentinel.flow.pattern.controller.link;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qiuguan
 * @date 2023/06/05 14:05:53  星期一
 */
@Slf4j
@Service
public class OrderService {

    /**
     * 当访问 http://localhost:8085/order/test1 时，其内部会调用 {@link  #getUser(String)}
     * 方法，而这个方法标有 {@link SentinelResource} 注解，所以在sentinel控制台上，将会有getUser资源
     *
     *
     * 注意：使用了 {@link SentinelResource} 注解，被限流后就不会走统一的
     * {@link com.qiuguan.cloud.sentinel.flow.ex.SentinelBlockExceptionHandler}异常处理了
     * 不然限流后页面显示500错误，所以这里需要手动指定 blockHandler 或者 blockHandlerClass 属性.
     *
     * 不过我将"链路"修改为"关联"后，发现全局异常生效了，不会走我这里定义的了。。。why ????
     * 原来是我弄错了：
     *  1）如果资源是 getUser, 关联的资源是 /order/test2, 那么还是走{@link SentinelResource} 注解的异常handler
     *  2) 如果资源是 /order/test2, 关联的资源是 getUser, 那么走的是全局异常。
     * 所以说，到底是走全局异常还是注解定义的异常，就看资源是谁。如果资源在注解上，那么一定走的是注解定义的handler
     */
    @SentinelResource(value = "getUser",
            /*blockHandler = "defaultBlockHandlerGetUser", */

            //这样可以使用外部类的外部方法，单指定 blockHandlerClass 也不行，找不到方法
            blockHandlerClass = DefaultBlockHandler.class,
            blockHandler = "customHandleGetUser")
    public String getUser(String userId){
        return "{\"userId:" + userId +"\", \"userName:qiuguan\"}";
    }

    /**
     * 注意：要和源方法参数保持一致，然后最后添加上 {@link BlockException} 异常，切记不要忘记
     * 注意：参数顺序哦！
     */
    @SuppressWarnings("unused")
    public String defaultBlockHandlerGetUser(String userId, BlockException blockException){
        return String.format("getUser被限流，userId = %s, ex = %s", userId, blockException.getMessage());
    }


    /**
     * 针对 blockHandlerClass
     */
    static class DefaultBlockHandler {

        /**
         * 方法必须是static的, 参数也要对应上
         */
        @SuppressWarnings("unused")
        public static String customHandleGetUser(String userId, BlockException blockException){
            log.error("被限流了...", blockException);
            return String.format("哈哈哈, getUser被限流，userId = %s", userId);
        }
    }
}
