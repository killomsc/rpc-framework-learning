package com.killomsc.simple.rpc.provider;

import com.killomsc.simple.rpc.framework.common.DemoConfig;

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
                String interfaceName = inputStream.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = inputStream.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
                Object[] arguments = (Object[]) inputStream.readObject();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

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


    public static void main(String[] args) throws IOException {
        // 将本地服务，发布成远程服务
        publish();

    }
}
