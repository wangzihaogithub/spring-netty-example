package com.github.netty.example.server.controller;

import com.github.sseserver.AccessUser;
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
import java.util.Map;

@RestController
@RequestMapping("/my-websocket")
public class MySseWebController extends SseWebController {
    private static final Logger log = LoggerFactory.getLogger(MySseWebController.class);
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/message")
    public Object message(@RequestParam Map query, @RequestBody(required = false) Map body) {
        log.info("收到前端消息. query = {}, body = {}", query, body);
        return query;
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

    @Override
    protected ResponseEntity buildIfConnectVerifyErrorResponse(AccessUser accessUser, Map query, Map body, Long keepaliveTime, HttpServletRequest request) {
        boolean success = accessUser != null && accessUser.getId() != null;
        if (success) {
            return null;
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.setConnection("close");
            ByteArrayResource responseBody = new ByteArrayResource("{\"msg\":\"身份验证失败\"}".getBytes(Charset.forName("utf-8")));
            return new ResponseEntity<>(responseBody, headers, HttpStatus.UNAUTHORIZED);
        }
    }
}
