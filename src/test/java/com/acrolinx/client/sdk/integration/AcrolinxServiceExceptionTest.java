/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Test;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AcrolinxServiceExceptionTest extends IntegrationTestBase {
    @Test
    public void testGetCapabilitiesWithInvalidAccessToken() throws AcrolinxException {
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
}
