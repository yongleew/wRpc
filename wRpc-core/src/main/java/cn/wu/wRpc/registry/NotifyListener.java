package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;

import java.util.List;

public interface NotifyListener {

    void notify(AbstractConfig registryConfig, List<AbstractConfig> configs);
}
