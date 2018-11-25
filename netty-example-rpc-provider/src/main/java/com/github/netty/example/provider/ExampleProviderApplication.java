package com.github.netty.example.provider;

import com.github.netty.rpc.RpcServer;
import com.github.netty.rpc.annotation.RpcService;

/**
 * RPC生产者 端口号:10001  (这样写不依赖spring)
 * Netty-rpc 适用于数据量小(例: 2M左右), 并发高的场景
 * @author 84215
 */
public class ExampleProviderApplication {

	public static void main(String[] args) {
		RpcServer server = new RpcServer("Provider", 10001);
		server.addInstance(new HelloRpcServiceImpl());
		server.run();
	}

	@RpcService("/helloRpcService")//该注解可以放在接口类 或 实现类上
    public static class HelloRpcServiceImpl {
        public String sayHello(String name) {
            return "hello-rpc-" + name+ "-"+System.currentTimeMillis();
        }
    }

}