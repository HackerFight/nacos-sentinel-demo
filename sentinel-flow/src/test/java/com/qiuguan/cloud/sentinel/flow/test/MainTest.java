package com.qiuguan.cloud.sentinel.flow.test;

import com.qiuguan.cloud.sentinel.flow.SentinelFlowApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author qiuguan
 * @date 2023/05/22 09:42:39  星期一
 *
 * 路径必须包含 {@link com.qiuguan.cloud.sentinel.flow.SentinelFlowApplication} 所在的路径
 * "com.qiuguan.cloud.sentinel.flow", 可以在此路径上进行延伸
 *
 * 如果不想保持上面的规则，可以使用classes属性指定主配置类
 */
@SpringBootTest(classes = SentinelFlowApplication.class)
public class MainTest {

    @Test
    public void test(){
        //-1
        System.out.println(Math.round(-1.5));
    }

    /**
     * 线程池的submit方法不会抛出异常，如果想获取异常，只能在get()时获取
     */
    @Test
    public void submitThreadTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(this::runTaskWithException);
    }

    @Test
    public void submitThreadWithExceptionGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(this::runTaskWithException);
        //get的时候才会看到异常
        Integer result = future.get();
        System.out.println("result = " + result);

    }

    /**
     * 线程池的execute方法会直接抛出异常
     */
    @Test
    public void executeThreadTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(this::runTaskWithException);
    }

    private int runTaskWithException() {
        int x = 1 / 0;
        return x;
    }
}
