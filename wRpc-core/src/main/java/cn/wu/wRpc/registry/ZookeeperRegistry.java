package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.config.ServiceConfig;
import cn.wu.wRpc.util.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperRegistry extends Registry {

    private String registryUrl;
    private CuratorFramework framework;

    public ZookeeperRegistry(String registryUrl, CuratorFramework framework) {
        this.registryUrl = registryUrl;
        this.framework = framework;
        this.framework.start();
    }

    @Override
    public void register(AbstractConfig<?> config) {
        String serviceUrl = config.getServiceUrl();
        String nodeTypePath = ZkUtils.toNodeTypePath(config, "server");
        String nodePath = ZkUtils.toNodePath(config, "server");
        try {
            if (framework.checkExists().forPath(nodeTypePath) == null) {
                framework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodeTypePath);
            }
            framework.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath, serviceUrl.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public <T> void subscribe(AbstractConfig<T> config, NotifyListener listener) {
        subscribeService(config, listener);
        List<AbstractConfig> services = discover(config);
        if (!services.isEmpty()) {
            listener.notify(config, services);
        }

    }

    @Override
    public List<AbstractConfig> discover(AbstractConfig<?> config) {
        String parentPath = ZkUtils.toNodeTypePath(config, "server");
        try {
            List<String> currentChilds = new ArrayList<>();
            if (framework.checkExists().forPath(parentPath) != null) {
                currentChilds = framework.getChildren().forPath(parentPath);
            }
            return nodeChildsToConfig(config, parentPath, currentChilds);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<AbstractConfig> nodeChildsToConfig(AbstractConfig<?> config, String parentPath, List<String> currentChilds) {
        List<AbstractConfig> configs = new ArrayList<>();
        if (currentChilds != null) {
            for (String node : currentChilds) {

                try {
                    byte[] data = framework.getData().forPath(parentPath + node);
                    URL url = new URL(new String(data));
                    ServiceConfig serviceConfig = new ServiceConfig();
                    serviceConfig.setHost(url.getHost());
                    serviceConfig.setPort(url.getPort());
                    serviceConfig.setInterfaceClass(config.getInterfaceClass());
                    configs.add(serviceConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return configs;
    }

    private void subscribeService(AbstractConfig<?> config, NotifyListener listener) {
        String serviceUrl = config.getServiceUrl();
        String nodeTypePath = ZkUtils.toNodeTypePath(config, "client");
        String nodePath = ZkUtils.toNodePath(config, "client");

        try {
            if (framework.checkExists().forPath(nodeTypePath) == null) {
                framework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodeTypePath);
            }
            framework.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath, serviceUrl.getBytes());
            //监听子节点
            String pPath = ZkUtils.toNodeTypePath(config, "server");
            CuratorCache cache = CuratorCache.builder(framework, pPath)
                    .build();
            CuratorCacheListener childrenListener = CuratorCacheListener.builder()
                    .forPathChildrenCache(framework, new PathChildrenCacheListener() {
                        @Override
                        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                            List<String> pps = client.getChildren().forPath(pPath);
                            List<AbstractConfig> serviceConfig = nodeChildsToConfig(config, pPath, pps);
                            listener.notify(config, serviceConfig);
                        }
                    }).build();
            cache.listenable().addListener(childrenListener);
            cache.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
