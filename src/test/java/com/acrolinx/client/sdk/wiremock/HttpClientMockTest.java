/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.wiremock;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.*;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.*;
import static junit.framework.TestCase.assertTrue;

public class HttpClientMockTest extends MockedTestBase {

    public static final int PLATFORM_PORT_MOCKED = 8089;
    public static final String acrolinxUrl = "http://localhost:" + PLATFORM_PORT_MOCKED;

    @Test
    public void test404HttpClient() throws URISyntaxException, IOException, AcrolinxException {
        String api = "/api/v1/";
        httpClientMockNotFoundResponse(api);
        HashMap<String, String> headersHashMap = new HashMap<>();
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
        AcrolinxResponse acrolinxResponse = apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET, headersHashMap, null);

        assertTrue(acrolinxResponse.getStatus() == 404);

    }

    @Test
    public void testTimeout() throws URISyntaxException, IOException, AcrolinxException {

        String api = "/api/v1/";
        httpClientMockTimeOut(api);
        HashMap<String, String> headersHashMap = new HashMap<>();
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
        AcrolinxResponse acrolinxResponse = apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET, headersHashMap, null);

        assertTrue(acrolinxResponse.getStatus() == 408);

    }

    @Test
    public void testTimeoutParallelConections() throws URISyntaxException, IOException, ExecutionException, InterruptedException, AcrolinxException {

        final String api = "/api/v1/";
        httpClientMockTimeOut(api);
        final HashMap<String, String> headersHashMap = new HashMap<>();
        final ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<AcrolinxResponse> acrolinxResponseFuture = executorService.submit(new Callable<AcrolinxResponse>() {

            @Override
            public AcrolinxResponse call() throws Exception {
                return apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET, headersHashMap, null);
            }
        });

        httpClientMockSuccess(api);
        AcrolinxResponse ar200 = apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET, headersHashMap, null);
        assertTrue(ar200.getStatus() == 200);
        assertTrue(acrolinxResponseFuture.get().getStatus() == 408);

    }
}
