package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.URL;

public class NettyEndpointFactory extends AbstractEndpointFactory {


    @Override
    protected Server doCreateServer(URL url, MessageHandler messageHandler) {
        return new NettyServer(url, messageHandler);
    }
}
