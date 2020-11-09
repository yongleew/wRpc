package cn.wu.wRpc.config;


import lombok.Data;

@Data
public class ServiceConfig<T> {
    private T ref;
    private Class<T> interfaceClass;

    public void export() {
        if (ref == null || interfaceClass == null) {
            throw new IllegalArgumentException("ref or interfaceClass not null");
        }


    }
}
