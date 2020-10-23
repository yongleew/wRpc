package cn.wu.wRpc.rpc;

import java.lang.reflect.Method;

public class DefaultProvider<T> implements Provider<T>  {
    private T ref;
    private Class<T> interfaceClass;
    private URL url;

    public DefaultProvider(T ref, Class<T> interfaceClass, URL url) {
        this.ref = ref;
        this.interfaceClass = interfaceClass;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public Method lookupMethod(String methodName, String methodDesc) {
        return null;
    }

    @Override
    public T getImpl() {
        return ref;
    }
}
