package com.danzir.scambio.figurine.scrapper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scambio.figurine.scrapper")
public class ConfigProperties {

    private String username;
    private String password;
    private boolean headless;
    private int threadSetSize;
    private int chunkSize;
    private long timeout;

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

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public int getThreadSetSize() {
        return threadSetSize;
    }

    public void setThreadSetSize(int threadSetSize) {
        this.threadSetSize = threadSetSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
