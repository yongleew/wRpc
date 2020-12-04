package cn.wu.wRpc.cluster;

import cn.wu.wRpc.cluster.support.DefaultCluster;

public class ClusterFactory {

    public static <T> Cluster<T> getCluster(String name) {
        return new DefaultCluster<>();
    }

    public static <T> HaStrategy<T> getHaStrategy(String name) {
        if (name.equals("activeWeight")) {

        }
        return null;
    }

    public static <T> LoadBalance<T> getLoadBalance(String name) {
        return null;
    }
}
