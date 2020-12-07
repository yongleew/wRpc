package cn.wu.wRpc.config;

import lombok.Data;

import java.util.Objects;

@Data
public class AbstractConfig<T> {

    public static final int DEFAULT_PORT = 8888;
    public static final String DEFAULT_Host = "127.0.0.1";

    protected T ref;
    protected Class<T> interfaceClass;
    protected String host = DEFAULT_Host;
    protected int port = DEFAULT_PORT;
    protected String registryAddress;

    public String getConfigKey() {
        return host + ":" + port + "/" + interfaceClass;
    }

    public String getIpPortStr() {
        return host + ":" + port;
    }

    public String getServiceUrl() {
        return "wRpc://" + host + ":" + port + "/" + interfaceClass.getName();
    }

    public String getPath() {
        return interfaceClass.getName();
    }

    public String getRegistryUri() {
        return "zookeeper://" + registryAddress + "/" + interfaceClass.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConfig<?> that = (AbstractConfig<?>) o;
        return port == that.port &&
                Objects.equals(interfaceClass, that.interfaceClass) &&
                Objects.equals(host, that.host) &&
                Objects.equals(registryAddress, that.registryAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interfaceClass, host, port, registryAddress);
    }
}
