/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AcrolinxServiceExceptionTest extends IntegrationTestBase {
    @Test
    public void testGetCapabilitiesWithInvalidAccessToken() throws AcrolinxException, InterruptedException, ExecutionException {
        try {
            endpoint.getCapabilities(new AccessToken("invalid")).get();
            fail("getCapabilities should fail because of invalid AccessToken");
        } catch (AcrolinxServiceException e) {
            assertEquals(AcrolinxServiceException.Type.auth.toString(), e.getType());
            assertThat(e.getDetail(), not(isEmptyOrNullString()));
            assertThat(e.getTitle(), not(isEmptyOrNullString()));
            assertEquals(401, e.getStatus());
        }
    }
}
