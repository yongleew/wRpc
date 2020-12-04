package cn.wu.wRpc.cluster.loadbalance;

import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;

import java.util.List;

public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    public static final int MAX_REFERER_COUNT = 10;

    private List<Referer<T>> referers;

    @Override
    public void onRefresh(List<Referer<T>> referers) {
        this.referers = referers;
    }

    @Override
    public Referer<T> select(Request request) {
        if (referers == null || referers.isEmpty()) {
            throw new IllegalStateException(
                    this.getClass().getSimpleName() + " No available referers for call request:" + request);
        }
        Referer<T> ref = null;
        if (referers.size() > 1) {
            ref = doSelect(request);
        } else {
            ref = referers.get(0);
        }
        return ref;
    }

    protected abstract Referer<T> doSelect(Request request);

    protected List<Referer<T>> getReferers() {
        return referers;
    }

    @Override
    public void selectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = this.referers;
        if (referers == null || referers.isEmpty()) {
            throw new IllegalStateException(
                    this.getClass().getSimpleName() + " No available referers for call request:" + request);
        }
        if (referers.size() > 1) {
            doSelectToHolder(request, refersHolder);
        } else {
            refersHolder.add(referers.get(0));
        }
    }

    protected abstract void doSelectToHolder(Request request, List<Referer<T>> refersHolder);

    @Override
    public void setWeightString(String weightString) {

    }
}
