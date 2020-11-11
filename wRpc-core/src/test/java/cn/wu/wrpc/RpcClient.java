package cn.wu.wrpc;

import cn.wu.wRpc.config.RefererConfig;

public class RpcClient {
    public static void main(String[] args) {
        RefererConfig<HelloService> refererConfig = new RefererConfig<>();
        refererConfig.setHost("127.0.0.1");
        refererConfig.setPort(8888);
        refererConfig.setInterfaceClass(HelloService.class);
        HelloService ref = refererConfig.getRef();
        System.out.println(ref.hello("wyl"));
    }
}
