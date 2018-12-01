package com.github.nett.example.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author 84215
 */
@RequestMapping("/rpc/helloService")//这里的值要与 生产者(服务端)对应
public interface HelloRpcApi {

    /**
     * hello world
     * @param body
     * @param name
     * @param id
     * @param pwd
     * @return
     */
    Map sayHello(@RequestBody Map body, @RequestParam("name") String name, @RequestParam("id") Integer id, @RequestParam("pwd") String pwd);//方法名称与 生产者(服务端)对应

}