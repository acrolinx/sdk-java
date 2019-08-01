/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import org.apache.http.client.config.RequestConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

public interface AcrolinxHttpClient {
    Future<AcrolinxResponse> fetch(URI url, HttpMethod method, Map<String, String> headers, String body) throws IOException, AcrolinxException;

    void close() throws IOException;

    void start();

    void configure(RequestConfig config);
}
