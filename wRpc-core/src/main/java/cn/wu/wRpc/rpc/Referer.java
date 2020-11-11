package cn.wu.wRpc.rpc;

public interface Referer<T> {
    Response call(Request request);

    void init();
}
