package com.tweek.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "source.email")
public class EmailSourceProperties {
    private String writeKey;

    public String getWriteKey() {
        return writeKey;
    }

    public void setWriteKey(String _writeKey) {
        writeKey = _writeKey;
    }
}
