package cn.wu.wRpc.config.handler;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.config.RefererConfig;
import cn.wu.wRpc.protocol.DefaultProtocol;
import cn.wu.wRpc.protocol.DefaultReferer;
import cn.wu.wRpc.proxy.ProxyFactory;
import cn.wu.wRpc.rpc.DefaultProvider;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.Referer;

public class SimpleConfigHandler {

    public <T> Exporter<T> export(AbstractConfig<T> config) {
        DefaultProtocol protocol = DefaultProtocol.SINGLE_WRPC_PROTOCOL;

        Provider<T> provider = new DefaultProvider<>(config);

        Exporter<T> exporter = protocol.export(config, provider);
        exporter.init();
        return exporter;
    }


    public <T> T refer(RefererConfig<T> config) {
        DefaultProtocol protocol = DefaultProtocol.SINGLE_WRPC_PROTOCOL;
        Referer<T> refer = protocol.refer(config);
        return ProxyFactory.getProxy(config, refer);
    }
}
