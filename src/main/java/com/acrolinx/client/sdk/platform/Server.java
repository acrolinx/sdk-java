/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.platform;

public class Server {
    private String version;
    private String name;

    public Server(String version, String name) {
        this.version = version;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
