package cn.wu.wRpc.test;

import cn.wu.wRpc.OriginalRpc;

public class RpcConsumer {
    public static void main(String[] args) {
        HelloService service = OriginalRpc.ref(HelloService.class, "localhost", 8888);
        System.out.println(service.getName());
        service.sayHello("www");
    }
}
