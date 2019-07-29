package com.acrolinx.client.sdk.manual;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.SignInException;

import java.net.URISyntaxException;

import static com.acrolinx.client.sdk.integration.CommonTestSetup.createTestAcrolinxEndpoint;

public class SignInInteractiveExample {
    public static void main(String[] args) throws URISyntaxException, SignInException {
        AcrolinxEndpoint endpoint = createTestAcrolinxEndpoint();

        SignInSuccess signInSuccess = endpoint.signInInteractive(new InteractiveCallback() {
            @Override
            public void onInteractiveUrl(String url) {
                System.out.println("Please open the following URL:");
                System.out.println(url);
            }
        });

        System.out.println("accessToken = " + signInSuccess.getAccessToken().getAccessToken());
        System.out.println("username = " + signInSuccess.getUser().getUsername());
    }
}