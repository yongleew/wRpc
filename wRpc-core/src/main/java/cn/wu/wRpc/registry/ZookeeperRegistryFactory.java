package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ConcurrentHashMap;

public class ZookeeperRegistryFactory implements RegistryFactory {

    public static final ZookeeperRegistryFactory REGISTRY_FACTORY = new ZookeeperRegistryFactory();

    private static ConcurrentHashMap<String, Registry> registries = new ConcurrentHashMap<>();

    @Override
    public Registry getRegistry(AbstractConfig<?> config) {
        Registry registry = registries.get(config.getRegistryUri());
        if (registry != null) {
            return registry;
        }
        registry = createRegistry(config.getRegistryAddress());
        registries.put(config.getRegistryAddress(), registry);
        return registry;
    }

    private Registry createRegistry(String registryUrl) {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework framework = CuratorFrameworkFactory.newClient(registryUrl, retry);
        return new ZookeeperRegistry(registryUrl, framework);
    }
}
