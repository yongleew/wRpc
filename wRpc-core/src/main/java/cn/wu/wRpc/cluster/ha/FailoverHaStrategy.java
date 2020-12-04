package cn.wu.wRpc.cluster.ha;

import cn.wu.wRpc.cluster.LoadBalance;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;

import java.util.ArrayList;
import java.util.List;

public class FailoverHaStrategy<T> extends AbstractHaStrategy<T> {

    protected ThreadLocal<List<Referer<T>>> refersHoder = new ThreadLocal<List<Referer<T>>>() {
        @Override
        protected List<Referer<T>> initialValue() {
            return new ArrayList<>();
        }
    };

    @Override
    public Response call(Request request, LoadBalance<T> loadBalance) {
        List<Referer<T>> referers = selectReferers(request, loadBalance);
        if (referers.isEmpty()) {
            throw new IllegalStateException("failover HaStrategy no referer");
        }
        int tryCount = 3;
        for (int i = 0; i <= tryCount; i++) {
            Referer<T> referer = referers.get(i);
            try {
                return referer.call(request);
            } catch (Exception e) {
                if (i >= tryCount) {
                    throw e;
                }
            }
        }
        throw new IllegalStateException("failover HaStrategy should not come here");

    }

    protected List<Referer<T>> selectReferers(Request request, LoadBalance<T> loadBalance) {
        List<Referer<T>> referers = refersHoder.get();
        referers.clear();
        loadBalance.selectToHolder(request, referers);
        return referers;
    }
}
