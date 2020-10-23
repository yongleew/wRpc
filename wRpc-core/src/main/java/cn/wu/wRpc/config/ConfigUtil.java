package cn.wu.wRpc.config;

import cn.wu.wRpc.common.WRpcConstants;
import cn.wu.wRpc.exception.ServiceWRpcException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
    public static Map<String, Integer> parseExport(String export) {
        Map<String, Integer> ppsMap = new HashMap<>();
        if (StringUtils.isBlank(export)) {
            ppsMap.put(WRpcConstants.PROTOCOL_WRPC, WRpcConstants.PROTOCOL_WRPC_PORT);
            return ppsMap;
        }
        String[] protocolAndPorts = WRpcConstants.COMMA_SPLIT_PATTERN.split(export);
        for (String protocolAndPort : protocolAndPorts) {
            String[] pp = WRpcConstants.COMMA_COLON_PATTERN.split(protocolAndPort);
            if (pp.length == 2) {
                ppsMap.put(pp[0], Integer.parseInt(pp[1]));
            } else {
                throw new ServiceWRpcException("Export is malformed :" + export);
            }
        }
        return ppsMap;
    }
}
