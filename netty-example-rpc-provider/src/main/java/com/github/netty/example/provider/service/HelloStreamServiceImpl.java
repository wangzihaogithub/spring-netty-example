package com.github.netty.example.provider.service;

import com.github.netty.annotation.NRpcService;
import com.github.netty.protocol.nrpc.RpcEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzihao
 */
@NRpcService("/rpc/provider/stream")//启用服务端的RPC
public class HelloStreamServiceImpl {

    public RpcEmitter<Map, String> helloStream(Map requestBody, String name, Integer id, String pwd) {
        RpcEmitter<Map, String> emitter = new RpcEmitter<>();
        for (int i = 10; i > 0; i--) {
            emitter.send("chunk" + i);
        }
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<>(1);
            map.put("msg", "helloService (" + requestBody + "," + name + "," + id + "," + pwd + ") " + System.currentTimeMillis());
            emitter.complete(map);
        }).start();
        return emitter;
    }
}
