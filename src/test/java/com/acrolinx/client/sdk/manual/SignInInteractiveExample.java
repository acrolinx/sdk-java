/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.manual;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.createTestAcrolinxEndpoint;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import java.net.URISyntaxException;

public class SignInInteractiveExample {
  public static void main(String[] args)
      throws URISyntaxException, AcrolinxException, InterruptedException {
    AcrolinxEndpoint acrolinxEndpoint = createTestAcrolinxEndpoint();

    SignInSuccess signInSuccess =
        acrolinxEndpoint.signInInteractive(
            urlString -> {
              System.out.println("Please open the following URL:");
              System.out.println(urlString);
            });

    System.out.println("accessToken = " + signInSuccess.getAccessToken().getAccessTokenAsString());
    System.out.println("username = " + signInSuccess.getUser().getUsername());
  }
}
