package com.github.netty.example.server;

import com.github.sseserver.LocalConnectionService;
import com.github.sseserver.LocalConnectionServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Websocket服务端 端口号:10005
 * 访问 http://localhost:10005/index.html 可以看效果
 *
 * @author wangzihao
 */
@SpringBootApplication
public class SseApplication10005 {

    public static void main(String[] args) {
        SpringApplication.run(SseApplication10005.class, args);
    }

    @Bean
    public LocalConnectionService localConnectionService() {
        return new LocalConnectionServiceImpl();
    }

}
