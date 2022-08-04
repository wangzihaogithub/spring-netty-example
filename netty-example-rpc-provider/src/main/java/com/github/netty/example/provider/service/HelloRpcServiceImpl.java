package com.github.netty.example.provider.service;

import com.github.nett.example.api.HelloRpcApi;
import com.github.netty.annotation.NRpcService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzihao
 */
@NRpcService//启用服务端的RPC
public class HelloRpcServiceImpl implements HelloRpcApi {

    @Override
    public Map helloService(Map body, String name, Integer id, String pwd) {
        Map map = new HashMap(1);
        map.put("msg", "helloService (" + body + "," + name + "," + id + "," + pwd + ") " + System.currentTimeMillis());
        return map;
    }
}
