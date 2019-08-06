/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.check.*;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxRuntimeException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.internal.*;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.Link;
import com.google.common.base.Strings;
import org.apache.http.client.utils.URIBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.acrolinx.client.sdk.internal.JsonUtils.parseJson;

public class AcrolinxEndpoint {

    private String clientSignature;
    private String clientVersion;
    private String clientLocale;
    private URI acrolinxUri;

    private AcrolinxHttpClient httpClient;

    public AcrolinxEndpoint(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this(new ApacheHttpClient(), acrolinxURL, clientSignature, clientVersion, clientLocale);
    }

    public AcrolinxEndpoint(AcrolinxHttpClient httpClient, URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this.clientSignature = clientSignature;
        this.clientVersion = clientVersion;
        this.clientLocale = clientLocale;
        this.acrolinxUri = acrolinxURL;
        this.httpClient = httpClient;
    }

    public void close() throws IOException {
        this.httpClient.close();
    }

    public PlatformInformation getPlatformInformation() throws AcrolinxException {
        return fetchDataFromApiPath("", PlatformInformation.class, HttpMethod.GET, null, null, null);
    }

    public SignInSuccess signInWithSSO(String genericToken, String username) throws AcrolinxException {
        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("password", genericToken);
        extraHeaders.put("username", username);

        return fetchDataFromApiPath("auth/sign-ins", SignInSuccess.class, HttpMethod.POST, null, null, extraHeaders);
    }

    public SignInSuccess signInInteractive(InteractiveCallback callback) throws AcrolinxException, InterruptedException {
        return signInInteractive(callback, null, 15L * 60L * 1000L);
    }

    public SignInSuccess signInInteractive(final InteractiveCallback callback, AccessToken accessToken, Long timeoutMs) throws AcrolinxException, InterruptedException {
        final SignInResponse signInResponse;
        try {
            signInResponse = fetchFromApiPath("auth/sign-ins",
                    JsonUtils.getSerializer(SignInResponse.class), HttpMethod.POST, accessToken, null, null);

            if (signInResponse instanceof SignInResponse.Success) {
                return ((SignInResponse.Success) signInResponse).data;
            }

            SignInResponse.SignInLinks signInLinks = (SignInResponse.SignInLinks) signInResponse;
            callback.onInteractiveUrl(signInLinks.links.getInteractive());

            //An upper limit for polling.
            long endTime = System.currentTimeMillis() + timeoutMs;

            while (System.currentTimeMillis() < endTime) {
                SignInPollResponse pollResponse = fetchFromUrl(new URI(signInLinks.links.getPoll()), JsonUtils.getSerializer(SignInPollResponse.class),
                        HttpMethod.GET, null, null, null);
                if (pollResponse instanceof SignInPollResponse.Success) {
                    return ((SignInPollResponse.Success) pollResponse).data;
                }

                Progress progress = ((SignInPollResponse.Progress) pollResponse).progress;

                long sleepTimeMs = progress.getRetryAfterMs();
                Thread.sleep(sleepTimeMs);
            }
        } catch (AcrolinxException | URISyntaxException | IOException e) {
            throw new AcrolinxException(e);
        }
        throw new SignInException("Timeout");
    }

    public Capabilities getCapabilities(AccessToken accessToken) throws AcrolinxException {
        return fetchDataFromApiPath("capabilities", Capabilities.class, HttpMethod.GET, accessToken, null, null);
    }

    public CheckResponse check(AccessToken accessToken, CheckRequest checkRequest) throws AcrolinxException {
        return fetchFromApiPath("checking/checks", JsonUtils.getSerializer(CheckResponse.class), HttpMethod.POST,
                accessToken, JsonUtils.toJson(checkRequest), null);
    }

    public String getContentAnalysisDashboard(AccessToken accessToken, String batchId) throws AcrolinxException {
        ContentAnalysisDashboard contentAnalysisDashboard = fetchDataFromApiPath("checking/" + batchId + "/contentanalysis",
                ContentAnalysisDashboard.class, HttpMethod.GET, accessToken, null, null);


        for (Link link : contentAnalysisDashboard.getLinks()) {
            if (link.getLinkType().equals("shortWithoutAccessToken")) {
                return link.getLink();
            }
        }

        throw new AcrolinxException("Could not fetch content analysis dashboard");
    }

    public CheckResult checkAndGetResult(AccessToken accessToken, CheckRequest checkRequest, ProgressListener progressListener) throws AcrolinxException, InterruptedException {
        try {
            CheckResponse checkResponse = this.check(accessToken, checkRequest);
            return pollForResultWithCancelHandling(accessToken, progressListener, checkResponse);
        } catch (URISyntaxException | IOException e) {
            throw new AcrolinxException(e);
        }
    }

    private CheckResult pollForResultWithCancelHandling(AccessToken accessToken, ProgressListener progressListener, CheckResponse checkResponse) throws AcrolinxException, URISyntaxException, IOException, InterruptedException {
        try {
            return pollForCheckResult(accessToken, checkResponse, progressListener);
        } catch (InterruptedException e) {
            cancelCheck(accessToken, checkResponse);
            throw e;
        }
    }

    private CheckResult pollForCheckResult(AccessToken accessToken, CheckResponse checkResponse, ProgressListener progressListener)
            throws AcrolinxException, URISyntaxException, IOException, InterruptedException {
        URI pollUrl = new URI(checkResponse.getLinks().getResult());
        while (true) {
            CheckPollResponse pollResponse = fetchFromUrl(pollUrl, JsonUtils.getSerializer(CheckPollResponse.class),
                    HttpMethod.GET, accessToken, null, null);
            if (pollResponse instanceof CheckPollResponse.Success) {
                return ((CheckPollResponse.Success) pollResponse).data;
            }
            Progress progress = ((CheckPollResponse.Progress) pollResponse).progress;
            progressListener.onProgress(progress);

            long sleepTimeMs = progress.getRetryAfterMs();
            Thread.sleep(sleepTimeMs);
        }
    }

    private CheckCancelledResponse cancelCheck(AccessToken accessToken, CheckResponse checkResponse) throws URISyntaxException, IOException, AcrolinxException {
        return this.fetchFromUrl(new URI(checkResponse.getLinks().getCancel()), JsonUtils.getSerializer(CheckCancelledResponse.class), HttpMethod.DELETE, accessToken, null, null);
    }

    @SuppressWarnings("unchecked")
    private <T> T fetchDataFromApiPath(String apiPath,
                                       Class<T> clazz,
                                       HttpMethod method,
                                       AccessToken accessToken,
                                       String body,
                                       Map<String, String> extraHeaders
    ) throws AcrolinxException {
        return (T) fetchFromApiPath(apiPath, JsonUtils.getSerializer(SuccessResponse.class, clazz), method, accessToken,
                body, extraHeaders).data;

    }

    private <T> T fetchFromApiPath(
            String apiPath,
            JsonDeserializer<T> deserializer,
            HttpMethod method,
            AccessToken accessToken,
            String body,
            Map<String, String> extraHeaders
    ) throws AcrolinxException {
        try {
            URI uri = new URIBuilder().setScheme(acrolinxUri.getScheme()).setPort(acrolinxUri.getPort())
                    .setHost(acrolinxUri.getHost()).setPath(acrolinxUri.getPath() + "api/v1/" + apiPath).build();
            return fetchFromUrl(uri, deserializer, method, accessToken, body, extraHeaders);
        } catch (IOException | URISyntaxException e) {
            throw new AcrolinxException(e);
        }
    }

    private <T> T fetchFromUrl(
            final URI uri,
            final JsonDeserializer<T> deserializer,
            final HttpMethod method,
            AccessToken accessToken,
            String body,
            @Nullable Map<String, String> extraHeaders
    ) throws IOException, AcrolinxException {
        Map<String, String> headers = getCommonHeaders(accessToken);
        if (extraHeaders != null) {
            headers.putAll(extraHeaders);
        }

        final AcrolinxResponse acrolinxHttpResponse = httpClient.fetch(uri, method, headers, body);
        validateHttpResponse(acrolinxHttpResponse, uri, method);
        return deserializer.deserialize(acrolinxHttpResponse.getResult());
    }

    /**
     * Throws an exception if the acrolinxHttpResponse indicates an error.
     *
     * @throws AcrolinxServiceException
     * @throws RuntimeException
     */
    private static void validateHttpResponse(AcrolinxResponse acrolinxHttpResponse, URI uri, HttpMethod method) throws AcrolinxServiceException {
        int statusCode = acrolinxHttpResponse.getStatus();
        if (statusCode >= 200 && statusCode < 300) {
            // Should we still check if there is an error?
            return;
        }

        String responseText = acrolinxHttpResponse.getResult();

        if (Strings.isNullOrEmpty(responseText)) {
            throw new AcrolinxRuntimeException("Fetch failed with status " + statusCode + " and no result.");
        }

        ErrorResponse.AcrolinxServiceError acrolinxServiceError;
        try {
            acrolinxServiceError = parseJson(responseText, ErrorResponse.class).error;
        } catch (RuntimeException e) {
            throw new AcrolinxRuntimeException("Fetch failed with status " + statusCode +
                    " and unexpected result\"" + responseText + "\".");
        }

        throw new AcrolinxServiceException(acrolinxServiceError, new AcrolinxServiceException.HttpRequest(uri, method));
    }

    private Map<String, String> getCommonHeaders(AccessToken accessToken) {
        HashMap<String, String> headersMap = new HashMap<>();

        if (accessToken != null && !accessToken.isEmpty()) {
            headersMap.put("X-Acrolinx-Auth", accessToken.getAccessToken());
        }
        headersMap.put("X-Acrolinx-Base-Url", this.acrolinxUri.toString());
        headersMap.put("X-Acrolinx-Client-Locale", this.clientLocale);
        headersMap.put("X-Acrolinx-Client", this.clientSignature + "; " + this.clientVersion);

        return headersMap;
    }
}