package com.github.example;

import com.github.example.protocol.MyProtocol;
import com.github.netty.protocol.MqttProtocol;
import com.github.netty.springboot.EnableNettyEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring动态协议扩展 端口号:10004
 * 适用于多协议开发的场景
 *
 * @author wangzihao
 */
@EnableNettyEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class ProtocolApplication10004 {

    /**
     * 添加私有协议
     *
     * @return
     */
    @Bean
    public MyProtocol myProtocol() {
        return new MyProtocol();
    }

    /**
     * 添加mqtt协议
     *
     * @return
     */
    @Bean
    public MqttProtocol mqttProtocolsRegister() {
        return new MqttProtocol();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProtocolApplication10004.class, args);
    }
}
