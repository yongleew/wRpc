package cn.wu.wRpc.config.handler;

import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.URL;

import java.util.List;

public interface ConfigHandler {

    <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registryUrls, URL serviceUrl);
}
