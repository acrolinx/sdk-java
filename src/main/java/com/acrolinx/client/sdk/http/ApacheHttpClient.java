/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxRuntimeException;
import com.acrolinx.client.sdk.internal.FutureMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
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
import java.util.concurrent.Future;

public class ApacheHttpClient implements AcrolinxHttpClient {

    private RequestConfig config = RequestConfig.custom().setConnectTimeout(500).setConnectionRequestTimeout(500).setSocketTimeout(500)
            .build();
    private CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();


    @Override
    public Future<AcrolinxResponse> fetch(URI uri, HttpMethod httpMethod, Map<String, String> headers, String jsonBody) throws IOException {
        HttpRequestBase request = createRequests(uri, httpMethod, jsonBody);
        request.setConfig(this.config);
        setHeaders(request, headers);

        final Future<HttpResponse> responseFuture = httpAsyncClient.execute(request, null);

        return new FutureMapper<HttpResponse, AcrolinxResponse>(responseFuture) {
            @Override
            protected AcrolinxResponse map(HttpResponse response) {
                AcrolinxResponse acrolinxResponse = new AcrolinxResponse();
                int statusCode = response.getStatusLine().getStatusCode();
                acrolinxResponse.setStatus(statusCode);

                HttpEntity responseEntity = response.getEntity();
                try {
                    acrolinxResponse.setResult(EntityUtils.toString(responseEntity));
                } catch (ParseException | IOException e) {
                    throw new AcrolinxRuntimeException(e);
                }
                return acrolinxResponse;
            }
        };
    }

    @Override
    public void close() throws IOException {
        this.httpAsyncClient.close();
    }

    @Override
    public void start() {
        this.httpAsyncClient.start();
    }

    private HttpRequestBase createRequests(URI uri, HttpMethod httpMethod, @Nullable String jsonBody) throws UnsupportedEncodingException {
        switch (httpMethod) {
            case GET:
                return new HttpGet(uri);
            case DELETE:
                return new HttpDelete(uri);
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
