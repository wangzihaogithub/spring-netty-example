package com.github.netty.example.consumer.controller;

import com.github.netty.protocol.NRpcProtocol;
import com.github.netty.protocol.nrpc.*;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * rpc调用的生命周期管理
 * @author wangzihao 2019年12月7日16:33:43
 */
@Component
public class RpcAop implements RpcClientAop, RpcServerAop {
    @Override
    public void onInitAfter(NRpcProtocol protocol) {
        System.out.println("rpcContext = " + protocol);
    }

    @Override
    public void onConnectAfter(RpcServerChannelHandler channel) {
        System.out.println("rpcContext = " + channel);
    }

    @Override
    public void onDisconnectAfter(RpcServerChannelHandler channel) {
        System.out.println("rpcContext = " + channel);
    }

    @Override
    public void onDecodeRequestBefore(RpcContext<RpcServerInstance> rpcContext, Map<String, Object> params) {
        System.out.println("rpcContext = " + rpcContext);
    }

    @Override
    public void onInitAfter(RpcClient rpcClient) {
        System.out.println("rpcContext = " + rpcClient);
    }

    @Override
    public void onConnectAfter(RpcClient rpcClient) {
        System.out.println("rpcContext = " + rpcClient);
    }

    @Override
    public void onDisconnectAfter(RpcClient rpcClient) {
        System.out.println("rpcContext = " + rpcClient);
    }

    @Override
    public void onEncodeRequestBefore(RpcContext<RpcClient> rpcContext, Map<String, Object> params) {
        System.out.println("rpcContext = " + rpcContext);
    }

    @Override
    public void onResponseAfter(RpcContext rpcContext) {
        System.out.println("rpcContext = " + rpcContext);
    }

    @Override
    public void onStateUpdate(RpcContext<RpcClient> rpcContext) {
        System.out.println("rpcContext = " + rpcContext);
    }
}