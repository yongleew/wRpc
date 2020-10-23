package cn.wu.wRpc.protocol;


import cn.wu.wRpc.exception.FrameworkWRpcException;
import cn.wu.wRpc.protocol.rpc.DefaultRpcExporter;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Protocol;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WRpcProtocol implements Protocol {

    private ConcurrentHashMap<String, Object> ipPort2RequestRouter = new ConcurrentHashMap<>();


    private Map<String, Exporter<?>> exporterMap = new HashMap<>();

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL url) {
        if (url == null) {
            throw new FrameworkWRpcException("WRpcProtocol export error: url is null");
        }
        if (provider == null) {
            throw new FrameworkWRpcException("WRpcProtocol export error: provider is null");
        }
        String protocolKey = url.toString();
        synchronized (exporterMap) {
            Exporter<T> exporter = (Exporter<T>) exporterMap.get(protocolKey);
            if (exporter != null) {
                throw new FrameworkWRpcException("WRpcProtocol export error : service already exist, url=" + url);
            }

            exporter = createExporter(provider, url);

            exporterMap.put(protocolKey, exporter);

            return exporter;
        }
    }

    private <T> Exporter<T> createExporter(Provider<T> provider, URL url) {
        return new DefaultRpcExporter<T>(provider, url, ipPort2RequestRouter, exporterMap);
    }
}
