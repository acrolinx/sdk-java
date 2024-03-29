/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.http;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewritingHttpClientDecorator implements AcrolinxHttpClient {
  private static final Logger logger = LoggerFactory.getLogger(RewritingHttpClientDecorator.class);

  private final AcrolinxHttpClient delegate;
  private final URI from;
  private final URI to;

  public RewritingHttpClientDecorator(
      final AcrolinxHttpClient delegate, final URI from, final URI to) {
    this.delegate = delegate;
    this.from = from;
    this.to = to;
  }

  @Override
  public AcrolinxResponse fetch(
      final URI url, final HttpMethod method, final Map<String, String> headers, final String body)
      throws IOException, AcrolinxException {
    return delegate.fetch(patchUrl(url), method, headers, body);
  }

  private URI patchUrl(final URI url) {
    if (!from.getScheme().toLowerCase().equalsIgnoreCase(url.getScheme())
        || !from.getAuthority().toLowerCase().equalsIgnoreCase(url.getAuthority())
        || !url.getPath().toLowerCase().startsWith(from.getPath().toLowerCase())) {
      return url;
    }

    try {
      return new URI(
          to.getScheme(),
          to.getAuthority(),
          to.getPath() + "/" + url.getPath().substring(from.getPath().length()),
          url.getQuery(),
          url.getFragment());
    } catch (final URISyntaxException e) {
      logger.info("Rewriting url failed falling back to original url: {}", e.getMessage());
      return url;
    }
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }
}
