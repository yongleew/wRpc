package cn.wu.wRpc.cluster.support;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.cluster.ClusterFactory;
import cn.wu.wRpc.cluster.HaStrategy;
import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.cluster.ha.FailoverHaStrategy;
import cn.wu.wRpc.cluster.loadbalance.RandomLoadBalance;
import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.config.RefererConfig;
import cn.wu.wRpc.protocol.DefaultProtocol;
import cn.wu.wRpc.registry.NotifyListener;
import cn.wu.wRpc.registry.Registry;
import cn.wu.wRpc.registry.RegistryFactory;
import cn.wu.wRpc.registry.ZookeeperRegistryFactory;
import cn.wu.wRpc.rpc.Referer;

import java.util.ArrayList;
import java.util.List;

public class ClusterSupport<T> implements NotifyListener {
    private RefererConfig<T> config;
    private Cluster<T> cluster;
    private DefaultProtocol protocol;

    private List<Referer<T>> registryReferers = new ArrayList<>();


    public ClusterSupport(RefererConfig<T> config) {
        this.protocol = DefaultProtocol.SINGLE_WRPC_PROTOCOL;
        this.config = config;
    }

    public Cluster<T> getCluster() {
        return cluster;
    }

    public void init() {
        prepareCluster();

        Registry registry = getRegistry();

        registry.subscribe(config, this);
    }

    private Registry getRegistry() {
        RegistryFactory registryFactory = ZookeeperRegistryFactory.REGISTRY_FACTORY;
        return registryFactory.getRegistry(config);
    }

    private void prepareCluster() {
        cluster = ClusterFactory.getCluster(config.getCluster());
        //LoadBalance<T> loadBalance = ClusterFactory.getLoadBalance(config.getLoadBalance());
        LoadBalance<T> loadBalance = new RandomLoadBalance<>();
        //HaStrategy<T> haStrategy = ClusterFactory.getHaStrategy(config.getHaStrategy());
        HaStrategy<T> haStrategy = new FailoverHaStrategy<>();

        cluster.setLoadBalance(loadBalance);
        cluster.setHaStrategy(haStrategy);
        cluster.setConfig(config);
    }

    @Override
    public void notify(AbstractConfig registryConfig, List<AbstractConfig> serviceConfig) {
        List<Referer<T>> newReferers = new ArrayList<>();
        for (AbstractConfig s : serviceConfig) {
            Referer<T> referer = getExistingReferer(s, registryReferers);
            if (referer == null) {
                referer = protocol.refer(s);
            }
            if (referer != null) {
                newReferers.add(referer);
            }
        }
        registryReferers = newReferers;

        refreshCluster();
    }

    private void refreshCluster() {
        cluster.onRefresh(registryReferers);
    }


    private Referer<T> getExistingReferer(AbstractConfig config, List<Referer<T>> referers) {
        if (referers == null) {
            return null;
        }
        for (Referer<T> referer : referers) {
            AbstractConfig<T> serviceConfig = referer.getServiceConfig();
            if (config.getConfigKey().equals(serviceConfig.getConfigKey())) {
                return referer;
            }
        }
        return null;
    }
}
