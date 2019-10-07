package com.github.netty.example.server;

import com.github.netty.springboot.EnableNettyEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Websocket服务端 端口号:10003
 * Netty-websocket 适用于数据量小(例: 2M左右), 并发高的场景
 * @author wangzihao
 */
@EnableNettyEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class WebsocketApplication10003 {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication10003.class, args);
    }
}
