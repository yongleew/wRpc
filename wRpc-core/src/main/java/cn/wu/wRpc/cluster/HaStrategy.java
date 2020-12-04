package cn.wu.wRpc.cluster;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;

public interface HaStrategy<T> {

    void setConfig(AbstractConfig<T> config);

    Response call(Request request, LoadBalance<T> loadBalance);
}
