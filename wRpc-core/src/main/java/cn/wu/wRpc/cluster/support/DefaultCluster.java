package cn.wu.wRpc.cluster.support;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.cluster.HaStrategy;
import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Referer;

import java.util.List;

public class DefaultCluster<T> implements Cluster<T> {

    @Override
    public void init() {

    }

    @Override
    public void setConfig(AbstractConfig<T> config) {

    }

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {

    }

    @Override
    public void setHaStrategy(HaStrategy<T> haStrategy) {

    }

    @Override
    public void onRefresh(List<Referer<T>> referers) {

    }

    @Override
    public List<Referer<T>> getReferers() {
        return null;
    }

    @Override
    public LoadBalance<T> getLoadBalance() {
        return null;
    }
}
