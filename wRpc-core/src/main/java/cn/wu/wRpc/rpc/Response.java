package cn.wu.wRpc.rpc;

public interface Response {
    Object getValue();
    Exception getException();
    long getRequestId();
}
