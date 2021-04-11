package com.killomsc.simple.rpc.framework.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class RpcRequest implements Serializable {

    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数的类型
     */
    private Class<?>[] paramTypes;
    /**
     * 具体的参数
     */
    private Object[] parameters;

}
