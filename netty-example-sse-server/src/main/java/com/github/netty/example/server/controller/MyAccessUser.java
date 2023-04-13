package com.github.netty.example.server.controller;

import com.github.sseserver.AccessToken;
import com.github.sseserver.TenantAccessUser;

/**
 * 当前登录用户
 *
 * @author wangzihao
 */
public class MyAccessUser implements com.github.sseserver.AccessUser, AccessToken, TenantAccessUser {
    private String accessToken;
    private String id;
    private Integer tenantId;

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}
