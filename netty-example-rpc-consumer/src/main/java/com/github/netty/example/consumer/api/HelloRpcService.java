package com.github.netty.example.consumer.api;

import com.github.nett.example.api.HelloRpcApi;
import com.github.netty.springboot.NettyRpcClient;

/**
 * hello world
 * @author 84215
 */
@NettyRpcClient("github-db-user")//这里的名称会传入NettyRpcLoadBalanced.class的chooseAddress方法,由chooseAddress方法提供IP地址.该方法由消费者自行实现(例如: eureka zokeeper)
public interface HelloRpcService extends HelloRpcApi {

}