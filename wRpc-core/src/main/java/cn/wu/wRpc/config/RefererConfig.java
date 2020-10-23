package cn.wu.wRpc.config;

import lombok.Data;

/**
 * 客户端调用服务配置
 *
 */
@Data
public class RefererConfig<T> extends AbstractInterfaceConfig {
    //服务接口
    private Class<T> interfaceClass;
    //服务的代理类
    private T ref;
}
