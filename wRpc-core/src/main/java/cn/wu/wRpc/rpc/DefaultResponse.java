package cn.wu.wRpc.rpc;

import lombok.Data;

import java.util.Map;

@Data
public class DefaultResponse implements Response {
    private long requestId;
    private Object value;
    private Exception exception;
    private Map<String, String> attachments;// rpc协议版本兼容时可以回传一些额外的信息

}
