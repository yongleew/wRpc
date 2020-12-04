package cn.wu.wRpc.cluster;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;

import java.util.List;

public interface Cluster<T> {
    void init();

    void setConfig(AbstractConfig<T> config);

    void setLoadBalance(LoadBalance<T> loadBalance);

    void setHaStrategy(HaStrategy<T> haStrategy);

    void onRefresh(List<Referer<T>> referers);

    List<Referer<T>> getReferers();

    LoadBalance<T> getLoadBalance();

    Response call(Request request);
}
