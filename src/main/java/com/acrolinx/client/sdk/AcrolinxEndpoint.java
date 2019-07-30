package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.check.CheckPollResponse;
import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckResponse;
import com.acrolinx.client.sdk.check.CheckResult;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxRuntimeException;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.internal.*;
import com.acrolinx.client.sdk.platform.Capabilities;
import org.apache.http.client.utils.URIBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class AcrolinxEndpoint {

    private String clientSignature;
    private String clientVersion;
    private String clientLocale;
    private URI acrolinxUri;

    private AcrolinxHttpClient httpClient;

    public AcrolinxEndpoint(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this.clientSignature = clientSignature;
        this.clientVersion = clientVersion;
        this.clientLocale = clientLocale;
        this.acrolinxUri = acrolinxURL;
        this.httpClient = new ApacheHttpClient();
        this.httpClient.start();
    }

    public AcrolinxEndpoint(AcrolinxHttpClient httpClient, URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this.clientSignature = clientSignature;
        this.clientVersion = clientVersion;
        this.clientLocale = clientLocale;
        this.acrolinxUri = acrolinxURL;
        this.httpClient = httpClient;
        this.httpClient.start();

    }

    public void close() throws IOException {
        this.httpClient.close();
    }

    public PlatformInformation getPlatformInformation() throws AcrolinxException {
        try {
            return fetchDataFromApiPath("", PlatformInformation.class, HttpMethod.GET, null, null, null).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AcrolinxException(e);
        }
    }

    public Future<SignInSuccess> signInWithSSO(String genericToken, String username) throws AcrolinxException {
        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("password", genericToken);
        extraHeaders.put("username", username);

        try {
            return fetchDataFromApiPath("auth/sign-ins", SignInSuccess.class, HttpMethod.POST, null, null, extraHeaders);
        } catch (Exception e) {
            throw new AcrolinxException(e);
        }
    }

    public Future<SignInSuccess> signInInteractive(InteractiveCallback callback) throws SignInException {
        return signInInteractive(callback, null);
    }

    public Future<SignInSuccess> signInInteractive(final InteractiveCallback callback, AccessToken accessToken) throws SignInException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        SignInInteractiveWithPolling poller = new SignInInteractiveWithPolling(accessToken, callback);
        Future<SignInSuccess> signInSuccessFuture = executor.submit(poller);
        executor.shutdown();
        return signInSuccessFuture;
    }

    public Future<Capabilities> getCapabilities(AccessToken accessToken) throws AcrolinxException {
        return fetchDataFromApiPath("capabilities", Capabilities.class, HttpMethod.GET, accessToken, null, null);
    }

    public Future<CheckResponse> check(AccessToken accessToken, CheckRequest checkRequest) throws AcrolinxException {
        return fetchFromApiPath("checking/checks", JsonUtils.getSerializer(CheckResponse.class), HttpMethod.POST,
                accessToken, JsonUtils.toJson(checkRequest), null);
    }

    public CheckResult pollForCheckResult(AccessToken accessToken, CheckResponse checkResponse)
            throws AcrolinxException, URISyntaxException, IOException, ExecutionException, InterruptedException {
        URI pollUrl = new URI(checkResponse.getLinks().getResult());
        while (true) {
            CheckPollResponse pollResponse = fetchFromUrl(pollUrl, JsonUtils.getSerializer(CheckPollResponse.class),
                    HttpMethod.GET, accessToken, null, null).get();
            if (pollResponse instanceof CheckPollResponse.Success) {
                return ((CheckPollResponse.Success) pollResponse).data;
            }
            Progress progress = ((CheckPollResponse.Progress) pollResponse).progress;

            long sleepTimeMs = progress.getRetryAfterMs();
            Thread.sleep(sleepTimeMs);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Future<T> fetchDataFromApiPath(String apiPath,
                                               Class<T> clazz,
                                               HttpMethod method,
                                               AccessToken accessToken,
                                               String body,
                                               Map<String, String> extraHeaders
    ) throws AcrolinxException {
        final Future<SuccessResponse> successResponse = fetchFromApiPath(apiPath, JsonUtils.getSerializer(SuccessResponse.class, clazz), method, accessToken,
                body, extraHeaders);

        return new FutureMapper<SuccessResponse, T>(successResponse) {
            @Override
            protected T map(SuccessResponse wrappedFutureResult) {
                return (T) wrappedFutureResult.data;
            }
        };
    }

    private <T> Future<T> fetchFromApiPath(
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

    private <T> Future<T> fetchFromUrl(
            URI uri,
            final JsonDeserializer<T> deserializer,
            HttpMethod method,
            AccessToken accessToken,
            String body,
            @Nullable Map<String, String> extraHeaders
    ) throws IOException, AcrolinxException {
        Map<String, String> headers = getCommonHeaders(accessToken);
        if (extraHeaders != null) {
            headers.putAll(extraHeaders);
        }

        final Future<AcrolinxResponse> acrolinxResponse = httpClient.fetch(uri, method, headers, body);

        return new FutureMapper<AcrolinxResponse, T>(acrolinxResponse) {
            @Override
            protected T map(AcrolinxResponse acrolinxHttpResponse) {
                int statusCode = acrolinxHttpResponse.getStatus();
                if (statusCode < 200 || statusCode > 299) {
                    throw new AcrolinxRuntimeException("Fetch failed: " + statusCode);
                }
                return deserializer.deserialize(acrolinxHttpResponse.getResult());
            }
        };
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

    private class SignInInteractiveWithPolling implements Callable<SignInSuccess> {

        private AccessToken accessToken;
        private InteractiveCallback callback;

        public SignInInteractiveWithPolling(AccessToken accessToken, InteractiveCallback callback) {
            this.accessToken = accessToken;
            this.callback = callback;
        }

        @Override
        public SignInSuccess call() throws Exception {

            final SignInResponse signInResponse = fetchFromApiPath("auth/sign-ins",
                    JsonUtils.getSerializer(SignInResponse.class), HttpMethod.POST, this.accessToken, null, null).get();

            if (signInResponse instanceof SignInResponse.Success) {
                return ((SignInResponse.Success) signInResponse).data;
            }

            SignInResponse.SignInLinks signInLinks = (SignInResponse.SignInLinks) signInResponse;
            callback.onInteractiveUrl(signInLinks.links.getInteractive());

            while (true) {
                SignInPollResponse pollResponse = fetchFromUrl(new URI(signInLinks.links.getPoll()), JsonUtils.getSerializer(SignInPollResponse.class),
                        HttpMethod.GET, null, null, null).get();
                if (pollResponse instanceof SignInPollResponse.Success) {
                    return ((SignInPollResponse.Success) pollResponse).data;
                }

                Progress progress = ((SignInPollResponse.Progress) pollResponse).progress;

                long sleepTimeMs = progress.getRetryAfterMs();
                Thread.sleep(sleepTimeMs);
            }
        }
    }
}