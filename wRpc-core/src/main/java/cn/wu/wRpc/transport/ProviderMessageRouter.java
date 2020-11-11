package cn.wu.wRpc.transport;

import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.DefaultResponse;

import java.util.HashMap;
import java.util.Map;

public class ProviderMessageRouter implements MessageHandler {
    protected Map<String, Provider<?>> providers = new HashMap<>();

    public void addProvider(Provider<?> provider) {
        String configKey = provider.getInterface().getName();
        if (providers.containsKey(configKey)) {
            throw new IllegalStateException("provider alread exist: " + configKey);
        }
        providers.put(configKey, provider);
    }

    @Override
    public Object handle(Object message) {
        Request request = (Request) message;
        String interfaceName = request.getInterfaceName();
        Provider<?> provider = providers.get(interfaceName);
        if (provider == null) {
            IllegalStateException e = new IllegalStateException("provider not exist");
            DefaultResponse defaultResponse = new DefaultResponse();
            defaultResponse.setException(e);
            return defaultResponse;
        }
        return provider.invoke(request);
    }
}
