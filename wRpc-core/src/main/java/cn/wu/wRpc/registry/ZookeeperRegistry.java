package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.util.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

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
    public <T> void subscribe(AbstractConfig<T> config) {
        subscribeService(config);
        discover(config);
    }

    @Override
    public void discover(AbstractConfig<?> config) {
        String parentPath = ZkUtils.toNodeTypePath(config, "server");
        try {
            List<String> currentChilds = new ArrayList<>();
            if (framework.checkExists().forPath(parentPath) != null) {
                currentChilds = framework.getChildren().forPath(parentPath);
            }
            nodeChildsToConfig(config, parentPath, currentChilds);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void nodeChildsToConfig(AbstractConfig<?> config, String parentPath, List<String> currentChilds) {
        if (currentChilds != null) {
            for (String node : currentChilds) {

            }
        }
    }

    private void subscribeService(AbstractConfig<?> config) {
        String serviceUrl = config.getServiceUrl();
        String nodeTypePath = ZkUtils.toNodeTypePath(config, "client");
        String nodePath = ZkUtils.toNodePath(config, "client");

        try {
            if (framework.checkExists().forPath(nodeTypePath) == null) {
                framework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodeTypePath);
            }
            framework.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath, serviceUrl.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
