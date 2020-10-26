package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.URL;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public abstract class AbstractEndpointFactory implements EndpointFactory {

    /**
     * 记录ipPort到server的映射
     */
    protected Map<String, Server> ipPort2Server = new HashMap<>();
    protected Map<Server, Set<String>> server2Url = new HashMap<>();

    @Override
    public Server createServer(URL url, MessageHandler messageHandler) {
        synchronized (ipPort2Server) {
            String ipPortStr = url.getServerPortStr();
            String protocolKey = url.toString();
            Server server = ipPort2Server.get(ipPortStr);

            if (server == null) {
                url = url.createCopy();
                url.setPath("");
                server = doCreateServer(url, messageHandler);
                ipPort2Server.put(ipPortStr, server);
            }
            saveEndpoint2Urls(server, protocolKey);
            return server;
        }
    }

    protected void saveEndpoint2Urls(Server server, String protocolKey) {
        Set<String> urls = server2Url.get(server);
        if (urls == null) {
            urls = new HashSet<>();
            server2Url.putIfAbsent(server, urls);
            urls = server2Url.get(server);
        }
        urls.add(protocolKey);
    };

    protected abstract Server doCreateServer(URL url, MessageHandler messageHandler);

}
