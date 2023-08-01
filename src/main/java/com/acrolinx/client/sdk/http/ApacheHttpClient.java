/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.http;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class ApacheHttpClient implements AcrolinxHttpClient
{
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClient.class);

    private final CloseableHttpClient httpClient;

    public ApacheHttpClient()
    {
        this.httpClient = createHttpClient();
    }

    private static CloseableHttpClient createHttpClient()
    {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

        return HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(
                requestConfig).useSystemProperties().build();
    }

    @Override
    public AcrolinxResponse fetch(URI uri, HttpMethod httpMethod, Map<String, String> headers, String jsonBody)
            throws IOException, AcrolinxException
    {
        HttpRequestBase request = createRequests(uri, httpMethod, jsonBody);

        setHeaders(request, headers);

        logger.debug("Executing request for API: {}", uri);
        final HttpResponse response = httpClient.execute(request);

        AcrolinxResponse acrolinxResponse = new AcrolinxResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        logger.debug("Response status code: {}", statusCode);
        acrolinxResponse.setStatus(statusCode);

        HttpEntity responseEntity = response.getEntity();

        try {
            String result = EntityUtils.toString(responseEntity);
            acrolinxResponse.setResult(result);
            logger.debug("Entity response: {}", result);
        } catch (ParseException | IOException e) {
            throw new AcrolinxException(e);
        }

        return acrolinxResponse;
    }

    @Override
    public void close() throws IOException
    {
        logger.info("Disconnected http client");
        this.httpClient.close();
    }

    private static HttpRequestBase createRequests(URI uri, HttpMethod httpMethod, @Nullable String jsonBody)
    {
        switch (httpMethod) {
            case GET:
                return new HttpGet(uri);
            case DELETE:
                return new HttpDelete(uri);
            case POST:
                HttpPost httpPost = new HttpPost(uri);

                if (jsonBody != null) {
                    StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                    httpPost.setEntity(entity);
                }

                return httpPost;
            default:
                throw new IllegalArgumentException("Illegal HttpMethod " + httpMethod);
        }
    }

    private static void setHeaders(HttpRequestBase request, Map<String, String> headers)
    {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
    }
}
