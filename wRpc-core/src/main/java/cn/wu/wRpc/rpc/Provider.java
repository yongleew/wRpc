package cn.wu.wRpc.rpc;

import cn.wu.wRpc.config.AbstractConfig;

import java.lang.reflect.Method;

public interface Provider<T> {
    Class<T> getInterface();

    Method lookupMethod(String name, String methodDesc);

    T getImpl();

    AbstractConfig<T> getConfig();

    public Response invoke(Request request);
}
