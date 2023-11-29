/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public interface AcrolinxHttpClient {
  AcrolinxResponse fetch(URI url, HttpMethod method, Map<String, String> headers, String body)
      throws IOException, AcrolinxException;

  void close() throws IOException;
}
