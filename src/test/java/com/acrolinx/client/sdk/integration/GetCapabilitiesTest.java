package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.platform.Language;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.acrolinx.client.sdk.integration.CommonTestSetup.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class GetCapabilitiesTest {
    private AcrolinxEndpoint endpoint;

    @Before
    public void beforeTest() throws URISyntaxException {
        assumeTrue(ACROLINX_URL != null);
        assumeTrue(ACROLINX_API_TOKEN != null);
        endpoint = createTestAcrolinxEndpoint();
    }

    @Test
    public void testGetCapabilities() throws AcrolinxException, InterruptedException, ExecutionException {
        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN).get();
        assertNotNull("", capabilities.getCheckingCapabilities());

        List<GuidanceProfile> guidanceProfiles = capabilities.getCheckingCapabilities().getGuidanceProfiles();
        assertNotNull(guidanceProfiles);
        assertTrue(guidanceProfiles.size() > 0);

        GuidanceProfile guidanceProfile = guidanceProfiles.get(0);
        assertNotNull(guidanceProfile.getId());
        assertNotNull(guidanceProfile.getDisplayName());

        Language language = guidanceProfile.getLanguage();
        assertNotNull(language);
        assertNotNull(language.getId());
        assertNotNull(language.getDisplayName());

    }
}
