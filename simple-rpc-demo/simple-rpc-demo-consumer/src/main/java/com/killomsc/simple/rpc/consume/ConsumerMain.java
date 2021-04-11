package com.killomsc.simple.rpc.consume;

import com.killomsc.simple.rpc.api.UserService;
import com.killomsc.simple.rpc.framework.common.DemoConfig;
import com.killomsc.simple.rpc.framework.model.RpcRequest;
import com.killomsc.simple.rpc.invoke.ConsumerHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ConsumerMain {

    private static <T> T getRemoteService(Class<T> cls) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new ConsumerHandler());
    }

    public static void main(String[] args) {
        UserService remoteService = getRemoteService(UserService.class);
        System.out.println(remoteService.getUserInfo("1001"));
    }
}
