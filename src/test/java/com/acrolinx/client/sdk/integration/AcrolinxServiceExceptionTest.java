/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AcrolinxServiceExceptionTest extends IntegrationTestBase {
  @Test
  void testGetCapabilitiesWithInvalidAccessToken() {
    AcrolinxServiceException acrolinxServiceException =
        Assertions.assertThrows(
            AcrolinxServiceException.class,
            () -> acrolinxEndpoint.getCapabilities(new AccessToken("invalid")));

    assertEquals(AcrolinxServiceException.Type.auth.toString(), acrolinxServiceException.getType());
    assertThat(acrolinxServiceException.getDetail(), not(emptyOrNullString()));
    assertThat(acrolinxServiceException.getTitle(), not(emptyOrNullString()));
    assertEquals(401, acrolinxServiceException.getStatus());

    assertEquals(HttpMethod.GET, acrolinxServiceException.getRequest().getMethod());
    assertThat(acrolinxServiceException.getRequest().getUrl().toString(), startsWith(ACROLINX_URL));
  }

  @Test
  void test404ErrorCodeCheckApi() throws URISyntaxException, IOException {
    try (AcrolinxEndpoint acrolinxEndpoint =
        new AcrolinxEndpoint(new URI(ACROLINX_URL + "/unknown"), "invlaid", "1.2.3.4", "en")) {
      Assertions.assertThrows(
          AcrolinxException.class, () -> acrolinxEndpoint.getPlatformInformation());
    }
  }
}
