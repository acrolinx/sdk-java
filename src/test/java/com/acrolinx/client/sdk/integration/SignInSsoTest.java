/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_SSO_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class SignInSsoTest extends IntegrationTestBase
{
    @Test
    public void testSignInWithSSO() throws AcrolinxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test(expected = AcrolinxException.class)
    public void testSignInWithSsoThrowsException() throws AcrolinxException
    {
        endpoint.signInWithSSO("invalidGenericToken", "invalidUserName");
    }
}
