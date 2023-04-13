package com.github.netty.example.server.controller;

import com.github.sseserver.local.SseEmitter;
import com.github.sseserver.local.SseWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/my-sse")
public class MySseWebController extends SseWebController<MyAccessUser> {
    private static final Logger log = LoggerFactory.getLogger(MySseWebController.class);
    @Autowired
    private HttpServletRequest request;

    @Override
    protected Object onMessage(String path, SseEmitter<MyAccessUser> connection, Map<String, Object> message) {
        if ("close".equals(path)) {
            connection.disconnect();
        } else {
            log.info("onMessage path = {}", path);
            try {
                connection.send("server-ack", "onMessage " + path + message)
                        .whenComplete((emitter, throwable) -> {
                            log.info("onMessage path = {} whenComplete", path + message);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    protected Object onUpload(String path, SseEmitter<MyAccessUser> connection, Map<String, Object> message, Collection<Part> files) {
        return super.onUpload(path, connection, message, files);
    }

    @Override
    protected MyAccessUser getAccessUser() {
        // 验证用户
        String token = request.getParameter("access-token");
        MyAccessUser accessUser = new MyAccessUser();
        accessUser.setId(token);
        accessUser.setAccessToken(token);
        accessUser.setTenantId(1);
        return accessUser;
    }

}
