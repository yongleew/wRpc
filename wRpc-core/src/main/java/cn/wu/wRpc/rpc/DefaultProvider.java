package cn.wu.wRpc.rpc;

import cn.wu.wRpc.config.AbstractConfig;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Data
public class DefaultProvider<T> implements Provider<T> {
    private Class<T> clz;
    private Map<String, Method> methodMap = new HashMap<>();
    private T ref;
    private AbstractConfig<T> config;

    public DefaultProvider(AbstractConfig<T> config) {
        clz = config.getInterfaceClass();
        ref = config.getRef();
        this.config = config;
        initMethodMap(clz);
    }

    private void initMethodMap(Class<T> clz) {
        Method[] methods = clz.getMethods();
        //ArrayList<String> methodDescs = new ArrayList<>();
        for (Method method : methods) {
            String methodDesc = getMethodDesc(method);
            methodMap.put(methodDesc, method);
        }
    }

    private String getMethodDesc(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName())
                .append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                builder.append(parameterType.getName())
                        .append(",");
            }
            builder.delete(builder.length() -1, builder.length());
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public Class<T> getInterface() {
        return clz;
    }

    @Override
    public Method lookupMethod(String methodName, String methodDesc) {
        String fullMethodName;
        if (methodDesc == null) {
            fullMethodName = methodName + "()";
        } else {
            fullMethodName = methodName + "(" + methodDesc + ")";
        }
        return methodMap.get(fullMethodName);
    }

    @Override
    public T getImpl() {
        return ref;
    }

    @Override
    public AbstractConfig<T> getConfig() {
        return config;
    }

    @Override
    public Response invoke(Request request) {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.setRequestId(request.getRequestId());
        Method method = lookupMethod(request.getMethodName(), request.getParamtersDesc());
        if (method == null) {
            Exception e = new Exception("service method not exist");
            defaultResponse.setException(e);
            return defaultResponse;
        }
        try {
            Object value = method.invoke(ref, request.getArguments());
            defaultResponse.setValue(value);
        } catch (Exception e) {
            if (e.getCause() != null) {
                defaultResponse.setException(new Exception("provider call process error", e.getCause()));
            } else {
                defaultResponse.setException(new Exception("provider call process error", e));
            }
        }
        return defaultResponse;
    }

}
