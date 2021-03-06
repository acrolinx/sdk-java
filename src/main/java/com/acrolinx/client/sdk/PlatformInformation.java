/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import java.util.List;

import com.acrolinx.client.sdk.platform.Server;

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
