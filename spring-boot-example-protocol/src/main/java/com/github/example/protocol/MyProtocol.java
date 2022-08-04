package com.github.example.protocol;

import com.github.netty.core.AbstractNettyServer;
import com.github.netty.core.AbstractProtocol;
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
public class MyProtocol extends AbstractProtocol {
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
     * @param clientFirstMsg 消息
     * @return 是否支持此消息类型
     */
    @Override
    public boolean canSupport(ByteBuf clientFirstMsg) {
        if (clientFirstMsg.readableBytes() < PROTOCOL_HEADER.length) {
            return false;
        }
        for (int i = 0; i < PROTOCOL_HEADER.length; i++) {
            if (clientFirstMsg.getByte(i) != PROTOCOL_HEADER[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addPipeline(Channel channel, ByteBuf clientFirstMsg) throws Exception {
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        channel.pipeline().addLast(new StringDecoder());
        channel.pipeline().addLast(new StringEncoder());
        channel.pipeline().addLast(new MyChannelHandler());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public <T extends AbstractNettyServer> void onServerStart(T server) throws Exception {
        logger.info("私有协议启动!");
    }

    @Override
    public <T extends AbstractNettyServer> void onServerStop(T server) throws Exception {
        logger.info("私有协议停止!");
    }

}
