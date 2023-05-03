/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

public class User
{
    private final String id;
    private final String username;

    public User(String id, String username)
    {
        this.id = id;
        this.username = username;
    }

    public String getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }
}
