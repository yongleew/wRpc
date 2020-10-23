package cn.wu.wRpc.config.handler;

import cn.wu.wRpc.protocol.WRpcProtocol;
import cn.wu.wRpc.rpc.*;

import java.util.List;

public class SimpleConfigHandler implements ConfigHandler {

    @Override
    public <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registryUrls, URL serviceUrl) {
        String protocolName = serviceUrl.getProtocol();
        Provider<T> provider = new DefaultProvider<>(ref, interfaceClass, serviceUrl);
        //按协议暴露服务
        Protocol protocol = new WRpcProtocol();
        protocol.export(provider, serviceUrl);
        //想注册中心注册服务
        register(registryUrls, serviceUrl);

        return null;
    }

    private void register(List<URL> registryUrls, URL serviceUrl) {

    }
}
