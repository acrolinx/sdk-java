package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.internal.JsonResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApacheHttpClient implements AcrolinxHttpClient {
    // Is the default closeable http client the best choice?
    // Can it handle forward proxies? In AEM it didn't
    private RequestConfig config = RequestConfig.custom().setConnectTimeout(500).setConnectionRequestTimeout(500).setSocketTimeout(500)
            .build();

    @Override
    public Future<String> fetch(URI uri, HttpMethod httpMethod, Map<String, String> headers, String jsonBody) throws IOException, AcrolinxException {

        HttpRequestBase request = createRequests(uri, httpMethod, jsonBody);
        request.setConfig(this.config);
        setHeaders(request, headers);

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
        httpAsyncClient.start();
        Future<HttpResponse> responseFuture = httpAsyncClient.execute(request, null);
        HttpResponse response;
        try {
            response = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AcrolinxException(e);
        } finally {
            httpAsyncClient.close();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            throw new IOException("Response has invalid valid status code" + statusCode + ".");
        }

        HttpEntity responseEntity = response.getEntity();

        JsonResponse jsonResponse = new JsonResponse(EntityUtils.toString(responseEntity));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(jsonResponse);
    }

    private HttpRequestBase createRequests(URI uri, HttpMethod httpMethod, @Nullable String jsonBody) throws UnsupportedEncodingException {
        switch (httpMethod) {
            case GET:
                return new HttpGet(uri);
            case POST:
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setHeader("Content-type", "application/json");
                if (jsonBody != null) {
                    StringEntity entity = new StringEntity(jsonBody);
                    httpPost.setEntity(entity);
                }
                return httpPost;
            default:
                throw new IllegalArgumentException("Illegal HttpMethod " + httpMethod);
        }
    }

    private void setHeaders(HttpRequestBase request, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
    }
}
