/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import java.util.Objects;

public class AccessToken
{
    private String token;

    public AccessToken(String token)
    {
        this.token = token;
    }

    public String getAccessTokenAsString()
    {
        return token;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AccessToken that = (AccessToken) object;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(token);
    }

    @Override
    public String toString()
    {
        return "Access token is hidden for security reason";
    }

    public boolean isEmpty()
    {
        return this.token == null || this.token.isEmpty();
    }
}
