package cn.wu.wRpc.cluster.support;

import cn.wu.wRpc.cluster.Cluster;
import cn.wu.wRpc.cluster.HaStrategy;
import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultCluster<T> implements Cluster<T> {

    private LoadBalance<T> loadBalance;
    private HaStrategy<T> haStrategy;
    private List<Referer<T>> referers;
    private AbstractConfig<T> config;

    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    @Override
    public void init() {
        onRefresh(referers);
    }

    @Override
    public void setConfig(AbstractConfig<T> config) {
        this.config = config;
    }

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public void setHaStrategy(HaStrategy<T> haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public synchronized void onRefresh(List<Referer<T>> referers) {
        if (referers == null || referers.isEmpty()) {
            return;
        }
        loadBalance.onRefresh(referers);
        List<Referer<T>> oldReferers = this.referers;
        this.referers = referers;
        haStrategy.setConfig(this.config);
        if (oldReferers == null || oldReferers.isEmpty()) {
            return;
        }

        List<Referer<T>> delayDestroyRefs = new ArrayList<>();
        for (Referer<T> oldReferer : oldReferers) {
            if (referers.contains(oldReferer)) {
                continue;
            }
            delayDestroyRefs.add(oldReferer);
        }

        if (!delayDestroyRefs.isEmpty()) {
            scheduledExecutor.schedule(() -> {
                for (Referer<T> referer : referers) {
                    //referer.destroy();
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public List<Referer<T>> getReferers() {
        return referers;
    }

    @Override
    public LoadBalance<T> getLoadBalance() {
        return loadBalance;
    }

    @Override
    public Response call(Request request) {
        return haStrategy.call(request, loadBalance);
    }
}
