package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.internal.JsonResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ApacheHttpClient implements AcrolinxHttpClient {

    private RequestConfig config = RequestConfig.custom().setConnectTimeout(500).setConnectionRequestTimeout(500).setSocketTimeout(500)
            .build();

    @Override
    public Future<AcrolinxResponse> fetch(URI uri, HttpMethod httpMethod, Map<String, String> headers, String jsonBody) throws IOException, AcrolinxException {
        HttpRequestBase request = createRequests(uri, httpMethod, jsonBody);
        request.setConfig(this.config);
        setHeaders(request, headers);

        final CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();
        httpAsyncClient.start(); //blongs somewhere else
        final Future<HttpResponse> responseFuture = httpAsyncClient.execute(request, null);
        
        return new Future<AcrolinxResponse>(){

            private volatile AcrolinxResponseState state = AcrolinxResponseState.INPROGRESS;
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                responseFuture.cancel(mayInterruptIfRunning);
                state = AcrolinxResponseState.CANCELLED;
                return true;
            }

            @Override
            public boolean isCancelled() {
                return (state == AcrolinxResponseState.CANCELLED);
            }

            @Override
            public boolean isDone() {
                return (state == AcrolinxResponseState.DONE);
            }

            @Override
            public AcrolinxResponse get() throws InterruptedException, ExecutionException {
                HttpResponse response;
                try {
                    response = responseFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw e;//new AcrolinxException(e);
                } finally {
                    try {
                        httpAsyncClient.close(); //blongs somewhere else
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        
                return processResponse(response);
            }

            @Override
            public AcrolinxResponse get(long timeout, TimeUnit unit)
                    throws InterruptedException, ExecutionException, TimeoutException {
                HttpResponse response;
                try {
                    response = responseFuture.get(timeout, unit);
                } catch (InterruptedException | ExecutionException e) {
                    throw e;//new AcrolinxException(e);
                } finally {
                    try {
                        httpAsyncClient.close();
                    } catch (IOException e) {
                        // TODO log
                    } //blongs somewhere else
                }
                return processResponse(response);
            }
            
            private AcrolinxResponse processResponse(HttpResponse response) {
                AcrolinxResponse acrolinxResponse = new AcrolinxResponse();
                int statusCode = response.getStatusLine().getStatusCode();
                acrolinxResponse.setStatus(statusCode);

               HttpEntity responseEntity = response.getEntity();
                try {
                    acrolinxResponse.setResult(EntityUtils.toString(responseEntity));
                } catch (ParseException | IOException e) {
                    // TODO log
                }
        
                state = AcrolinxResponseState.DONE;
                return acrolinxResponse;
            }
            
            
        };
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
