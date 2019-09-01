package com.github.netty.example.consumer;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.netty.springboot.EnableNettyEmbedded;
import com.github.netty.springboot.EnableNettyRpcClients;
import com.github.netty.springboot.client.NettyRpcLoadBalanced;
import com.github.netty.springboot.client.NettyRpcRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * RPC消费者 端口号:10000, 远程端口号: 10001
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 *
 * @author wangzihao  (示例)
 */
@EnableNettyEmbedded//切换容器的注解, 可选不切换, 继续用tomcat
@SpringBootApplication
@EnableNettyRpcClients//这里开启自动注入RPC服务功能
@EnableDiscoveryClient
public class RpcConsumerApplication10000 {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerApplication10000.class, args);
    }

    /**
     * 寻找地址, 需要用户自行根据需求实现 (这里用 nacos实现)
     * @author wangzihao
     */
    @Component
    public static class NacosRpcLoadBalanced implements NettyRpcLoadBalanced {
        @NacosInjected
        private NamingService namingService;
        @Override
        public InetSocketAddress chooseAddress(NettyRpcRequest request) {
            InetSocketAddress inetSocketAddress;
            try {
                Instance instance = namingService.selectOneHealthyInstance(request.getServiceName());
                inetSocketAddress = new InetSocketAddress(instance.getIp(), instance.getPort());
            } catch (NacosException e) {
                inetSocketAddress = new InetSocketAddress("localhost",10001);
            }
            return inetSocketAddress;
        }
    }

}
