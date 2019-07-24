package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.internal.SuccessResponse;
import com.acrolinx.client.sdk.platform.Server;
import org.junit.Test;

import static com.acrolinx.client.sdk.internal.JsonUtils.parseJson;
import static com.acrolinx.client.sdk.testutils.TestUtils.readResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GsonTest {
    @Test
    public void testParseServer() {
        String json = readResource("server.json");

        Server server = parseJson(json, Server.class);

        assertEquals(server.getName(), "ServerName");
        assertEquals(server.getVersion(), "ServerVersion");
    }

    @Test
    public void testParseSuccessResponse() {
        String json = readResource("server-success-response.json");

        SuccessResponse<Server> server = parseJson(json, SuccessResponse.class, Server.class);

        assertEquals(server.data.getName(), "ServerName");
        assertEquals(server.data.getVersion(), "ServerVersion");
    }

    @Test
    public void testParseSignInSuccessResponse() {
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
    public void testParseSignInLinksResponse() {
        String json = readResource("sign-in-links-response.json");

        SignInResponse signInResponse = parseJson(json, SignInResponse.class);

        if (signInResponse instanceof SignInResponse.SignInLinks) {
            SignInResponse.SignInLinksInternal links = ((SignInResponse.SignInLinks) signInResponse).links;
            assertEquals("https://test-next-ssl.acrolinx.com/signin/?ticket_id=2b3db9c6-79e3-4cc0-b760-624fb19802e9", links.interactive);
        } else {
            fail("signInResponse should be links but is " + signInResponse);
        }
    }
}
