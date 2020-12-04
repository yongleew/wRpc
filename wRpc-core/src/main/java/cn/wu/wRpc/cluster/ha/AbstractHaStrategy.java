package cn.wu.wRpc.cluster.ha;

import cn.wu.wRpc.cluster.HaStrategy;
import cn.wu.wRpc.config.AbstractConfig;

public abstract class AbstractHaStrategy<T> implements HaStrategy<T> {

    protected AbstractConfig<T> config;

    @Override
    public void setConfig(AbstractConfig<T> config) {
        this.config = config;
    }
}
