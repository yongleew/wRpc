package cn.wu.wRpc.rpc;


/**
 * 单例
 * 一个协议只保存一个实例, 见{@link cn.wu.wRpc.protocol.ProtocolFactory}
 */
public interface Protocol {
    <T> Exporter<T> export(Provider<T> provider, URL url);
}
