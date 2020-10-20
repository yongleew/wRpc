package cn.wu.wRpc.test;

import cn.wu.wRpc.OriginalRpc;

public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloServiceImpl service = new HelloServiceImpl();
        OriginalRpc.export(service, 8888);
        System.in.read();
    }
}
