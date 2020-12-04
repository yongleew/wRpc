package cn.wu.wRpc.cluster.support;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.cluster.ClusterFactory;
import cn.wu.wRpc.cluster.HaStrategy;
import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.config.RefererConfig;
import cn.wu.wRpc.registry.Registry;
import cn.wu.wRpc.registry.RegistryFactory;
import cn.wu.wRpc.registry.ZookeeperRegistryFactory;

public class ClusterSupport<T> {
    private RefererConfig<T> config;
    private Cluster<T> cluster;

    public ClusterSupport(RefererConfig<T> config) {
        this.config = config;
    }

    public void init() {
        prepareCluster();

        Registry registry = getRegistry();

        registry.subscribe(config);
    }

    private Registry getRegistry() {
        RegistryFactory registryFactory = ZookeeperRegistryFactory.REGISTRY_FACTORY;
        return registryFactory.getRegistry(config);
    }

    private void prepareCluster() {
        cluster = ClusterFactory.getCluster(config.getCluster());
        LoadBalance<T> loadBalance = ClusterFactory.getLoadBalance(config.getLoadBalance());
        HaStrategy<T> haStrategy = ClusterFactory.getHaStrategy(config.getHaStrategy());

        cluster.setLoadBalance(loadBalance);
        cluster.setHaStrategy(haStrategy);
        cluster.setConfig(config);
    }
}
