/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import static com.acrolinx.client.sdk.internal.JsonUtils.parseJson;
import static com.acrolinx.client.sdk.testutils.TestUtils.readResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.internal.SuccessResponse;
import com.acrolinx.client.sdk.platform.Server;

class GsonTest
{
    @Test
    void testParseServer() throws IOException
    {
        String json = readResource("server.json");

        Server server = parseJson(json, Server.class);

        assertEquals("ServerName", server.getName());
        assertEquals("ServerVersion", server.getVersion());
    }

    @Test
    void testParseSuccessResponse() throws IOException
    {
        String json = readResource("server-success-response.json");

        SuccessResponse<Server> server = parseJson(json, SuccessResponse.class, Server.class);

        assertEquals("ServerName", server.data.getName());
        assertEquals("ServerVersion", server.data.getVersion());
    }

    @Test
    void testParseSignInSuccessResponse() throws IOException
    {
        String json = readResource("sign-in-success-response.json");

        SignInResponse signInResponse = parseJson(json, SignInResponse.class);

        SignInSuccess signInSuccess = ((SignInResponse.Success) signInResponse).data;
        assertEquals(new AccessToken("dummyAccessToken"), signInSuccess.getAccessToken());
    }

    @Test
    void testParseSignInLinksResponse() throws IOException
    {
        String json = readResource("sign-in-links-response.json");

        SignInResponse signInResponse = parseJson(json, SignInResponse.class);

        SignInResponse.SignInLinksInternal links = ((SignInResponse.SignInLinks) signInResponse).links;
        assertEquals("https://acrolinx.com/signin/?ticket_id=2b3db9c6-79e3-4cc0-b760-624fb19802e9",
                links.getInteractive());
    }
}
