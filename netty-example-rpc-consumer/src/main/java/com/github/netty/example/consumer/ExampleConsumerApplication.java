package com.github.netty.example.consumer;

import com.github.netty.springboot.EnableNettyRpcClients;
import com.github.netty.springboot.NettyRpcLoadBalanced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.Map;

/**
 * RPC消费者 端口号:10000, 远程端口号: 10001
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 * @author 84215  (示例)
 */
@RestController
@EnableNettyRpcClients//这里开启自动注入RPC服务功能
//@EnableNettyServletEmbedded//切换容器的注解, 可选不切换, 继续用tomcat
@SpringBootApplication
public class ExampleConsumerApplication {

    @Resource
    private HelloRpcService exampleRpcService;

    public static void main(String[] args) {
		SpringApplication.run(ExampleConsumerApplication.class, args);
	}

	@Bean
    public NettyRpcLoadBalanced nettyRpcLoadBalanced(){
        return new NettyRpcLoadBalanced() {
            @Override
            public InetSocketAddress chooseAddress(String serviceId) {
                return new InetSocketAddress("localhost",10001);
            }
        };
    }

    @RequestMapping("/hello")
    public Object hello(@RequestParam Map query, @RequestBody(required = false) Map body,
                        HttpServletRequest request, HttpServletResponse response,
                        HttpSession session, Principal principal,
                        InputStream in, OutputStream out) {
        return exampleRpcService.sayHello("wang");
    }
}
