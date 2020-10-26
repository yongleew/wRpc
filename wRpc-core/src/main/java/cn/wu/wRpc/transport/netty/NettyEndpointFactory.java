package cn.wu.wRpc.transport.netty;

import cn.wu.wRpc.rpc.URL;
import cn.wu.wRpc.transport.AbstractEndpointFactory;
import cn.wu.wRpc.transport.EndpointFactory;
import cn.wu.wRpc.transport.MessageHandler;
import cn.wu.wRpc.transport.Server;

public class NettyEndpointFactory extends AbstractEndpointFactory {

    public static final EndpointFactory NETTY_ENDPOINTFACTORY = new NettyEndpointFactory();

    @Override
    protected Server doCreateServer(URL url, MessageHandler messageHandler) {
        return new NettyServer(url, messageHandler);
    }


}
