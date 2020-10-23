package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.URL;

import java.net.InetSocketAddress;

public class NettyServer implements Server {

    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private URL url;

    private MessageHandler messageHandler;


    public NettyServer(URL url, MessageHandler messageHandler) {
        this.url = url;
        this.messageHandler = messageHandler;
    }
}
