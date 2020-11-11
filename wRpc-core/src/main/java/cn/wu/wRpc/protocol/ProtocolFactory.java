package cn.wu.wRpc.protocol;

import java.util.concurrent.ConcurrentHashMap;

public class ProtocolFactory {
    private static ConcurrentHashMap<String, Protocol> protocolMap = new ConcurrentHashMap<>();

    public static Protocol newProtocol(String protocolKey) {
        Protocol protocol = protocolMap.get(protocolKey);
        if (protocol == null) {
            //protocolMap.putIfAbsent(protocolKey, )
        }
        return null;
    }
}
