package com.github.example.protocol;

import com.github.netty.core.AbstractProtocolsRegister;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2019/2/28/028.
 * 私有协议注册
 *
 * @author wangzihao
 */
public class MyProtocolsRegister extends AbstractProtocolsRegister {
    //协议头
    public static final byte[] PROTOCOL_HEADER = {
            'M', 'Y',
            'H', 'E', 'A', 'D', 'E', 'R'
    };
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getProtocolName() {
        return "my-protocol";
    }

    /**
     * 第一个消息决定,该连接以后传输的协议
     *
     * @param msg 消息
     * @return 是否支持此消息类型
     */
    @Override
    public boolean canSupport(ByteBuf msg) {
        if (msg.readableBytes() < PROTOCOL_HEADER.length) {
            return false;
        }
        for (int i = 0; i < PROTOCOL_HEADER.length; i++) {
            if (msg.getByte(i) != PROTOCOL_HEADER[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void registerTo(Channel channel) throws Exception {
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        channel.pipeline().addLast(new StringDecoder());
        channel.pipeline().addLast(new StringEncoder());
        channel.pipeline().addLast(new MyChannelHandler());
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void onServerStart() throws Exception {
        logger.info("私有协议启动!");
    }

    @Override
    public void onServerStop() throws Exception {
        logger.info("私有协议停止!");
    }
}
