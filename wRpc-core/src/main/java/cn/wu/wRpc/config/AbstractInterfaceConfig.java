package cn.wu.wRpc.config;

import lombok.Data;

@Data
public class AbstractInterfaceConfig {
    private String application;
    private String version;
    private String filter;
}
