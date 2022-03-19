package com.github.netty.example.server.controller;

import com.github.sseserver.LocalConnectionService;
import com.github.sseserver.SseEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ServerSender {
    @Autowired
    private LocalConnectionService localConnectionService;

    @PostConstruct
    public void init() {
        new ScheduledThreadPoolExecutor(1)
                .scheduleWithFixedDelay(() -> {
                    // 每秒发送消息
                    List<MyAccessUser> users = localConnectionService.getUsers();
                    for (MyAccessUser user : users) {
                        localConnectionService.sendByUserId(user.getId(), SseEmitter.event()
                                .name("event1")
                                .data("{\"name\":\"服务端推送的\"}")
                        );
                    }
                }, 1, 1, TimeUnit.SECONDS);
    }

}
