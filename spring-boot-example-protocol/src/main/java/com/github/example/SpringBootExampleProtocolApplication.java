package com.github.example;

import com.github.netty.core.ProtocolsRegister;
import com.github.netty.register.MqttProtocolsRegister;
import com.github.netty.register.RtspProtocolsRegister;
import com.github.netty.springboot.EnableNettyServletEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
        return new MqttProtocolsRegister();
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
