package cn.wu.wrpc;

import cn.wu.wRpc.config.ServiceConfig;

public class RpcServer {
    public static void main(String[] args) {
        ServiceConfig<HelloService> config = new ServiceConfig<>();
        config.setPort(8888);
        config.setInterfaceClass(HelloService.class);
        config.setRef(new HelloServiceImpl());
        config.export();
    }
}
