package cn.wu.wRpc.rpc;

import java.lang.reflect.Method;

/**
 * 服务提供者，持有服务接口的实现类
 */
public interface Provider<T> {

    Class<T> getInterface();

    Method lookupMethod(String methodName, String methodDesc);

    T getImpl();
}
