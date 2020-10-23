package cn.wu.wRpc.transport;

public interface MessageHandler {

    Object handle(Channel channel, Object message);
}
