package com.github.netty.example.provider;

import com.github.netty.springboot.EnableNettyEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * RPC生产者 端口号:10001 可以单端口同时使用多协议,例:80端口[http,rpc,mqtt,ftp] (这样依赖spring)
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 *
 * @author wangzihao
 */
@EnableNettyEmbedded//这里需要切换为netty容器
@SpringBootApplication
@EnableDiscoveryClient
public class RpcProviderApplication10001 {

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication10001.class, args);
    }

}