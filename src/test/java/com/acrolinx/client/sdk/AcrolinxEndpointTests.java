/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.HttpMethod;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class AcrolinxEndpointTests {
  private final AcrolinxHttpClient acrolinxHttpClient = Mockito.mock(AcrolinxHttpClient.class);
  private final AcrolinxResponse acrolinxResponse = Mockito.mock(AcrolinxResponse.class);
  private final ArgumentCaptor<Map<String, String>> headersCaptor =
      ArgumentCaptor.forClass(Map.class);

  @BeforeEach
  void beforeEach() throws IOException, AcrolinxException {
    when(acrolinxHttpClient.fetch(
            any(URI.class), any(HttpMethod.class), notNull(), eq((String) null)))
        .thenReturn(acrolinxResponse);

    when(acrolinxResponse.getStatus()).thenReturn(200);
    when(acrolinxResponse.getResult()).thenReturn("{\"data\":{} }");
  }

  @Test
  void ensureUrlSetCorrectly() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/api/v1/")),
              eq(HttpMethod.GET),
              notNull(),
              eq((String) null));
    }
  }

  @Test
  void ensureUrlWithSlashSetCorrectly() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient, new URI("https://www.acrolinx.com/"), "foo", "1.2.3.97", "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/api/v1/")),
              eq(HttpMethod.GET),
              notNull(),
              eq((String) null));
    }
  }

  @Test
  void ensureUrlWithPathSetCorrectly() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/proxy"),
            "foo",
            "1.2.3.97",
            "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/proxy/api/v1/")),
              eq(HttpMethod.GET),
              notNull(),
              eq((String) null));
    }
  }

  @Test
  void ensureUrlWithPathAndSlashSetCorrectly()
      throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/proxy/"),
            "foo",
            "1.2.3.97",
            "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/proxy/api/v1/")),
              eq(HttpMethod.GET),
              notNull(),
              eq((String) null));
    }
  }

  @Test
  void ensureBaseUrlSetCorrectly() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void testCapabilitiesCall() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar")) {
      acrolinxEndpoint.getCapabilities(new AccessToken("abc"));

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/api/v1/capabilities")),
              eq(HttpMethod.GET),
              headersCaptor.capture(),
              eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void testCapabilitiesCallWithPath() throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/foo"),
            "foo",
            "1.2.3.97",
            "bar")) {
      acrolinxEndpoint.getCapabilities(new AccessToken("abc"));

      verify(acrolinxHttpClient)
          .fetch(
              eq(new URI("https://www.acrolinx.com/foo/api/v1/capabilities")),
              eq(HttpMethod.GET),
              headersCaptor.capture(),
              eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com/foo", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void ensureBaseUrlWithSlashSetCorrectly()
      throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient, new URI("https://www.acrolinx.com/"), "foo", "1.2.3.97", "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com/", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void ensureBaseUrlWithPathSetCorrectly()
      throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/proxy"),
            "foo",
            "1.2.3.97",
            "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com/proxy", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void ensureBaseUrlWithPathAndSlashSetCorrectly()
      throws AcrolinxException, URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/proxy/"),
            "foo",
            "1.2.3.97",
            "bar")) {
      acrolinxEndpoint.getPlatformInformation();

      verify(acrolinxHttpClient)
          .fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

      final Map<String, String> headers = headersCaptor.getValue();
      assertEquals("https://www.acrolinx.com/proxy/", headers.get("X-Acrolinx-Base-Url"));
    }
  }

  @Test
  void errorHandledInCaseOfNoResult() throws URISyntaxException, IOException {
    when(acrolinxResponse.getStatus()).thenReturn(700);
    when(acrolinxResponse.getResult()).thenReturn(null);

    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(
            acrolinxHttpClient,
            new URI("https://www.acrolinx.com/proxy"),
            "foo",
            "1.2.3.97",
            "bar")) {
      AcrolinxException acrolinxException =
          Assertions.assertThrows(
              AcrolinxException.class, () -> acrolinxEndpoint.getPlatformInformation());
      assertEquals("Fetch failed with status 700 and no result.", acrolinxException.getMessage());
    }
  }
}
