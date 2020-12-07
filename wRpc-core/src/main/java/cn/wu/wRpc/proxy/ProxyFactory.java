package cn.wu.wRpc.proxy;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.config.RefererConfig;
import cn.wu.wRpc.rpc.Referer;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T getProxy(RefererConfig<T> config, Referer<T> referer) {
        Class<T> clz = config.getInterfaceClass();
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RefererInvocationHandler(clz, referer));
    }

    public static <T> T getProxy(RefererConfig<T> config, Cluster<T> cluster) {
        Class<T> clz = config.getInterfaceClass();
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RefererInvocationClusterHandler(clz, cluster));
    }
}
