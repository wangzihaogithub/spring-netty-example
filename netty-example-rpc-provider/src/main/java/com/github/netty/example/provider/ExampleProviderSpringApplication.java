package com.github.netty.example.provider;

import com.github.netty.rpc.annotation.RpcService;
import com.github.netty.springboot.EnableNettyServletEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Map;

/**
 * RPC生产者 端口号:10001 可以单端口同时使用多协议,例:80端口[http,rpc,mqtt,ftp] (这样依赖spring)
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 * @author 84215
 */
@RestController
@EnableNettyServletEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class ExampleProviderSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleProviderSpringApplication.class, args);
	}

    @RequestMapping("/hello")
    public Object hello(@RequestParam Map query, @RequestBody(required = false) Map body,
                        HttpServletRequest request, HttpServletResponse response,
                        HttpSession session, Principal principal,
                        InputStream in, OutputStream out) {
        return "hello-servlet query="+query +",body="+ body ;
    }


    @Component
	@RpcService("/helloRpcService")//该注解可以放在接口类 或 实现类上
    public static class HelloRpcServiceImpl {
        public String sayHello(String name) {
            return "hello-rpc-" + name+ "-"+System.currentTimeMillis();
        }
    }

}