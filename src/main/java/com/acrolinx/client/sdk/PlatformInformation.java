/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.Server;
import java.util.List;

public class PlatformInformation
{
    private final Server server;
    private final List<String> locales;

    public PlatformInformation(Server server, List<String> locales)
    {
        this.server = server;
        this.locales = locales;
    }

    public Server getServer()
    {
        return server;
    }

    public List<String> getLocales()
    {
        return locales;
    }
}
