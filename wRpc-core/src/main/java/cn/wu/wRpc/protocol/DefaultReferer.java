package cn.wu.wRpc.protocol;

import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.Referer;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.transport.NettyClient;
import cn.wu.wRpc.transport.NettyEndpointFactory;

public class DefaultReferer<T> implements Referer<T> {
    private AbstractConfig<T> config;
    private NettyClient client;

    public DefaultReferer(AbstractConfig<T> config) {
        this.config = config;
        this.client = NettyEndpointFactory.createClient(config);
    }

    @Override
    public Response call(Request request) {
        return client.request(request);
    }

    @Override
    public void init() {
        client.open();
        //todo 连接池
        //initPool();
    }

    @Override
    public AbstractConfig<T> getServiceConfig() {
        return config;
    }
}
