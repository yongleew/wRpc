package cn.wu.wRpc.cluster.loadbalance;

import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance<T> extends AbstractLoadBalance<T> {

    private AtomicInteger idx = new AtomicInteger();

    @Override
    protected Referer<T> doSelect(Request request) {
        List<Referer<T>> referers = this.getReferers();
        int idx = getNonNegative();
        for (int i = 0; i < referers.size(); i++) {
            Referer<T> referer = referers.get((i + idx) % referers.size());
            return referer;
        }
        return null;
    }

    private int getNonNegative() {
        int org = idx.getAndIncrement();
        return 0x7fffffff & org;
    }

    @Override
    protected void doSelectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = this.getReferers();
        int idx = getNonNegative();
        for (int i = 0, count = 0; i < referers.size() && count <= MAX_REFERER_COUNT; i++) {
            Referer<T> referer = referers.get((i + idx) % referers.size());
            refersHolder.add(referer);
            count++;
        }
    }
}
