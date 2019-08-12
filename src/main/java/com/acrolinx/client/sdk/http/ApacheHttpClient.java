/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxRuntimeException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

public class ApacheHttpClient implements AcrolinxHttpClient {
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClient.class);


    @Override
    public AcrolinxResponse fetch(URI uri, HttpMethod httpMethod, Map<String, String> headers, String jsonBody) throws IOException {
        HttpRequestBase request = createRequests(uri, httpMethod, jsonBody);

        setHeaders(request, headers);

        logger.debug("Executing request for API: " + uri.toString());
        final HttpResponse response = httpClient.execute(request);

        AcrolinxResponse acrolinxResponse = new AcrolinxResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        logger.debug("Response status code: " + statusCode);
        acrolinxResponse.setStatus(statusCode);

        HttpEntity responseEntity = response.getEntity();
        try {
            String result = EntityUtils.toString(responseEntity);
            acrolinxResponse.setResult(result);
            logger.debug("Entity response: " + result);
        } catch (ParseException | IOException e) {
            throw new AcrolinxRuntimeException(e);
        }
        return acrolinxResponse;
    }

    @Override
    public void close() throws IOException {
        logger.info("Disconnected http client");
        this.httpClient.close();
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
