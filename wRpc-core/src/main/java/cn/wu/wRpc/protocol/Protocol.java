package cn.wu.wRpc.protocol;

import cn.wu.wRpc.config.AbstractConfig;

public interface Protocol {
    <T> void export(AbstractConfig<T> config);
}
