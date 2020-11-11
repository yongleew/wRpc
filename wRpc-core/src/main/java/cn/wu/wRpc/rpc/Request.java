package cn.wu.wRpc.rpc;

import lombok.Data;

import java.util.Map;

@Data
public class Request {
    private long requestId;
    private String interfaceName;
    private String methodName;
    private String paramtersDesc;
    private Object[] arguments;

}
