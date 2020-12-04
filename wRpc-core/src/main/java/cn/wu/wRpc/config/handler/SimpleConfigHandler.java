package cn.wu.wRpc.config.handler;

import cn.wu.wRpc.cluster.support.ClusterSupport;
import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.config.RefererConfig;
import cn.wu.wRpc.protocol.DefaultProtocol;
import cn.wu.wRpc.protocol.DefaultReferer;
import cn.wu.wRpc.proxy.ProxyFactory;
import cn.wu.wRpc.registry.Registry;
import cn.wu.wRpc.registry.RegistryFactory;
import cn.wu.wRpc.registry.ZookeeperRegistryFactory;
import cn.wu.wRpc.rpc.DefaultProvider;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.Referer;

public class SimpleConfigHandler {

    public <T> Exporter<T> export(AbstractConfig<T> config) {
        DefaultProtocol protocol = DefaultProtocol.SINGLE_WRPC_PROTOCOL;

        Provider<T> provider = new DefaultProvider<>(config);

        Exporter<T> exporter = protocol.export(config, provider);
        register(config);
        return exporter;
    }

    private void register(AbstractConfig<?> config) {
        RegistryFactory registryFactory = ZookeeperRegistryFactory.REGISTRY_FACTORY;
        Registry registry = registryFactory.getRegistry(config);
        registry.register(config);
    }


    public <T> T refer(RefererConfig<T> config) {
        DefaultProtocol protocol = DefaultProtocol.SINGLE_WRPC_PROTOCOL;
        Referer<T> refer = protocol.refer(config);
        return ProxyFactory.getProxy(config, refer);
    }

    public <T> ClusterSupport<T> buildClusterSupport( RefererConfig<T> config) {
        ClusterSupport<T> clusterSupport = new ClusterSupport<T>(config);
        clusterSupport.init();

        return clusterSupport;
    }
}
