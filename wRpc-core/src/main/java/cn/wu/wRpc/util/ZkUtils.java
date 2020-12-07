package cn.wu.wRpc.util;

import cn.wu.wRpc.config.AbstractConfig;

public class ZkUtils {

    private static final String ZOOKEEPER_REGISTRY_NAMESPACE = "/wRpc";

    public static String toNodeTypePath(AbstractConfig<?> config, String nodeType) {
        return ZOOKEEPER_REGISTRY_NAMESPACE + "/" + config.getPath() + "/" + nodeType;
    }

    public static String toNodePath(AbstractConfig<?> config, String nodeType) {
        return toNodeTypePath(config, nodeType) + "/" + config.getIpPortStr();
    }
}
