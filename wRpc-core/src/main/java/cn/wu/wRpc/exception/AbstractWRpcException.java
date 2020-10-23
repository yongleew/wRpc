package cn.wu.wRpc.exception;

public abstract class AbstractWRpcException extends RuntimeException {
    private String msg;

    public AbstractWRpcException(String msg) {
        this.msg = msg;
    }
}
