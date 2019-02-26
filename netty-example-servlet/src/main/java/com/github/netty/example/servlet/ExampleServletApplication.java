package com.github.netty.example.servlet;

import com.github.netty.springboot.EnableNettyServletEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
 * web服务器. 端口号:10002 （将tomcat换成了netty, 可以从request,response的实现看出来）
 *
 * Netty-servlet 适用于数据量小(例: 2M左右), 并发高的场景
 * @author wangzihao  (示例)
 */
@RestController
@EnableNettyServletEmbedded//切换容器的注解
@SpringBootApplication
public class ExampleServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleServletApplication.class, args);
	}

	@RequestMapping("/hello")
	public Object hello(@RequestParam Map query, @RequestBody(required = false) Map body,
						HttpServletRequest request, HttpServletResponse response,
						HttpSession session, Principal principal,
						InputStream in, OutputStream out) {
		return "hello-servlet query="+query +",body="+ body ;
	}

}
