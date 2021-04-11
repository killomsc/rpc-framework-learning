package com.killomsc.simple.rpc.invoke;

import com.killomsc.simple.rpc.framework.common.DemoConfig;
import com.killomsc.simple.rpc.framework.model.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class ConsumerHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try (Socket socket = new Socket(DemoConfig.RegisterIp, DemoConfig.RegisterPort);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            RpcRequest request = RpcRequest.builder()
                    .className(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .paramTypes(method.getParameterTypes())
                    .parameters(args).build();

            outputStream.writeObject(request);
            Object result = inputStream.readObject();
            return result;
        } catch (Exception e) {
            //log.error("consumer invoke error", e);
            throw new RuntimeException("consumer invoke error");
        }
    }

}
