/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.demo;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckResponse;

// Right place for a demo? A separate project? A java 8 demo much be much better
// This demo targets Java 7 environment
public class AsyncEndpointDemo
{

    AcrolinxEndpoint endpoint;
    int allowedThreads;
    ExecutorService executorService;

    public AsyncEndpointDemo(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale)
    {
        this(acrolinxURL, clientSignature, clientVersion, clientLocale, 1);
    }

    public AsyncEndpointDemo(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale,
            int allowedThreads)
    {
        this.allowedThreads = allowedThreads;
        this.endpoint = new AcrolinxEndpoint(acrolinxURL, clientSignature, clientVersion, clientLocale);
        createExecutor();
    }

    private void createExecutor()
    {
        this.executorService = Executors.newFixedThreadPool(allowedThreads);
    }

    public Future<PlatformInformation> getPlatformInformation()
    {
        return executorService.submit(new GetPlatformInformation(endpoint));
    }

    public Future<CheckResponse> check(AccessToken accessToken, CheckRequest checkRequest)
    {
        return executorService.submit(new Check(endpoint, accessToken, checkRequest));
    }

    private static class GetPlatformInformation implements Callable<PlatformInformation>
    {
        AcrolinxEndpoint endpoint;

        public GetPlatformInformation(AcrolinxEndpoint endpoint)
        {
            this.endpoint = endpoint;
        }

        @Override
        public PlatformInformation call() throws Exception
        {
            return endpoint.getPlatformInformation();
        }
    }

    private static class Check implements Callable<CheckResponse>
    {
        AcrolinxEndpoint endpoint;
        AccessToken accessToken;
        CheckRequest checkRequest;

        public Check(AcrolinxEndpoint endpoint, AccessToken accessToken, CheckRequest checkRequest)
        {
            this.endpoint = endpoint;
            this.accessToken = accessToken;
            this.checkRequest = checkRequest;
        }

        @Override
        public CheckResponse call() throws Exception
        {
            return endpoint.check(accessToken, checkRequest);
        }
    }

}
