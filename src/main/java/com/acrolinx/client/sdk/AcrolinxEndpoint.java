package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.SSOException;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.http.AcrolinxHttpClient;
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
import java.util.concurrent.ExecutionException;

public class AcrolinxEndpoint {

    private String clientSignature;
    private String clientVersion;
    private String clientLocale;
    private URI acrolinxUri;

    private AcrolinxHttpClient httpClient = new ApacheHttpClient();

    // Constructor with lots of params?
    public AcrolinxEndpoint(URI acrolinxURL, String clientSignature, String clientVersion, String clientLocale) {
        this.clientSignature = clientSignature;
        this.clientVersion = clientVersion;
        this.clientLocale = clientLocale;
        this.acrolinxUri = acrolinxURL;
    }

    public PlatformInformation getPlatformInformation() throws AcrolinxException {
        return fetchDataFromApiPath("", PlatformInformation.class, HttpMethod.GET, null, null, null);
    }

    public SignInSuccess signInWithSSO(String genericToken, String username) throws SSOException {
        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("password", genericToken);
        extraHeaders.put("username", username);
        try {
            return fetchDataFromApiPath("auth/sign-ins", SignInSuccess.class, HttpMethod.POST, null, null, extraHeaders);
        } catch (Exception e) {
            throw new SSOException();
        }
    }

    public SignInSuccess signInInteractive(InteractiveCallback callback) throws SignInException {
        return signInInteractive(callback, null);
    }

    public SignInSuccess signInInteractive(InteractiveCallback callback, AccessToken accessToken) throws SignInException {
        return signInInteractive(callback, accessToken, 60L * 60L * 1000L);
    }

    public SignInSuccess signInInteractive(InteractiveCallback callback, AccessToken accessToken, long timeoutMs) throws SignInException {
        try {
            SignInResponse signInResponse = fetchFromApiPath("auth/sign-ins", JsonUtils.getSerializer(SignInResponse.class),
                    HttpMethod.POST, accessToken, null, null);

            if (signInResponse instanceof SignInResponse.Success) {
                return ((SignInResponse.Success) signInResponse).data;
            }
            SignInResponse.SignInLinks signInLinks = (SignInResponse.SignInLinks) signInResponse;

            callback.onInteractiveUrl(signInLinks.links.getInteractive());

            return pollForInteractiveSignIn(signInLinks.links, timeoutMs);

        } catch (AcrolinxException | InterruptedException | URISyntaxException | IOException e) {
            throw new SignInException(e);
        }
    }

    private SignInSuccess pollForInteractiveSignIn(SignInResponse.SignInLinksInternal signInLinks, long timeoutMs) throws  AcrolinxException, InterruptedException, URISyntaxException, IOException {
        long endTime = System.currentTimeMillis() + timeoutMs;

        while (System.currentTimeMillis() < endTime) {
            SignInPollResponse pollResponse = fetchFromUrl(new URI(signInLinks.getPoll()), JsonUtils.getSerializer(SignInPollResponse.class),
                    HttpMethod.GET, null, null, null);
            if (pollResponse instanceof SignInPollResponse.Success) {
                return ((SignInPollResponse.Success) pollResponse).data;
            }
            ProgressInternal progress = ((SignInPollResponse.Progress) pollResponse).progress;

            long sleepTimeMs = progress.getRetryAfterMs();
            if (System.currentTimeMillis() + sleepTimeMs > endTime) {
                break;
            }
            Thread.sleep(sleepTimeMs);
        }

        throw new SignInException("Timeout");
    }

    public Capabilities getCapabilities(AccessToken accessToken) throws AcrolinxException {
        return fetchDataFromApiPath("capabilities", Capabilities.class, HttpMethod.GET, accessToken, null, null);
    }

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
            URI uri,
            JsonDeserializer<T> deserializer,
            HttpMethod method,
            AccessToken accessToken,
            String body,
            @Nullable Map<String, String> extraHeaders
    ) throws IOException, AcrolinxException {
        HashMap<String, String> headers = getCommonHeaders(accessToken);
        if (extraHeaders != null) {
            headers.putAll(extraHeaders);
        }

        String jsonString = null;
        try {
            jsonString = httpClient.fetch(uri, method, headers, body).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AcrolinxException(e);
        }
        return deserializer.deserialize(jsonString);
    }

    private HashMap<String, String> getCommonHeaders(AccessToken accessToken) {
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