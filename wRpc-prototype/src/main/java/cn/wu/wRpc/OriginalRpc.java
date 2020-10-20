package cn.wu.wRpc;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 参考自：https://www.iteye.com/blog/javatar-1123915，https://www.cnkirito.moe/easy-know-rpc/
 *
 * 1. socket IO完成service和client的通信
 * 2. ObjectStream完成参数的序列化
 * 3. jdk动态代理完成方法的远程调用
 *
 */
public class OriginalRpc {

    public static void export(final Object service, final int port) throws Exception {
        if (service == null)
            throw new IllegalArgumentException("service instance == null");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port" + port);
        System.out.println("Export service" + service.getClass().getName() + " on port " + port);

        ServerSocket server = new ServerSocket(port);
        while (true) {
            //server socket监听客户端请求
            Socket sc = server.accept();
            new Thread(() -> {
                try (ObjectInputStream objInput = new ObjectInputStream(sc.getInputStream())) {
                    String methodName = objInput.readUTF();
                    Class<?>[] parameterTypes = null;
                    Object[] arguments = null;
                    //处理方法参数为空的情况
                    try {
                        parameterTypes = (Class<?>[]) objInput.readObject();
                        arguments = (Object[]) objInput.readObject();
                    } catch (EOFException e) {
                        //ignore
                    }
                    ObjectOutputStream objOutput = new ObjectOutputStream(sc.getOutputStream());
                    try {
                        Method method = service.getClass().getMethod(methodName, parameterTypes);
                        Object result = method.invoke(service, arguments);
                        objOutput.writeObject(result);
                    } catch (Throwable e) {
                        objOutput.writeObject(e);
                    } finally {
                        objOutput.close();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static <T> T ref(final Class<T> interfaceClass, final String host, final int port) {
        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (!interfaceClass.isInterface())
            throw new IllegalArgumentException("The" + interfaceClass.getName() + "must be interface class!");
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("Host == null!");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port" + port);

        System.out.println("Get remote service" + interfaceClass.getName() + "from server" + host + ":" + port);

        Object o = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
            //socket 发出请求
            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream objOutput = new ObjectOutputStream(socket.getOutputStream())) {
                objOutput.writeUTF(method.getName());
                objOutput.writeObject(method.getParameterTypes());
                objOutput.writeObject(args);
                try (ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream())) {
                    Object result = objInput.readObject();
                    if (result == null) {
                        return null;
                    }
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                }
            }

        });
        return (T) o;
    }
}
