package cn.wu.wRpc.config;

import cn.wu.wRpc.config.handler.ConfigHandler;
import cn.wu.wRpc.config.handler.SimpleConfigHandler;
import cn.wu.wRpc.rpc.Exporter;
import cn.wu.wRpc.rpc.URL;
import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务端提供服务配置
 * @param <T>
 */
@Data
public class ServiceConfig<T> extends AbstractInterfaceConfig {
    //服务接口
    private Class<T> interfaceClass;
    //服务实现类
    private T ref;
    //注册中心
    //private List<RegistryConfig> registries;
    //使用的协议及端口，目前WRPC协议 wrpc:8088
    private String export;

    private List<URL> registryUrls;

    private AtomicBoolean exported = new AtomicBoolean(false);

    //导出服务对应的exporter
    private List<Exporter<T>> exporters = new ArrayList<>();

    public void export() {
        if (!exported.compareAndSet(false, true)) {
            return;
        }

        validateInterface(interfaceClass);

        /*if (registries == null || registries.isEmpty()) {
            throw new IllegalStateException("registries is null");
        }*/
        Map<String, Integer> protocols = getProtocolAndPort();
        protocols.forEach(this::doExport);
    }

    private void doExport(String protocol, Integer port) {
        String hostAddress = getLocalHostAddress();

        URL serviceUrl = new URL(protocol, hostAddress, port, interfaceClass.getName());

        ConfigHandler configHandler = new SimpleConfigHandler();
        Exporter<T> export = configHandler.export(interfaceClass, ref, registryUrls, serviceUrl);
        exporters.add(export);
    }

    private String getLocalHostAddress() {
        String address = null;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            address = "127.0.0.1";
        }
        return address;
    }

    public Map<String, Integer> getProtocolAndPort() {
        return ConfigUtil.parseExport(this.export);
    }

    private void validateInterface(Class<T> interfaceClass) {
        if (interfaceClass == null) {
            throw new IllegalStateException("interface is null");
        }
        if (ref == null) {
            throw new IllegalStateException("ref is null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalStateException("the interfaceClass " + interfaceClass + " is not interface");
        }
        if (!interfaceClass.isAssignableFrom(ref.getClass())) {
            throw new IllegalStateException("the ref " + ref + " not implement the interfaceClass " + interfaceClass);
        }
    }
}
