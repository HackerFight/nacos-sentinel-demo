# 1.关于版本选择
关于SpringBoot, Spring Cloud, Spring Cloud Alibaba 三者版本的选择可以参考[官方文档](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E) 或者使用[脚手架](https://start.aliyun.com/) 来构建项目，这样可以避免版本冲突。

# 2. sentinel 控制台
[参考官方文档说明](https://sentinelguard.io/zh-cn/docs/dashboard.html)

# 3.sentinel 如何使用
[如何使用](https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8)

# 4.nacos 2.x 版本客户端说明
首先说一下，如果只是测试sentinel,可以不用连接nacos的，但是我为了全面，还是配置了nacos,但是在连接时报错：【Client not connected,current status:STARTING,StatusRuntimeException】，后确定是因为nacos 2.x 版本后新增了gprc的通信方式，需要新开放2个端口，分别是9848和9849，由于我使用是阿里云服务器，需要去服务中配置即可


# 5.模块说明
## 5.1 sentinel-flow : 限流模块测试
### 5.1.1 <font color="red">流控模式</font>
#### 5.1.1.1 简单流控模式
1. 请看 `SentinelFlowQPSController` 和 `SentinelFlowThreadController`
> 其中 '阈值类型=线程并发数' 的可以将值配置的小一点，然后开启两个浏览器窗口(无痕)进行测试
2. 请看 /resources/sentinel-img/目录下的图片看下，比较容易理解
#### 5.1.1.2 关联流控模式
1. 请看 `SentinelFlowCorrelationPatternController`
2. 请看 配置细节<br>![配置图片](sentinel-flow/src/main/resources/关联/关联资源限流.png)
> 注意：关联资源那里应该是步骤2
3. 如何测试？需要借助jmeter <br>
  <b><font color="green">i: 定义线程组</font></b> <br>
 ![线程组](sentinel-flow/src/main/resources/关联/1-定义线程组.png) <br>
   <b><font color="green">ii: 定义线程组</font></b> <br>
 ![http请求](sentinel-flow/src/main/resources/关联/2-定义http请求.png) <br>
   <b><font color="green">iii: 定义线程组</font></b> <br>
 ![结果树](sentinel-flow/src/main/resources/关联/3-定义结果树.png) <br>
   <b><font color="green">iiii: 通过浏览器访问订单查询接口</font></b>
  > http://localhost:8085/get 可以看到被限流了。<br>
  ![限流](sentinel-flow/src/main/resources/关联/4-限流.png)

#### 5.1.1.3 链路流控模式
1. 请看 `SentinelFlowLinkPatternController`类，重点看下注释
2. 资源关系网说明：
> 下面记录了资源之间的调用链路，这些资源通过调用关系，相互之间构成一颗调用树，这颗树的根节点是名为getUser的节点，调用链的入口都是他的子节点 <br>
```html
 /order/test1   /order/test2
         \\       //
          \\     //
           \\   //
            \\ //
           getUser
```
3. 资源配置<br>
![链路资源配置](sentinel-flow/src/main/resources/链路/1-链路模式资源配置.png) <br>
4. 浏览器分别访问 http://localhost:8085/order/test1 和 http://localhost:8085/order/test2, 发现无论访问哪个，规则都不生效，这是为什么？ <br>
> 从1.6.3版本开始，Sentinel Web filter默认收敛所有URL的入口context,因此链路限流不生效。可以通过配置 `spring.cloud.sentinel.web-context-unify=false` 即可关闭收敛

```yaml
spring:
  application:
    name: nacos-sentinel-flow
  cloud:
    nacos:
      discovery:
        #该项目主要是测试sentinel,nacos不是必需的，等后面测试sentinel持久化时才会用到
        server-addr: 47.96.111.179:8848  #nacos 2.x后需要开放出9848和9849两个新端口，不然客户端无法连接服务器，我已经在阿里云服务器开放过了
    sentinel:
      eager: true  #默认是懒加载的,只有访问了一次接口才可以在sentinel控制台上显示
      transport:
        port: 8719 #默认是8719
        #dashboard: http://sentinel.hacketown.com  #sentinel dashboard 的服务器地址和端口
        dashboard: localhost:8080  #确保sentinel控制台和资源访问处于同一个网络中，方便测试我就在本地运行。端口默认是8080，可以在部署时手动指定端口运行
      log:
        dir: /Users/qiuguan/logs/sentinel/
      web-context-unify: false #针对链路模式的设置，默认情况下它将调用链路树收敛了，所以链路限流规则不生效，这里需要打开
```
5. 再次访问后就生效了。访问 /order/test2 就会被限流，而访问 /order/test1 就不会。<br>
6. 关于限流异常的说明
> 由于我定义的"资源"是getUser,而getUser上是标注了 @SentinelResource 注解的，所以此时全局的限流异常就不会生效了，而是使用
> 注解定义的限流异常handler。

7. 问题：我将"链路"修改为"关联"后，发现全局异常生效了，不会走@SientnelResource注解定义的了。。。why ????
* 原来是我弄错了：
   1) 如果资源是 getUser, 关联的资源是 /order/test2, 那么还是走{@link SentinelResource} 注解的异常handler
   2) 如果资源是 /order/test2, 关联的资源是 getUser, 那么走的是全局异常。
* 所以说，到底是走全局异常还是注解定义的异常，就看资源是谁。如果资源在注解上，那么一定走的是注解定义的handler

8. 关联模式和链路模式的区别？ <br>

   | 模式   | 含义                            | 使用场景                                                                                                           |
   ------|-------------------------------|----------------------------------------------------------------------------------------------------------------|
   | 关联模式 | 统计与当前资源相关的另一个资源，触发阀值时，对当前资源限流 | 比如用户支付时需要修改订单状态，同时用户要查询订单。查询和修改操作会争抢数据库锁，产生竞争。业务需求是有限支付和更新订单的业务，因此当修改订单业务触发國值时，需要对查询订单业务限流。                    |
   | 链路模式    | 只针对从指定链路访问到本资源的请求做统计，判断是否超过阈值 | 列如有两条请求链路：<br>/order/test1---→getUser<br>/order/test2---→getUser<br>如果只希望统计从/order/test2进入到getUser的请求，则可以这样按我上面的配置

### 5.1.2 <font color="red">流控效果</font>
#### 5.1.2.1 快速失败
[<b>直接拒绝<b>（RuleConstant.CONTROL_BEHAVIOR_DEFAULT）方式是默认的流量控制方式，当QPS超过任意规则的阈值后，新的请求就会被立即拒绝，拒绝方式为抛出FlowException。这种方式适用于对系统处理能力确切已知的情况下，比如通过压测确定了系统的准确水位时](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6#%E7%9B%B4%E6%8E%A5%E6%8B%92%E7%BB%9D)
> 前面测试流控模式时，默认的流控效果就是快速失败。

#### 5.1.2.2 Warm Up
[Warm Up（RuleConstant.CONTROL_BEHAVIOR_WARM_UP）方式，即预热/冷启动方式。当系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6#warm-up)
* [参考官网](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6)
> 比如某些商品平时购买量很少，但是大促活动开始，购买量激增，要避免这种瞬时流量把系统打垮


 `冷加载因子，codeFactor = 3, 即请求QPS从 threshold/3开始，经过预热时长逐渐升至设定的QPS阈值`

1. 请看 `WarmUpController` 类
2. sentinel配置预热参数 <br>
![warmup](sentinel-flow/src/main/resources/流控效果/warmup/1-warmup参数设置.png)
> 效果应该是从 15 / 3 = 5 开始在5s内达到阈值

3. 效果演示，浏览器不明显，还是用jmeter测试 <br>
    1) 定义线程组 <br>
   ![定义线程组](sentinel-flow/src/main/resources/流控效果/warmup/4-定义线程组.png)
    2) 定义http请求 <br>
   ![定义请求](sentinel-flow/src/main/resources/流控效果/warmup/5-定义http请求.png)
    3) 观察sentinel控制台的实时监控
   ![实时监控1](sentinel-flow/src/main/resources/流控效果/warmup/2-实时监控.png) <br>
   ![实时监控2](sentinel-flow/src/main/resources/流控效果/warmup/3-实时监控.png)


#### 5.1.2.3 匀速排队
匀速排队（RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER）方式会严格控制请求通过的间隔时间，也即是让请求以均匀的速度通过，对应的是漏桶算法。[参考官方](https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6#%E5%8C%80%E9%80%9F%E6%8E%92%E9%98%9F)

* WarmUp： 激增流量
* 匀速排队：脉冲流量

1. 请看 `QueueUpController`
2. 体验脉冲流量
   1) 借助jmeter <br>
   ![定义线程组](sentinel-flow/src/main/resources/流控效果/queue/1-定义线程组.png)<br>
   ![定义定时器](sentinel-flow/src/main/resources/流控效果/queue/2-定义定时器.png) <br>
   ![定义请求](sentinel-flow/src/main/resources/流控效果/queue/3-定义http请求.png) <br>
   
   2) sentinel实时监控大盘 <br>
   ![定义请求](sentinel-flow/src/main/resources/流控效果/queue/4-实时监控.png) <br>
   ![定义请求](sentinel-flow/src/main/resources/流控效果/queue/5-实时监控.png) <br>

> 就是一会有流量，一会又空闲，一会又有流量

针对这种情况，如果我们使用快速失败来看下效果，还是以 `QueueUpController` 为例，我们在sentinel控制台配置QPS=5, 然后流控效果是快速失败，使用上面的jmeter配置进行测试：<br>
![定义请求](sentinel-flow/src/main/resources/流控效果/queue/6-快速失败例子.png) <br>
很明显就是一部分失败，一部分成功，现在我们用 <b>快速排队</b> 来看下：<br>
![定义请求](sentinel-flow/src/main/resources/流控效果/queue/7-排队等待参数配置.png) <br>

接下来在使用上面的jmeter配置进行测试: <br>
![定义请求](sentinel-flow/src/main/resources/流控效果/queue/8-排队等待.png) <br>

从结果上看，没有失败的线程了，全部都被处理了，这样充分利用了服务器的空闲时间，还减少了瞬时流量对服务器的冲击，简直是一举两得。

## 5.2 sentinel-feign : feign的集成测试
## 5.3 sentinel-gateway : 网关的集成测试
## 5.4 sentinel-nacos-datasource : sentinel配置的持久化



# 6.参考文档
1. [参考文档1](https://developer.aliyun.com/article/878296)

# 7.其他
1. 项目开始只有一个父工程，后面我将父工程的代码结构(src)直接复制到了新的模块[sentinel-flow]中，运行主配置类时报错，找不到主配置类，此时可以重新编译一下 <br>
![img.png](sentinel-flow/src/main/resources/img/maven%20编译.png)

2. sentinel不显示资源 <br>
![sentinel资源show](sentinel-flow/src/main/resources/img/sentinel%20局域网限制.png)
请求接口后无法看到实时监控，关闭懒加载后也一样看不到，添加了相关注解也无法看到，原来是因为我的请求资源(Controller)和sentinel控制台不在同一个网络中，sentinel控制台我是部署到了阿里云服务器上，资源是本地的springboot, 只需要将二者部署在同一个网络中即可。

