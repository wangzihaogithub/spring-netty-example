package com.github.netty.example.consumer.controller;

import com.github.netty.example.consumer.api.HelloRpcController;
import com.github.netty.example.consumer.api.HelloRpcService;
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
 * @author 84215
 */
@RestController
public class MyController {
    @Resource
    private HelloRpcService helloRpcService;
    @Resource
    private HelloRpcController helloRpcController;

    @RequestMapping("/hello")
    public Object hello(@RequestParam Map query, @RequestBody(required = false) Map body,
                        HttpServletRequest request, HttpServletResponse response) {
        String name = (String) query.get("name");
        String pwd = (String) query.get("pwd");

        Map map = new HashMap();
        map.put("1",helloRpcService.sayHello(query,name,1,pwd));
        map.put("2",helloRpcController.sayHello(name,1,pwd));
        return map;
    }
}
