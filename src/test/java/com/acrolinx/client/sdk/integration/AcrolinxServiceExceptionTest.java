/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class AcrolinxServiceExceptionTest extends IntegrationTestBase
{
    @Test
    public void testGetCapabilitiesWithInvalidAccessToken() throws AcrolinxException
    {
        try {
            endpoint.getCapabilities(new AccessToken("invalid"));
            fail("getCapabilities should fail because of invalid AccessToken");
        } catch (AcrolinxServiceException e) {
            assertEquals(AcrolinxServiceException.Type.auth.toString(), e.getType());
            assertThat(e.getDetail(), not(isEmptyOrNullString()));
            assertThat(e.getTitle(), not(isEmptyOrNullString()));
            System.out.println(e.toString());
            assertEquals(401, e.getStatus());

            assertEquals(HttpMethod.GET, e.getRequest().getMethod());
            assertThat(e.getRequest().getUrl().toString(), startsWith(ACROLINX_URL));
        }
    }

    @Test
    public void test404ErrorCodeCheckApi() throws URISyntaxException, AcrolinxException, IOException
    {
        AcrolinxEndpoint endpoint = new AcrolinxEndpoint(new URI("http://acrolinx.berlin"), "invlaid", "1.2.3.4", "en");
        try {
            PlatformInformation information = endpoint.getPlatformInformation();
            fail("test should fail due to 404");
        } catch (AcrolinxException ae) {
            assertTrue(ae.getMessage().contains("404"));
        } finally {
            endpoint.close();
        }
    }

}
