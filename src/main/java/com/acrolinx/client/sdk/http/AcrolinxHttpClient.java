package com.acrolinx.client.sdk.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public interface AcrolinxHttpClient {
    String fetch(URI url, HttpMethod method, Map<String, String> headers, String body) throws IOException;
}
