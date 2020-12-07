package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;

import java.util.List;

public abstract class Registry {
    public abstract void register(AbstractConfig<?> config);

    public abstract <T> void subscribe(AbstractConfig<T> config, NotifyListener listener);

    public abstract List<AbstractConfig> discover(AbstractConfig<?> config);
}
