package com.github.netty.example.provider.controller;

import com.github.netty.rpc.annotation.RpcService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 84215
 */
@RpcService
@RequestMapping("/my/controller")
@RestController
public class HelloRpcController{

    @RequestMapping("/sayHello")
    public Map sayHello(String name, Integer id, String pwd) {
        Map map = new HashMap(1);
        map.put("msg","controller (name=" + name+ ",id="+id+",pwd="+pwd+") "+System.currentTimeMillis());
        return map;
    }

}
