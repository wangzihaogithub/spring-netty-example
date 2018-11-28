package com.github.netty.example.provider.service;

import com.github.nett.example.api.HelloRpcApi;
import com.github.netty.rpc.annotation.RpcService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 84215
 */
@RpcService
@Service
public class HelloRpcServiceImpl implements HelloRpcApi {

    @Override
    public Map sayHello(String name, Integer id, String pwd) {
        Map map = new HashMap(1);
        map.put("msg","service (name=" + name+ ",id="+id+",pwd="+pwd+") "+System.currentTimeMillis());
        return map;
    }
}
