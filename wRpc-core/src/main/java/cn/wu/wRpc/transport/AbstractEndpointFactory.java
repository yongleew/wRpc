package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.URL;

public abstract class AbstractEndpointFactory implements EndpointFactory {


    @Override
    public Server createServer(URL url, MessageHandler messageHandler) {
        return null;
    }
}
