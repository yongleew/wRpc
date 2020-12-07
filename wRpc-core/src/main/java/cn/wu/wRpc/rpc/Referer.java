package cn.wu.wRpc.rpc;

import cn.wu.wRpc.config.AbstractConfig;

public interface Referer<T> {
    Response call(Request request);

    void init();

    AbstractConfig<T> getServiceConfig();
}
