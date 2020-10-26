package cn.wu.wRpc.protocol.rpc;

import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.URL;
import cn.wu.wRpc.transport.*;
import cn.wu.wRpc.transport.netty.NettyEndpointFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRpcExporter<T> implements Exporter<T> {

    protected Provider<T> provider;
    protected final ConcurrentHashMap<String, Object> ipPort2RequestRouter;
    protected final Map<String, Exporter<?>> exporterMap;
    protected URL url;
    private Server server;

    protected volatile boolean init = false;

    public DefaultRpcExporter(Provider<T> provider, URL url, ConcurrentHashMap<String, Object> ipPort2RequestRouter, Map<String, Exporter<?>> exporterMap) {
        this.provider = provider;
        this.url = url;
        this.ipPort2RequestRouter = ipPort2RequestRouter;
        this.exporterMap = exporterMap;
        //暴露在同一端口的服务，都存储在ProviderMessageRouter中
        //ProviderMessageRouter完成同一端中服务的路由
        ProviderMessageRouter requestRouter = initRequestRouter(url);

        EndpointFactory endpointfactory = NettyEndpointFactory.NETTY_ENDPOINTFACTORY;
        this.server = endpointfactory.createServer(url, requestRouter);
    }

    private ProviderMessageRouter initRequestRouter(URL url) {
        return null;
    }

    @Override
    public Provider<T> getProvider() {
        return this.provider;
    }

    @Override
    public void init() {
        if (init) {
            return;
        }
        boolean result = doInit();
        if (result) {
            init = true;
        }
    }

    private boolean doInit() {
        return server.open();
    }


}
