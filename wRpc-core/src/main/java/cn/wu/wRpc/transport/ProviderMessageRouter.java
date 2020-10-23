package cn.wu.wRpc.transport;

import cn.wu.wRpc.exception.FrameworkWRpcException;
import cn.wu.wRpc.rpc.Provider;
import cn.wu.wRpc.rpc.URL;

import java.util.HashMap;
import java.util.Map;

public class ProviderMessageRouter implements MessageHandler {

    private Map<String, Provider<?>> providers = new HashMap<>();

    @Override
    public Object handle(Channel channel, Object message) {

        return null;
    }

    public void addProvider(Provider<?> provider) {
        URL url = provider.getUrl();
        String serviceKey= url.getPath();
        if (providers.containsKey(serviceKey)) {
            throw new FrameworkWRpcException("provider already exist");
        }
        providers.put(serviceKey, provider);
    }
}
