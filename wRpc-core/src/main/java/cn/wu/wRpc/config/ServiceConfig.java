package cn.wu.wRpc.config;


import cn.wu.wRpc.config.handler.SimpleConfigHandler;
import cn.wu.wRpc.rpc.Exporter;
import lombok.Data;

@Data
public class ServiceConfig<T> extends AbstractConfig<T> {


    public Exporter<T> export() {
        if (ref == null || interfaceClass == null) {
            throw new IllegalArgumentException("ref or interfaceClass can't null");
        }
        SimpleConfigHandler configHandler = new SimpleConfigHandler();
        return configHandler.export(this);
    }
}
