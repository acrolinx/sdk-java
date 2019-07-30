package com.acrolinx.client.sdk.wiremock;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.acrolinx.client.sdk.wiremock.common.WireMockUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockUrl;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests if the Apache Http Client behaves like expected.
 */
public class ApacheHttpClientFutureTest extends MockedTestBase {
    static final int DELAY_MS = 2000;
    CloseableHttpAsyncClient httpAsyncClient;
    Future<HttpResponse> future;

    @Before
    public void beforeTest() {
        WireMockUtils.mockPostSuccessResponseWithDelay("", "dummyResponse", DELAY_MS);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setConnectionRequestTimeout(60000).setSocketTimeout(60000)
                .build();
        HttpRequestBase request = new HttpGet(mockUrl(""));
        request.setConfig(config);
        httpAsyncClient = HttpAsyncClients.createDefault();
        httpAsyncClient.start();
        future = httpAsyncClient.execute(request, null);
    }

    @After
    public void afterTest() throws IOException {
        httpAsyncClient.close();
    }

    // Long timeouts to make it better debuggable.
    @Test(expected = CancellationException.class, timeout = 60000)
    public void verify_strange_apache_http_client_cancel_result() throws AcrolinxException, InterruptedException, ExecutionException, IOException {
        // ============================= Here comes the mystery ================================================
        assertFalse(future.isDone());
        boolean cancelResult = future.cancel(true);
        assertFalse("actually cancelResult should be true, but we assert false because this is strange reality", cancelResult);
        assertTrue(future.isCancelled());
        // ============================= Here ends the mystery ================================================

        assertTrue("A canceled Future should be also \"done\" according to the Future specs", future.isDone());
        // Here we expect a CancellationException according to the Future specs
        future.get();
    }

}
