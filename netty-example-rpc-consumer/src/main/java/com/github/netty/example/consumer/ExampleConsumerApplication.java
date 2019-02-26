package com.github.netty.example.consumer;

import com.github.netty.springboot.EnableNettyRpcClients;
import com.github.netty.springboot.EnableNettyServletEmbedded;
import com.github.netty.springboot.client.NettyRpcLoadBalanced;
import com.github.netty.springboot.client.NettyRpcRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

/**
 * RPC消费者 端口号:10000, 远程端口号: 10001
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 * @author wangzihao  (示例)
 */
//@EnableNettyServletEmbedded//切换容器的注解, 可选不切换, 继续用tomcat
@EnableNettyRpcClients//这里开启自动注入RPC服务功能
@SpringBootApplication
public class ExampleConsumerApplication {

    /**
     * 寻找地址, 需要用户自行根据需求实现
     * @return RPC负载均衡器
     */
    @Bean
    public NettyRpcLoadBalanced nettyRpcLoadBalanced(){
        return new NettyRpcLoadBalanced() {
            public InetSocketAddress chooseAddress(NettyRpcRequest request) {
                return new InetSocketAddress("localhost",10001);
            }
        };
    }

    public static void main(String[] args) {
		SpringApplication.run(ExampleConsumerApplication.class, args);
	}

}
