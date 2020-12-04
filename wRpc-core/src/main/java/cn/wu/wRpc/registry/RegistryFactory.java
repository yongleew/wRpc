package cn.wu.wRpc.registry;

import cn.wu.wRpc.config.AbstractConfig;


public interface RegistryFactory {

    Registry getRegistry(AbstractConfig<?> config);
}
