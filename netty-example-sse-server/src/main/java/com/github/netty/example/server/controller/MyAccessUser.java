package com.github.netty.example.server.controller;

import com.github.sseserver.AccessToken;

/**
 * 当前登录用户
 *
 * @author wangzihao
 */
public class MyAccessUser implements com.github.sseserver.AccessUser, AccessToken {
    private String accessToken;
    private Integer id;

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
