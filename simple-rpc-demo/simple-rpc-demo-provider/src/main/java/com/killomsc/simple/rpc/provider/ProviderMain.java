package com.killomsc.simple.rpc.provider;

import com.killomsc.simple.rpc.api.UserService;
import com.killomsc.simple.rpc.framework.common.DemoConfig;
import com.killomsc.simple.rpc.framework.model.RpcRequest;
import com.killomsc.simple.rpc.framework.register.RegisterService;
import com.killomsc.simple.rpc.provider.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ProviderMain {

    private static Executor executor = Executors.newFixedThreadPool(10);

    private static class DealRpcRequest implements Runnable {

        Socket client = null;

        public DealRpcRequest(Socket client) {
            this.client = client;
        }

        public void run() {
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try {
                inputStream = new ObjectInputStream(client.getInputStream());
                outputStream = new ObjectOutputStream(client.getOutputStream());

                RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
                Object service = RegisterService.get(rpcRequest.getClassName());
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object result = method.invoke(service, rpcRequest.getParameters());
                outputStream.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    /**
     * 提供者发布远程调用服务
     * @throws IOException
     */
    public static void publish() throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(DemoConfig.RegisterIp, DemoConfig.RegisterPort));
        System.out.println(String.format("bind:%s:%s", DemoConfig.RegisterIp, DemoConfig.RegisterPort));
        try {
            while (true) {
                executor.execute(new DealRpcRequest(socket.accept()));
            }
        } finally {
            socket.close();
        }

    }

    /**
     * 注册服务
     */
    private static void register() {
        UserService userService = new UserServiceImpl();
        RegisterService.register(UserService.class.getName(), userService);
    }


    public static void main(String[] args) throws IOException {

        // 注册
        register();

        // 将本地服务，发布成远程服务
        publish();

    }
}
