package cn.wu.wRpc.transport;

public interface MessageHandler {
    Object handle(Object message);
}
