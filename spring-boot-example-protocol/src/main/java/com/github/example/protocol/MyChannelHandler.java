package com.github.example.protocol;

import com.github.netty.core.AbstractChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 2019/2/28/028.
 * 消息处理器
 *
 * @author acer01
 */
public class MyChannelHandler extends AbstractChannelHandler<String, String> {

    @Override
    protected void onMessageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        ctx.writeAndFlush("\r\n hello " + msg + "\r\n");
    }

}
