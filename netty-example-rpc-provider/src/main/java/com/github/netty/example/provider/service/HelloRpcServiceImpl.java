package com.github.netty.example.provider.service;

import com.github.nett.example.api.HelloRpcApi;
import com.github.netty.annotation.Protocol;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzihao
 */
@Protocol.RpcService//启用服务端的RPC
@Service
public class HelloRpcServiceImpl implements HelloRpcApi {

    @Override
    public Map sayHello(Map body,String name, Integer id, String pwd) {
        Map map = new HashMap(1);
        map.put("msg","hello rpc service ("+body+"," + name+ ","+id+","+pwd+") "+System.currentTimeMillis());
        return map;
    }
}
