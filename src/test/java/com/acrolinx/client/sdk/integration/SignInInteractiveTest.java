/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_USERNAME;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class SignInInteractiveTest extends IntegrationTestBase {
  private final InteractiveCallback interactiveCallback = Mockito.mock(InteractiveCallback.class);

  @Test
  void testSignInWithNoAccessToken() {
    SignInException signInException =
        Assertions.assertThrows(
            SignInException.class,
            () -> acrolinxEndpoint.signInInteractive(interactiveCallback, null, 400L));
    assertEquals("Timeout", signInException.getMessage());
    verify(interactiveCallback, times(1))
        .onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
  }

  @Test
  void testSignInWithPollingWithValidAccessToken() throws AcrolinxException, InterruptedException {
    SignInSuccess signInSuccess =
        acrolinxEndpoint.signInInteractive(interactiveCallback, ACROLINX_API_TOKEN, 500L);

    assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    verifyNoMoreInteractions(interactiveCallback);
  }

  @Test
  void testSignInWithPollingWithInvalidAccessToken() {
    SignInException signInException =
        Assertions.assertThrows(
            SignInException.class,
            () ->
                acrolinxEndpoint.signInInteractive(
                    interactiveCallback, new AccessToken("accesstoken"), 1_000L));
    assertEquals("Timeout", signInException.getMessage());
    verify(interactiveCallback, times(1))
        .onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
  }

  @Test
  void testSignCancel() {
    final long timeoutMs = 1_000;

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Future<SignInSuccess> future =
        executorService.submit(
            new Callable<SignInSuccess>() {
              @Override
              public SignInSuccess call() throws Exception {
                return acrolinxEndpoint.signInInteractive(
                    interactiveCallback, ACROLINX_API_TOKEN, timeoutMs);
              }
            });

    assertTrue(future.cancel(true));

    Assertions.assertThrows(
        CancellationException.class, () -> future.get(timeoutMs, TimeUnit.MILLISECONDS));
  }
}
