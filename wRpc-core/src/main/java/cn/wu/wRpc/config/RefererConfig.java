package cn.wu.wRpc.config;

import cn.wu.wRpc.cluster.support.ClusterSupport;
import cn.wu.wRpc.config.handler.SimpleConfigHandler;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class RefererConfig<T> extends AbstractConfig<T> {

    private static final String DEFAULT_HASTRATEGY = "failover";
    private static final String DEFAULT_LOADBALANCE = "activeWeight";
    public String cluster = "default";

    private String haStrategy = DEFAULT_HASTRATEGY;
    private String loadBalance = DEFAULT_LOADBALANCE;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    public T getRef() {
        if (ref == null) {
            initRef();
        }
        return ref;
    }

    private void initRef() {
        if (initialized.get()) {
            return;
        }
        if (interfaceClass == null || !interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass invalid");
        }
        SimpleConfigHandler configHandler = new SimpleConfigHandler();

        ClusterSupport<T> clusterSupport = createClusterSupport(this, configHandler);

        ref = configHandler.refer(this, clusterSupport.getCluster());
    }

    private ClusterSupport<T> createClusterSupport(RefererConfig<T> config, SimpleConfigHandler configHandler) {
        if (config.getRegistryAddress() == null) {
            throw new IllegalStateException("No registry to reference");
        }

        return configHandler.buildClusterSupport(this);
    }

}
