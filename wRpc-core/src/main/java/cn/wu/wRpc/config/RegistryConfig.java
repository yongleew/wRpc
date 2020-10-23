package cn.wu.wRpc.config;

import lombok.Data;

/**
 * 服务注册配置
 */
@Data
public class RegistryConfig extends AbstractConfig {

    private String name;
    //注册协议
    private String regProtocol;
    //地址,格式：ip1:port1,ip2:port2,ip3，如果没有port，则使用默认的port
    private String address;
    //默认端口
    private Integer port;
    // 会话超时时间(毫秒)
    private Integer registrySessionTimeout;
    // 失败后重试的时间间隔
    private Integer registryRetryPeriod;
}
