/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.manual;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.createTestAcrolinxEndpoint;

import java.net.URISyntaxException;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class SignInInteractiveExample
{
    public static void main(String[] args) throws URISyntaxException, AcrolinxException, InterruptedException
    {
        AcrolinxEndpoint acrolinxEndpoint = createTestAcrolinxEndpoint();

        SignInSuccess signInSuccess = acrolinxEndpoint.signInInteractive(new InteractiveCallback() {
            @Override
            public void onInteractiveUrl(String url)
            {
                System.out.println("Please open the following URL:");
                System.out.println(url);
            }
        });

        System.out.println("accessToken = " + signInSuccess.getAccessToken().getAccessTokenAsString());
        System.out.println("username = " + signInSuccess.getUser().getUsername());
    }
}
