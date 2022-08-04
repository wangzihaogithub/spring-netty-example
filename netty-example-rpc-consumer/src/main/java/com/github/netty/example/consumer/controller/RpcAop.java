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
public class RpcAop implements RpcServerAop {
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
    public void onResponseAfter(RpcContext rpcContext) {
        System.out.println("rpcContext = " + rpcContext);
    }

    @Override
    public void onStateUpdate(RpcContext<RpcServerInstance> rpcContext, State formState, State toState) {
        System.out.println("onStateUpdate = " + toState);
    }
}