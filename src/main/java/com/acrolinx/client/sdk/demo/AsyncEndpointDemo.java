/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.demo;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.PlatformInformation;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Right place for a demo? A separate project
public class AsyncEndpointDemo {

    AcrolinxEndpoint endpoint;
    int allowedThreads;
    ExecutorService executorService;

    public AsyncEndpointDemo(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this(acrolinxURL, clientSignature, clientVersion, clientLocale, 1);

    }

    public AsyncEndpointDemo(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale, int allowedThreads) {
        this.allowedThreads = allowedThreads;
        this.endpoint = new AcrolinxEndpoint(acrolinxURL, clientSignature, clientVersion, clientLocale);
        createExecutor();
    }

    private void createExecutor() {
        this.executorService = Executors.newFixedThreadPool(allowedThreads);
    }

    public Future<PlatformInformation> getPlatformInformation() {
        GetPlatformInformation getPlatformInformationCallable = new GetPlatformInformation(endpoint);
        return executorService.submit(getPlatformInformationCallable);
    }

    private static class GetPlatformInformation implements Callable<PlatformInformation> {
        AcrolinxEndpoint endpoint;

        public GetPlatformInformation(AcrolinxEndpoint endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public PlatformInformation call() throws Exception {
            return endpoint.getPlatformInformation();
        }
    }
}
