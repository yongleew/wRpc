package cn.wu.wRpc.protocol;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.transport.NettyEndpointFactory;
import cn.wu.wRpc.transport.NettyServer;
import cn.wu.wRpc.transport.ProviderMessageRouter;

import java.lang.ref.PhantomReference;
import java.util.Map;


public class DefaultExporter<T> implements Exporter<T> {

    private Map<String, ProviderMessageRouter> ipPort2RequestRouter;
    private Provider<T> provider;
    private AbstractConfig<T> config;
    private NettyServer server;

    public DefaultExporter(Map<String, ProviderMessageRouter> ipPort2RequestRouter, Provider<T> provider, AbstractConfig<T> config) {
        this.ipPort2RequestRouter = ipPort2RequestRouter;
        this.provider = provider;
        this.config = config;

        ProviderMessageRouter requestRouter = initRequestRouter(config.getIpPortStr());
        server = NettyEndpointFactory.createServer(config, requestRouter);
    }


    private ProviderMessageRouter initRequestRouter(String ipPortStr) {
        ProviderMessageRouter router = ipPort2RequestRouter.get(ipPortStr);
        if (router == null) {
            router = new ProviderMessageRouter();
            ipPort2RequestRouter.put(ipPortStr, router);
        }
        router.addProvider(provider);
        return router;
    }


    @Override
    public void init() {
        server.open();
    }
}
