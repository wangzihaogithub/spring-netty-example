package com.github.netty.example.consumer.api;

import com.github.netty.springboot.NettyRpcClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * hello world
 *
 * @author wangzihao
 */
@NettyRpcClient(serviceImplName = "rpc-provider-service")
//这里的名称会传入NettyRpcLoadBalanced.class的chooseAddress方法,由chooseAddress方法提供IP地址.该方法由消费者自行实现(例如: eureka zokeeper, nacos)
@RequestMapping("/rpc/provider/controller")//这里的值要与服务端的值一致
public interface HelloRpcController {

    /**
     * hello world
     *
     * @param name
     * @param id
     * @param pwd
     * @return
     */
    Map helloController(@RequestParam("name") String name, @RequestParam("id") Integer id, @RequestParam("pwd") String pwd);//方法名称与 生产者(服务端)对应

}