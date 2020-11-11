package cn.wu.wRpc.protocol;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.transport.ProviderMessageRouter;

import java.util.HashMap;
import java.util.Map;

public class DefaultProtocol {

    public static final DefaultProtocol SINGLE_WRPC_PROTOCOL = new DefaultProtocol();

    private Map<String, Exporter<?>> exporterMap = new HashMap<>();
    private Map<String, ProviderMessageRouter> ipPort2RequestRouter = new HashMap<>();

    public <T> Exporter<T> export(AbstractConfig<T> config, Provider<T> provider) {
        String configKey = config.getConfigKey();
        synchronized (exporterMap) {
            Exporter<T> exporter = (Exporter<T>) exporterMap.get(configKey);
            if (exporter != null) {
                throw new IllegalStateException(config.getInterfaceClass().getSimpleName() + " already exist");
            }
            exporter = new DefaultExporter<>(ipPort2RequestRouter, provider, config);
            exporterMap.put(configKey, exporter);
            exporter.init();
            return exporter;
        }
    }

    public <T> Referer<T> refer(AbstractConfig<T> config) {
        Referer<T> referer = new DefaultReferer<>(config);
        referer.init();
        return referer;
    }
}
