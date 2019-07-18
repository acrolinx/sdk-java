package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.Server;

import java.util.List;

public class PlatformInformation
{
    private Server server;
    private List<String> locales;

    public Server getServer();
    // try to make this readonly
    public List<String> getLocales();

}
