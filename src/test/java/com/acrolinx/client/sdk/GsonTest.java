/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import static com.acrolinx.client.sdk.internal.JsonUtils.parseJson;
import static com.acrolinx.client.sdk.testutils.TestUtils.readResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.internal.SuccessResponse;
import com.acrolinx.client.sdk.platform.Server;

public class GsonTest
{
    @Test
    public void testParseServer()
    {
        String json = readResource("server.json");

        Server server = parseJson(json, Server.class);

        assertEquals("ServerName", server.getName());
        assertEquals("ServerVersion", server.getVersion());
    }

    @Test
    public void testParseSuccessResponse()
    {
        String json = readResource("server-success-response.json");

        SuccessResponse<Server> server = parseJson(json, SuccessResponse.class, Server.class);

        assertEquals("ServerName", server.data.getName());
        assertEquals("ServerVersion", server.data.getVersion());
    }

    @Test
    public void testParseSignInSuccessResponse()
    {
        String json = readResource("sign-in-success-response.json");

        SignInResponse signInResponse = parseJson(json, SignInResponse.class);

        if (signInResponse instanceof SignInResponse.Success) {
            SignInSuccess signInSuccess = ((SignInResponse.Success) signInResponse).data;
            assertEquals(new AccessToken("dummyAccessToken"), signInSuccess.getAccessToken());
        } else {
            fail("signInResponse should be success but is " + signInResponse);
        }
    }

    @Test
    public void testParseSignInLinksResponse()
    {
        String json = readResource("sign-in-links-response.json");

        SignInResponse signInResponse = parseJson(json, SignInResponse.class);

        if (signInResponse instanceof SignInResponse.SignInLinks) {
            SignInResponse.SignInLinksInternal links = ((SignInResponse.SignInLinks) signInResponse).links;
            assertEquals("https://test-next-ssl.acrolinx.com/signin/?ticket_id=2b3db9c6-79e3-4cc0-b760-624fb19802e9",
                    links.getInteractive());
        } else {
            fail("signInResponse should be links but is " + signInResponse);
        }
    }
}
