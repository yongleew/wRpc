package cn.wu.wRpc.transport;

import cn.wu.wRpc.config.AbstractConfig;

import java.util.HashMap;
import java.util.Map;

public class NettyEndpointFactory {

    private static Map<String, NettyServer> ipPort2ShareChannel = new HashMap<>();

    public static NettyServer createServer(AbstractConfig<?> config, ProviderMessageRouter requestRouter) {
        String ipPortStr = config.getIpPortStr();
        NettyServer nettyServer = ipPort2ShareChannel.get(ipPortStr);
        if (nettyServer == null) {
            nettyServer = new NettyServer(config, requestRouter);
            ipPort2ShareChannel.put(ipPortStr, nettyServer);
        }
        return nettyServer;
    }

    public static NettyClient createClient(AbstractConfig<?> config) {
        return new NettyClient(config);
    }
}
