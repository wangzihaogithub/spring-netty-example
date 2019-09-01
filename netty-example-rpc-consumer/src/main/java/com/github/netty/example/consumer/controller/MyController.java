package com.github.netty.example.consumer.controller;

import com.github.netty.example.consumer.api.HelloRpcController;
import com.github.netty.example.consumer.api.HelloRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzihao
 */
@RestController
@RefreshScope
public class MyController {
    @Resource
    private HelloRpcService helloRpcService;
    @Resource
    private HelloRpcController helloRpcController;
    @Value("${user.name:}")
    private String configCenterTest;

    @RequestMapping("/hello")
    public Object hello(@RequestParam Map query, @RequestBody(required = false) Map body,
                        HttpServletRequest request, HttpServletResponse response) {
        String name = (String) query.get("name");

        Map map = new HashMap();
        map.put("1", helloRpcService.sayHello(query, name, 1, configCenterTest));
        map.put("2", helloRpcController.sayHello(name, 1, configCenterTest));
        return map;
    }
}
