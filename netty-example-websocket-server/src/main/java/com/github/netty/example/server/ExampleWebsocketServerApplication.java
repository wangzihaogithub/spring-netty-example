package com.github.netty.example.server;

import com.github.netty.springboot.EnableNettyServletEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Websocket服务端 端口号:10003
 * Netty-websocket 适用于数据量小(例: 2M左右), 并发高的场景
 * @author 84215
 */
@EnableNettyServletEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class ExampleWebsocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleWebsocketServerApplication.class, args);
	}
}
