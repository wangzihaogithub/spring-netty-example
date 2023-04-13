package com.github.netty.example.server.controller;

import com.github.sseserver.local.LocalConnectionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ServerSender {
    @Resource
    private LocalConnectionService localConnectionService;

    @PostConstruct
    public void init() {
        new ScheduledThreadPoolExecutor(1)
                .scheduleWithFixedDelay(() -> {
                    // 每5秒发送消息
                    List<MyAccessUser> users = localConnectionService.getUsers();
                    for (MyAccessUser user : users) {
                        localConnectionService.sendByUserId(user.getId(),
                                "server-push","{\"name\":\"ServerSender#sendByUserId 服务端推送的\"}" );
                    }
                }, 1, 3, TimeUnit.SECONDS);
    }

}
