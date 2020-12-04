package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;

public abstract class Registry {
    public abstract void register(AbstractConfig<?> config);

    public abstract <T> void subscribe(AbstractConfig<T> config);

    public abstract void discover(AbstractConfig<?> config);
}
