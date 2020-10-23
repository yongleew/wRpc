package cn.wu.wRpc.rpc;

import lombok.Data;

@Data
public class URL {
    private String protocol;
    private String host;
    private int port;
    //接口名
    private String path;

    public URL(String protocol, String host, int port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public String getUri() {
        return protocol + "://" + host + ":" + port + "/" + path;
    }

    @Override
    public String toString() {
        return getUri();
    }

    public String getServerPortStr() {
        return host + ":" + port;
    }

    public URL createCopy() {
        return new URL(protocol, host, port, path);
    }
}
