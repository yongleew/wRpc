package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.URL;

/**
 * 单例
 */
public interface EndpointFactory {
    Server createServer(URL url, MessageHandler messageHandler);
}
