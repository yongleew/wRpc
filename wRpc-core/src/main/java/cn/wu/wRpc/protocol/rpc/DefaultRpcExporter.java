package cn.wu.wRpc.protocol.rpc;

import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.URL;
import cn.wu.wRpc.transport.ProviderMessageRouter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRpcExporter<T> implements Exporter<T> {

    protected Provider<T> provider;
    protected final ConcurrentHashMap<String, Object> ipPort2RequestRouter;
    protected final Map<String, Exporter<?>> exporterMap;
    protected URL url;

    public DefaultRpcExporter(Provider<T> provider, URL url, ConcurrentHashMap<String, Object> ipPort2RequestRouter, Map<String, Exporter<?>> exporterMap) {
        this.provider = provider;
        this.url = url;
        this.ipPort2RequestRouter = ipPort2RequestRouter;
        this.exporterMap = exporterMap;

        ProviderMessageRouter requestRouter = initRequestRouter(url);
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

    }
}
