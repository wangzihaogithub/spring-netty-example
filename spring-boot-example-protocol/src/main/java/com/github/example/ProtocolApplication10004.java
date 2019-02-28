package com.github.example;

import com.github.example.protocol.MyProtocolsRegister;
import com.github.netty.core.ProtocolsRegister;
import com.github.netty.protocol.MqttProtocolsRegister;
import com.github.netty.springboot.EnableNettyServletEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring动态协议扩展 端口号:10004
 *  适用于多协议开发的场景
 * @author wangzihao
 */
@EnableNettyServletEmbedded//这里需要切换为netty容器
@SpringBootApplication
public class ProtocolApplication10004 {

    /**
     * 添加私有协议
     * @return
     */
    @Bean
    public MyProtocolsRegister myProtocolsRegister(){
        return new MyProtocolsRegister();
    }

    /**
     * 添加mqtt协议
     * @return
     */
    @Bean
    public ProtocolsRegister mqttProtocolsRegister(){
        return new MqttProtocolsRegister();
    }

	public static void main(String[] args) {
		SpringApplication.run(ProtocolApplication10004.class, args);
	}
}
