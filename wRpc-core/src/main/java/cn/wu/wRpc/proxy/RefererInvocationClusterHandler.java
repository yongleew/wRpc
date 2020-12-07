package cn.wu.wRpc.proxy;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.util.RequestIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RefererInvocationClusterHandler implements InvocationHandler {


    private Cluster<?> cluster;
    private Class<?> clz;

    public <T> RefererInvocationClusterHandler(Class<T> clz, Cluster<T> cluster) {
        this.cluster = cluster;
        this.clz = clz;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method)) {
            throw new IllegalStateException("can not invoke local method: " + method.getName());
        }
        Request request = new Request();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        request.setMethodName(method.getName());
        StringBuilder paramsDesc = new StringBuilder();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                paramsDesc.append(parameterType.getName())
                        .append(",");
            }
            paramsDesc.delete(paramsDesc.length() -1, paramsDesc.length());
        }
        request.setParamtersDesc(paramsDesc.toString());
        request.setInterfaceName(clz.getName());
        return invokeRequest(request, method.getReturnType(), cluster);
    }

    private Object invokeRequest(Request request, Class<?> returnType, Cluster<?> cluster) throws Exception {

        Response response = cluster.call(request);
        Object value = response.getValue();
        if (response.getException() != null) {
            throw response.getException();
        }
        return value;
    }

    private boolean isLocalMethod(Method method) {
        if (method.getDeclaringClass().equals(Object.class)) {
            try {
                Method interfaceMethod = clz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return false;
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}
