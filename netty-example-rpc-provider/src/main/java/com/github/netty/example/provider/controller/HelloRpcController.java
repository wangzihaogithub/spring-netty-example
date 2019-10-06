package com.github.netty.example.provider.controller;

import com.github.netty.annotation.Protocol;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzihao
 */
@Protocol.RpcService//启用服务端的RPC
@RequestMapping("/rpc/provider/controller")
@RestController
public class HelloRpcController {

    @RequestMapping("/helloController")
    public Map helloController(String name, Integer id, String pwd, HttpServletResponse response) throws IOException, InterruptedException {
//        response.setHeader("Content-Type", "application/json");
//        int i = 0;
//        while (i< 100){
//            i++;
//            Thread.sleep(1000);
//            response.getWriter().write(i + "你好");
//            response.flushBuffer();
//        }
        Map map = new HashMap(1);
        map.put("msg", "helloController (" + name + "," + id + "," + pwd + ") " + System.currentTimeMillis());
        return map;
    }

}
