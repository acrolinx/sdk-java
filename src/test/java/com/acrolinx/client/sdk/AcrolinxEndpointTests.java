/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class AcrolinxEndpointTests
{
    @Mock
    private AcrolinxHttpClient client;

    private AcrolinxEndpoint endpoint;

    @Mock
    private AcrolinxResponse response;

    @Captor
    private ArgumentCaptor<Map<String, String>> headersCaptor;

    @Before
    public void setUp() throws IOException, AcrolinxException
    {
        when(client.fetch(any(URI.class), any(HttpMethod.class), (Map<String, String>) any(Map.class),
                eq((String) null))).thenReturn(response);

        when(response.getStatus()).thenReturn(200);
        when(response.getResult()).thenReturn("{\"data\":{} }");
    }

    @Test
    public void ensureUrlSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/api/v1/")), eq(HttpMethod.GET),
                (Map<String, String>) any(Map.class), eq((String) null));
    }

    @Test
    public void ensureUrlWithSlashSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/api/v1/")), eq(HttpMethod.GET),
                (Map<String, String>) any(Map.class), eq((String) null));
    }

    @Test
    public void ensureUrlWithPathSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/proxy"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/proxy/api/v1/")), eq(HttpMethod.GET),
                (Map<String, String>) any(Map.class), eq((String) null));
    }

    @Test
    public void ensureUrlWithPathAndSlashSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/proxy/"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/proxy/api/v1/")), eq(HttpMethod.GET),
                (Map<String, String>) any(Map.class), eq((String) null));
    }

    @Test
    public void ensureBaseUrlSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void testCapabilitiesCall() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com"), "foo", "1.2.3.97", "bar");

        endpoint.getCapabilities(new AccessToken("abc"));

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/api/v1/capabilities")), eq(HttpMethod.GET),
                headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void testCapabilitiesCallWithPath() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/foo"), "foo", "1.2.3.97", "bar");

        endpoint.getCapabilities(new AccessToken("abc"));

        verify(client).fetch(eq(new URI("https://www.acrolinx.com/foo/api/v1/capabilities")), eq(HttpMethod.GET),
                headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com/foo", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void ensureBaseUrlWithSlashSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com/", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void ensureBaseUrlWithPathSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/proxy"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com/proxy", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void ensureBaseUrlWithPathAndSlashSetCorrectly() throws AcrolinxException, URISyntaxException, IOException
    {
        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/proxy/"), "foo", "1.2.3.97", "bar");

        endpoint.getPlatformInformation();

        verify(client).fetch(any(URI.class), eq(HttpMethod.GET), headersCaptor.capture(), eq((String) null));

        final Map<String, String> headers = headersCaptor.getValue();
        assertTrue(headers.containsKey("X-Acrolinx-Base-Url"));
        assertEquals("https://www.acrolinx.com/proxy/", headers.get("X-Acrolinx-Base-Url"));
    }

    @Test
    public void errorHandledInCaseOfNoResult() throws AcrolinxException, URISyntaxException, IOException
    {
        when(response.getStatus()).thenReturn(700);
        when(response.getResult()).thenReturn(null);

        endpoint = new AcrolinxEndpoint(client, new URI("https://www.acrolinx.com/proxy"), "foo", "1.2.3.97", "bar");

        try {
            endpoint.getPlatformInformation();
            fail("expected that resonse causes failure");
        } catch (final AcrolinxException e) {
            assertEquals("Fetch failed with status 700 and no result.", e.getMessage());
        }
    }
}