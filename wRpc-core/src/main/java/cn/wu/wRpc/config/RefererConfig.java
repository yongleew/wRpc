package cn.wu.wRpc.config;

import cn.wu.wRpc.config.handler.SimpleConfigHandler;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class RefererConfig<T> extends AbstractConfig<T> {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    public T getRef() {
        if (ref == null) {
            initRef();
        }
        return ref;
    }

    private void initRef() {
        if (initialized.get()) {
            return;
        }
        if (interfaceClass == null || !interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass invalid");
        }
        SimpleConfigHandler configHandler = new SimpleConfigHandler();

        ref = configHandler.refer(this);

    }
}
