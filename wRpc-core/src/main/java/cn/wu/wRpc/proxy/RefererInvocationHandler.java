package cn.wu.wRpc.proxy;

import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.util.RequestIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RefererInvocationHandler implements InvocationHandler {


    private Referer<?> referer;
    private Class<?> clz;


    public <T> RefererInvocationHandler(Class<T> clz, Referer<T> referer) {
        this.clz = clz;
        this.referer = referer;
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
        return invokeRequest(request, method.getReturnType(), referer);
    }

    private Object invokeRequest(Request request, Class<?> returnType, Referer<?> referer) throws Exception {

        Response response = referer.call(request);
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
