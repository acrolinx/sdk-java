package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

public interface AcrolinxHttpClient {
    Future<String> fetch(URI url, HttpMethod method, Map<String, String> headers, String body) throws IOException, AcrolinxException;
}
