/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk;

import static com.acrolinx.client.sdk.internal.JsonUtils.parseJson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.client.sdk.check.CheckCancelledResponse;
import com.acrolinx.client.sdk.check.CheckPollResponse;
import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckResponse;
import com.acrolinx.client.sdk.check.CheckResult;
import com.acrolinx.client.sdk.check.ProgressListener;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.http.RewritingHttpClientDecorator;
import com.acrolinx.client.sdk.internal.ErrorResponse;
import com.acrolinx.client.sdk.internal.JsonDeserializer;
import com.acrolinx.client.sdk.internal.JsonUtils;
import com.acrolinx.client.sdk.internal.SignInPollResponse;
import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.internal.SuccessResponse;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.Link;
import com.google.common.base.Strings;

public class AcrolinxEndpoint
{
    private static final Logger logger = LoggerFactory.getLogger(AcrolinxEndpoint.class);

    private final String signature;
    private final String clientVersion;
    private final String clientLocale;
    private final URI acrolinxUri;
    private final AcrolinxHttpClient httpClient;

    /**
     *
     * @param acrolinxURL URL to your Acrolinx Platform for example: https://yourcompany.acrolinx.com
     * @param signature License to use integration with Acrolinx.
     * @param clientVersion Version number of your Acrolinx Integration for example: 1.2.5.34
     * @param clientLocale Locale of environment in which the integration is deployed.
     */
    public AcrolinxEndpoint(URI acrolinxURL, String signature, String clientVersion, String clientLocale)
    {
        this(new ApacheHttpClient(), acrolinxURL, signature, clientVersion, clientLocale);
    }

    /**
     * Use this constructor in case the URL that is returned to the user have to differ to the URL the
     * integration connects to.
     * 
     * Examples:
     * <ul>
     * <li>An SSO-proxy is used to access the Scorecard and the Content Analysis dashboard, but the SDK
     * in an automated environment isn’t able to use the same route.
     * https://yourcompany.myintegration.com/proxy/ vs. https://yourcompany.acrolinx.com.</li>
     * <li>An integration can connect directly to the Acrolinx Platform using an internal URL, where a
     * user has to access it using an external URL. http://localhost:8031 vs.
     * https://yourcompany.acrolinx.com.</li>
     * </ul>
     * 
     * @param sdkAcrolinxURL URL to your Acrolinx Platform for example: https://yourcompany.acrolinx.com
     * @param userFacingAcrolinxURL URL to your Acrolinx Platform that is returned to the user:
     *        https://yourcompany.myintegration.com/proxy/
     * @param signature License to use integration with Acrolinx.
     * @param clientVersion Version number of your Acrolinx Integration for example: 1.2.5.34
     * @param clientLocale Locale of environment in which the integration is deployed.
     */
    public AcrolinxEndpoint(URI sdkAcrolinxURL, URI userFacingAcrolinxURL, String signature, String clientVersion,
            String clientLocale)
    {
        this(new RewritingHttpClientDecorator(new ApacheHttpClient(), userFacingAcrolinxURL, sdkAcrolinxURL),
                userFacingAcrolinxURL, signature, clientVersion, clientLocale);
    }

    /**
     *
     * @param httpClient Provide your own http Acrolinx Integration implementing
     *        {@link AcrolinxHttpClient} interface
     * @param acrolinxURL URL to your Acrolinx Platform for example: https://yourcompany.acrolinx.com
     * @param signature License to use integration with Acrolinx.
     * @param clientVersion Version number of your Acrolinx Integration for example: 1.2.5.37
     * @param clientLocale Locale of environment in which the integration is deployed.
     */
    public AcrolinxEndpoint(AcrolinxHttpClient httpClient, URI acrolinxURL, String signature, String clientVersion,
            String clientLocale)
    {
        this.signature = signature;
        this.clientVersion = clientVersion;
        this.clientLocale = clientLocale;
        this.acrolinxUri = acrolinxURL;
        this.httpClient = httpClient;
    }

    /**
     * Throws an exception if the acrolinxHttpResponse indicates an error.
     *
     * @throws AcrolinxServiceException Acrolinx specific to Http Client.
     * @throws RuntimeException JSON parsing failure.
     */
    private static void validateHttpResponse(AcrolinxResponse acrolinxHttpResponse, URI uri, HttpMethod method)
            throws AcrolinxException
    {
        int statusCode = acrolinxHttpResponse.getStatus();
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        String responseText = acrolinxHttpResponse.getResult();
        if (Strings.isNullOrEmpty(responseText)) {
            logger.debug("Response is empty");
            throw new AcrolinxException("Fetch failed with status " + statusCode + " and no result.");
        }

        ErrorResponse.AcrolinxServiceError acrolinxServiceError;

        try {
            logger.debug("Error response text: {}", responseText);
            acrolinxServiceError = parseJson(responseText, ErrorResponse.class).error;
            if (acrolinxServiceError == null) {
                logger.error("Unable to parse JSON response");
                throw new AcrolinxException("Invalid error class generated");
            }
        } catch (RuntimeException e) {
            logger.debug("Invalid JSON exception occurred. ");
            throw new AcrolinxException("Fetch failed with status " + statusCode + " and unexpected result\""
                    + responseText + "\"." + e.getMessage() + "\".");
        }

        throw new AcrolinxServiceException(acrolinxServiceError, new AcrolinxServiceException.HttpRequest(uri, method));
    }

    /**
     * Close Endpoint after use.
     */
    public void close() throws IOException
    {
        logger.info("Endpoint terminated");
        this.httpClient.close();
    }

    /**
     *
     * @return Information about Platform version and locales supported
     */
    public PlatformInformation getPlatformInformation() throws AcrolinxException
    {
        return fetchDataFromApiPath("", PlatformInformation.class, HttpMethod.GET, null, null, null);
    }

    /**
     * Single sign-on
     *
     * @param genericToken Generic SSO token configured on your Acrolinx Platform.
     * @param username User who wants to authenticate
     * @return SignInSuccess holds the access token that is required to initiate check
     */
    public SignInSuccess signInWithSSO(String genericToken, String username) throws AcrolinxException
    {
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("password", genericToken);
        extraHeaders.put("username", username);

        return fetchDataFromApiPath("auth/sign-ins", SignInSuccess.class, HttpMethod.POST, null, null, extraHeaders);
    }

    /**
     * An interactive sign-in where user can open a URL in a browser to sign in.
     * 
     * @param callback Provide a method that can be called to open sign-in link in the browser.
     * @return SignInSuccess holds the access token that is required to initiate check
     */
    public SignInSuccess signInInteractive(InteractiveCallback callback) throws AcrolinxException, InterruptedException
    {
        return signInInteractive(callback, null, 30L * 60L * 1000L);
    }

    /**
     * An interactive sign-in where user can open URL in a browser to sign in.
     *
     * @param callback Provide a method that can be called to open sign-in link in the browser.
     * @param accessToken Provide an already available access to check its validity
     * @param timeoutMs Provide timeout in milliseconds
     * @return SignInSuccess holds the access token that is required to initiate check
     */
    public SignInSuccess signInInteractive(final InteractiveCallback callback, AccessToken accessToken, long timeoutMs)
            throws AcrolinxException, InterruptedException
    {
        final SignInResponse signInResponse;
        try {
            signInResponse = fetchFromApiPath("auth/sign-ins", JsonUtils.getSerializer(SignInResponse.class),
                    HttpMethod.POST, accessToken, null, null);

            if (signInResponse instanceof SignInResponse.Success) {
                logger.debug("Signed In with already available access token.");
                return ((SignInResponse.Success) signInResponse).data;
            }

            SignInResponse.SignInLinks signInLinks = (SignInResponse.SignInLinks) signInResponse;
            callback.onInteractiveUrl(signInLinks.links.getInteractive());
            logger.debug("Sigin In link provided. Polling until user signs in dashboard.");

            // An upper limit for polling.
            long endTime = System.currentTimeMillis() + timeoutMs;

            while (System.currentTimeMillis() < endTime) {
                SignInPollResponse pollResponse = fetchFromUrl(new URI(signInLinks.links.getPoll()),
                        JsonUtils.getSerializer(SignInPollResponse.class), HttpMethod.GET, null, null, null);
                if (pollResponse instanceof SignInPollResponse.Success) {
                    return ((SignInPollResponse.Success) pollResponse).data;
                }
                logger.debug("Poll response: {}", pollResponse);
                Progress progress = ((SignInPollResponse.Progress) pollResponse).progress;
                logger.debug("SignIn polling: {}", progress.getPercent());

                long sleepTimeMs = progress.getRetryAfterMs();
                Thread.sleep(sleepTimeMs);
            }
        } catch (AcrolinxException | URISyntaxException | IOException e) {
            logger.debug("Sign-in failed {}", e.getMessage());
            throw new AcrolinxException(e);
        }
        throw new SignInException("Timeout");
    }

    /**
     * Get check capabilities of the Platform
     * 
     * @param accessToken Provide access token obtained from Sign-in Success Response or Acrolinx
     *        dashboard
     * @return Checking capabilities available for Platform
     */
    public Capabilities getCapabilities(AccessToken accessToken) throws AcrolinxException
    {
        return fetchDataFromApiPath("capabilities", Capabilities.class, HttpMethod.GET, accessToken, null, null);
    }

    /**
     * Submit a check request
     *
     * @param accessToken Provide access token obtained from Sign-in Success Response or Acrolinx
     *        dashboard.
     * @param checkRequest Use CheckRequestBuilder to simplify building check request.
     * @return Check response contains the URL to fetch check result.
     */
    public CheckResponse submitCheck(AccessToken accessToken, CheckRequest checkRequest) throws AcrolinxException
    {
        return fetchFromApiPath("checking/checks", JsonUtils.getSerializer(CheckResponse.class), HttpMethod.POST,
                accessToken, JsonUtils.toJson(checkRequest), null);
    }

    /**
     * Get URL to Content Analysis dashboard
     *
     * @param accessToken Provide access token obtained from Sign-in Success Response or Acrolinx
     *        dashboard
     * @param batchId Provide a batch Id submitted for performing a check
     * @return String contains URL to Content Analysis dashboard.
     */
    public String getContentAnalysisDashboard(AccessToken accessToken, String batchId) throws AcrolinxException
    {
        ContentAnalysisDashboard contentAnalysisDashboard = fetchDataFromApiPath(
                "checking/" + batchId + "/contentanalysis", ContentAnalysisDashboard.class, HttpMethod.GET, accessToken,
                null, null);

        for (Link link : contentAnalysisDashboard.getLinks()) {
            if (link.getLinkType().equals("shortWithoutAccessToken")) {
                return link.getLink();
            }
        }
        logger.debug("Failed to fetch Content Analysis dashboard.");
        throw new AcrolinxException("Could not fetch Content Analysis dashboard.");
    }

    /**
     * Submits a check and waits until result is obtained.
     * 
     * @param accessToken Provide access token obtained from Sign-in Success Response or Acrolinx
     *        dashboard
     * @param checkRequest Use CheckRequestBuilder to simplify building check request.
     * @param progressListener Provides statistical information about check progress
     * @return Acrolinx Scorecard for the checked content.
     */
    public CheckResult check(AccessToken accessToken, CheckRequest checkRequest, ProgressListener progressListener)
            throws AcrolinxException
    {
        CheckResponse checkResponse = this.submitCheck(accessToken, checkRequest);
        logger.debug("Submitted check. Polling for result started.");
        try {
            return pollForResultWithCancelHandling(accessToken, progressListener, checkResponse);
        } catch (URISyntaxException | IOException e) {
            logger.debug("Pollong for check result failed");
            throw new AcrolinxException(e);
        }
    }

    public CheckResult check(AccessToken accessToken, CheckRequest checkRequest) throws AcrolinxException
    {
        CheckResponse checkResponse = this.submitCheck(accessToken, checkRequest);
        logger.debug("Submitted check. Polling for result started.");
        try {
            return pollForResultWithCancelHandling(accessToken, null, checkResponse);
        } catch (URISyntaxException | IOException e) {
            logger.debug("Pollong for check result failed");
            throw new AcrolinxException(e);
        }
    }

    private CheckResult pollForResultWithCancelHandling(AccessToken accessToken,
            @Nullable ProgressListener progressListener, CheckResponse checkResponse)
            throws AcrolinxException, URISyntaxException, IOException
    {
        try {
            return pollForCheckResult(accessToken, checkResponse, progressListener);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Polling interrupted. Check will be cancelled");
            cancelCheck(accessToken, checkResponse);
            throw new AcrolinxException("Polling interrupted. Cancelled check.");
        }
    }

    private CheckResult pollForCheckResult(AccessToken accessToken, CheckResponse checkResponse,
            @Nullable ProgressListener progressListener)
            throws AcrolinxException, URISyntaxException, IOException, InterruptedException
    {
        URI pollUri = new URI(checkResponse.getLinks().getResult());

        while (true) {
            CheckPollResponse pollResponse = fetchFromUrl(pollUri, JsonUtils.getSerializer(CheckPollResponse.class),
                    HttpMethod.GET, accessToken, null, null);
            if (pollResponse instanceof CheckPollResponse.Success) {
                return ((CheckPollResponse.Success) pollResponse).data;
            }
            Progress progress = ((CheckPollResponse.Progress) pollResponse).progress;
            if (progressListener != null) {
                progressListener.onProgress(progress);
            }

            logger.debug("Polling for check result. Progress: {}", progress.getPercent());

            long sleepTimeMs = progress.getRetryAfterMs();
            Thread.sleep(sleepTimeMs);
        }
    }

    private CheckCancelledResponse cancelCheck(AccessToken accessToken, CheckResponse checkResponse)
            throws URISyntaxException, IOException, AcrolinxException
    {
        return this.fetchFromUrl(new URI(checkResponse.getLinks().getCancel()),
                JsonUtils.getSerializer(CheckCancelledResponse.class), HttpMethod.DELETE, accessToken, null, null);
    }

    private <T> T fetchDataFromApiPath(String apiPath, Class<T> clazz, HttpMethod method, AccessToken accessToken,
            String body, Map<String, String> extraHeaders) throws AcrolinxException
    {
        return (T) fetchFromApiPath(apiPath, JsonUtils.getSerializer(SuccessResponse.class, clazz), method, accessToken,
                body, extraHeaders).data;
    }

    private <T> T fetchFromApiPath(String apiPath, JsonDeserializer<T> deserializer, HttpMethod method,
            AccessToken accessToken, String body, Map<String, String> extraHeaders) throws AcrolinxException
    {
        URI uri;
        try {
            uri = new URIBuilder().setScheme(acrolinxUri.getScheme()).setPort(
                    acrolinxUri.getPort()).setHost(acrolinxUri.getHost()).setPath(acrolinxUri.getPath()
                            + (acrolinxUri.getPath().length() == 0 || acrolinxUri.getPath().endsWith("/") ? "" : "/")
                            + "api/v1/" + apiPath).build();
        } catch (URISyntaxException e) {
            throw new AcrolinxException("Uri creation failed, apiPath: " + apiPath + ", acrolinxUri: " + acrolinxUri,
                    e);
        }

        try {
            return fetchFromUrl(uri, deserializer, method, accessToken, body, extraHeaders);
        } catch (IOException e) {
            throw new AcrolinxException("fetch from URL failed: " + uri, e);
        }
    }

    private <T> T fetchFromUrl(final URI uri, final JsonDeserializer<T> deserializer, final HttpMethod method,
            AccessToken accessToken, String body, @Nullable Map<String, String> extraHeaders)
            throws IOException, AcrolinxException
    {
        Map<String, String> headers = getCommonHeaders(accessToken);
        if (extraHeaders != null) {
            headers.putAll(extraHeaders);
        }

        final AcrolinxResponse acrolinxHttpResponse = httpClient.fetch(uri, method, headers, body);
        validateHttpResponse(acrolinxHttpResponse, uri, method);
        return deserializer.deserialize(acrolinxHttpResponse.getResult());
    }

    /**
     * Check if document you wish to check is supported by Platform
     * 
     * @param documentType Type of your document for example: .xml, .md, .dita
     * @param accessToken Provide access token obtained from Sign-in Success Response or Acrolinx
     *        dashboard
     * @return true if document type is checkable and false if not.
     */
    public boolean isDocumentTypeCheckable(String documentType, AccessToken accessToken) throws AcrolinxException
    {
        Capabilities capabilities = this.getCapabilities(accessToken);
        String referencePattern = capabilities.getCheckingCapabilities().getReferencePattern();
        logger.debug("Refrence Pattern: {}", referencePattern);
        return documentType.matches(referencePattern);
    }

    private Map<String, String> getCommonHeaders(AccessToken accessToken)
    {
        Map<String, String> headersMap = new HashMap<>();

        if (accessToken != null && !accessToken.isEmpty()) {
            headersMap.put("X-Acrolinx-Auth", accessToken.getAccessTokenAsString());
        }
        headersMap.put("X-Acrolinx-Base-Url", this.acrolinxUri.toString());
        headersMap.put("X-Acrolinx-Client-Locale", this.clientLocale);
        headersMap.put("X-Acrolinx-Client", this.signature + "; " + this.clientVersion);

        return headersMap;
    }
}
