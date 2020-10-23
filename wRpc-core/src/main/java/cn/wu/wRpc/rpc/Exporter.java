package cn.wu.wRpc.rpc;

public interface Exporter<T> {

    Provider<T> getProvider();

    void init();
}
