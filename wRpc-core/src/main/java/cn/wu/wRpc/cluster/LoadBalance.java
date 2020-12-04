package cn.wu.wRpc.cluster;

import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;

import java.util.List;

public interface LoadBalance<T> {

    void onRefresh(List<Referer<T>> referers);

    Referer<T> select(Request request);

    void selectToHolder(Request request, List<Referer<T>> refersHolder);

    void setWeightString(String weightString);
}
