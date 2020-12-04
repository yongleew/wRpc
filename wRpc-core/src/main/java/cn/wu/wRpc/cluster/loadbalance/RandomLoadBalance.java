package cn.wu.wRpc.cluster.loadbalance;

import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance <T> extends AbstractLoadBalance<T> {

    @Override
    protected Referer<T> doSelect(Request request) {
        List<Referer<T>> referers = this.getReferers();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * referers.size());

        for (int i = 0; i < referers.size(); i++) {
            Referer<T> ref = referers.get((i + idx) % referers.size());
            //todo 验证是否有效
            return ref;
        }
        return null;
    }

    @Override
    protected void doSelectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = this.getReferers();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * referers.size());

        for (int i = 0; i < referers.size(); i++) {
            Referer<T> ref = referers.get((i + idx) % referers.size());
            //todo 验证是否有效
            refersHolder.add(ref);
        }
    }
}
