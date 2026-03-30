package com.manage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * FTP配置属性类
 * 从application.yml中读取ftp配置
 */
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String basepath;
    private Integer timeout;
    private Boolean passivemode;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getPassivemode() {
        return passivemode;
    }

    public void setPassivemode(Boolean passivemode) {
        this.passivemode = passivemode;
    }
}
