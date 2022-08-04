package com.github.netty.example.server.controller;

import com.github.sseserver.AccessUser;
import com.github.sseserver.SseEmitter;
import com.github.sseserver.SseWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/my-sse")
public class MySseWebController extends SseWebController {
    private static final Logger log = LoggerFactory.getLogger(MySseWebController.class);
    @Autowired
    private HttpServletRequest request;

    @Override
    protected Object onMessage(String path, SseEmitter connection, Map message) {
        return super.onMessage(path, connection, message);
    }

    @Override
    protected Object onUpload(String path, SseEmitter connection, Map message, Collection files) {
        return super.onUpload(path, connection, message, files);
    }

    @Override
    protected AccessUser getAccessUser() {
        // 验证用户
        String token = request.getParameter("access-token");
        MyAccessUser accessUser = new MyAccessUser();
        accessUser.setId(Integer.valueOf(token));
        accessUser.setAccessToken(token);
        return accessUser;
    }

}
