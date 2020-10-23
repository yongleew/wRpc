package cn.wu.wRpc.protocol;

import cn.wu.wRpc.rpc.Protocol;

import java.util.HashMap;
import java.util.Map;

public class ProtocolFactory {

    private static Map<String, Protocol> protocolInstances = new HashMap<>();

    public static Protocol newProtocol(String protocolName) {
        Protocol protocol = protocolInstances.get(protocolName);
        if (protocol == null) {
            protocol = new WRpcProtocol();
            protocolInstances.put(protocolName, protocol);
        }
        return protocol;
    }
}
