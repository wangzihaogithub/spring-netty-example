package com.github.netty.example.consumer.controller;

import com.github.netty.example.consumer.api.HelloRpcController;
import com.github.netty.example.consumer.api.HelloRpcService;
import com.github.netty.protocol.nrpc.RpcClientChunkCompletableFuture;
import com.github.netty.springboot.NettyRpcClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangzihao
 */
@RestController
@RequestMapping("/rpc/consumer")
public class MyController {
    @Resource
    private HelloRpcService helloRpcService;
    @Resource
    private HelloRpcController helloRpcController;
    @Resource
    private HelloRpcAsyncClient helloRpcAsyncClient;
    @Resource
    private HelloRpcStreamClient helloRpcStreamClient;

    @RequestMapping("/callProvider")
    public DeferredResult<Map> callProvider(@RequestParam Map query, @RequestBody(required = false) Map body,
                                            HttpServletRequest request, HttpServletResponse response) {
        DeferredResult<Map> deferredResult = new DeferredResult<>();

        String name = (String) query.get("name");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("call-service", helloRpcService.helloService(query, name, 1, "Test"));
        map.put("call-controller", helloRpcController.helloController(name, 1, "Test"));
        helloRpcAsyncClient.helloService(query, name, 1, "Test").whenComplete((map1, throwable) -> {
            map.put("call-service-async", map1);

            Map<String, Object> streamData = new LinkedHashMap<>();
            map.put("call-stream", streamData);
            helloRpcStreamClient.helloStream(query, name, 2, "Test").whenChunk((chunk, index) -> {
                streamData.put(index + "", chunk);
            }).whenComplete((complete, throwable1) -> {
                streamData.put("complete", complete);
                streamData.put("throwable", throwable1);
                deferredResult.setResult(map);
            });
        });
        return deferredResult;
    }

    @NettyRpcClient(serviceName = "rpc-provider-service")
    @RequestMapping("/rpc/provider/service")//这里的值要与 生产者(服务端)对应
    public interface HelloRpcAsyncClient {
        CompletableFuture<Map> helloService(@RequestBody Map body, @RequestParam("name") String name, @RequestParam("id") Integer id, @RequestParam("pwd") String pwd);//方法名称与 生产者(服务端)对应
    }

    @NettyRpcClient(serviceName = "rpc-provider-service")
    @RequestMapping("/rpc/provider/stream")//这里的值要与 生产者(服务端)对应
    public interface HelloRpcStreamClient {
        RpcClientChunkCompletableFuture<Map, String> helloStream(@RequestBody Map body, @RequestParam("name") String name, @RequestParam("id") Integer id, @RequestParam("pwd") String pwd);//方法名称与 生产者(服务端)对应
    }

}
