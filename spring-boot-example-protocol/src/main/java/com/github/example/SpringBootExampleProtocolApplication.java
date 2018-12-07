package com.github.example;

import com.github.netty.core.ProtocolsRegister;
import com.github.netty.core.util.ConcurrentReferenceHashMap;
import com.github.netty.register.MqttProtocolsRegister;
import com.github.netty.register.RtspProtocolsRegister;
import com.github.netty.register.mqtt.MqttServerChannelHandler;
import com.github.netty.springboot.EnableNettyServletEmbedded;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Spring动态协议扩展 端口号:10004
 *  适用于多协议开发的场景
 * @author 84215
 */
@EnableNettyServletEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class SpringBootExampleProtocolApplication {

    /**
     * 添加mqtt协议
     * @return
     */
    @Bean
    public ProtocolsRegister mqttProtocolsRegister(){
        return new MqttProtocolsRegister(4096 * 10,new MqttServerChannelHandler(){
            class Topic{
                private String topic;
                private MqttQoS qoS;
                private Channel publishChannel;
                private List<Channel> subscribeChannels;

                public Topic(String topic, MqttQoS qoS,Channel publishChannel, List<Channel> subscribeChannels) {
                    this.topic = topic;
                    this.qoS = qoS;
                    this.publishChannel = publishChannel;
                    this.subscribeChannels = subscribeChannels;
                }
            }

            Map<String,Channel> channelMap = new ConcurrentReferenceHashMap<>();
            List<Topic> topics = new LinkedList<>();

            @Override
            protected void onMessageReceived(ChannelHandlerContext ctx, MqttMessage request) throws Exception {

                MqttFixedHeader responseFixedHeader = null;
                Object responseVariableHeader = null;
                Object responsePayload = null;

                switch (request.fixedHeader().messageType()) {
                    //客户端到服务端的连接请求
                    case CONNECT:{
                        channelMap.put(ctx.channel().toString(),ctx.channel());
                        responseFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                        responseVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED,true);
                        break;
                    }
                    //服务端对连接请求的响应
                    case CONNACK:{

                        break;
                    }
                    //发布消息
                    case PUBLISH:{
                        MqttPublishMessage requestHeader = (MqttPublishMessage)request.variableHeader();
                        responseFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                        responseVariableHeader = MqttMessageIdVariableHeader.from(requestHeader.variableHeader().packetId());
                        break;
                    }
                    //对发布消息的回应
                    case PUBACK:{
                        break;
                    }
                    //收到发布消息（保证传输part1）
                    case PUBREC:{
                        break;
                    }
                    //释放发布消息（保证传输part2）
                    case PUBREL:{
                        break;
                    }
                    //完成发布消息（保证传输part3）
                    case PUBCOMP:{
                        break;
                    }
                    //客户端订阅请求
                    case SUBSCRIBE:{
                        Channel channel = ctx.channel();
                        MqttMessageIdVariableHeader requestHeader = (MqttMessageIdVariableHeader) request.variableHeader();
                        MqttSubscribePayload payload = (MqttSubscribePayload) request.payload();
                        for(MqttTopicSubscription topicSubscription : payload.topicSubscriptions()){
                            topics.add(new Topic(topicSubscription.topicName(),topicSubscription.qualityOfService(),channel,new ArrayList<>()));
                        }
                        responseFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                        responseVariableHeader = MqttMessageIdVariableHeader.from(requestHeader.messageId());
                        break;
                    }
                    //订阅请求的回应
                    case SUBACK:{
                        break;
                    }
                    //停止订阅请求
                    case UNSUBSCRIBE:{
                        break;
                    }
                    //停止订阅请求响应
                    case UNSUBACK:{
                        break;
                    }
                    //Ping请求（保持连接）
                    case PINGREQ:{
                        responseFixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false,MqttQoS.AT_MOST_ONCE, false, 0);
                        break;
                    }
                    //Ping响应
                    case PINGRESP:{
                        break;
                    }
                    //客户端正在断开
                    case DISCONNECT:{
                        break;
                    }
                    default: {
                        //保留字段 0或15 reserved
                        break;
                    }
                }
                MqttMessage response = MqttMessageFactory.newMessage(responseFixedHeader,responseVariableHeader,responsePayload);
                ctx.writeAndFlush(response);
            }
        });
    }

    /**
     * 添加rtsp协议
     * @return
     */
    @Bean
    public ProtocolsRegister rtspProtocolsRegister(){
        return new RtspProtocolsRegister();
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleProtocolApplication.class, args);
	}
}
