/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.http;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public interface AcrolinxHttpClient
{
    AcrolinxResponse fetch(URI url, HttpMethod method, Map<String, String> headers, String body)
            throws IOException, AcrolinxException;

    void close() throws IOException;
}
