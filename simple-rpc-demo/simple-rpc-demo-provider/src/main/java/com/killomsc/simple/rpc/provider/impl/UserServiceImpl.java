package com.killomsc.simple.rpc.provider.impl;

import com.killomsc.simple.rpc.api.UserService;


public class UserServiceImpl implements UserService {

    public String getUserInfo(String userId) {
        return "用户信息:" + userId;
    }

}
