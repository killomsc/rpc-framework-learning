package com.killomsc.simple.rpc.framework.register;

import java.util.HashMap;
import java.util.Map;

public class RegisterService {

    /**
     * 已注册的服务列表
     */
    private static Map<String, Object> registerServices = new HashMap<String, Object>();

    /**
     * 注册服务
     * @param key
     * @param service
     */
    public static void register(String key, Object service) {
        registerServices.put(key, service);
    }

    /**
     * 从已注册的列表获取服务
     * @param key
     * @return
     */
    public static Object get(String key) {
        return registerServices.get(key);
    }

}
