/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.platform.Language;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class GetCapabilitiesTest extends IntegrationTestBase {
    @Before
    public void beforeTest() {
        assumeTrue(ACROLINX_API_TOKEN != null);
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

    @Test
    public void testGetCapablitiesExtended() throws AcrolinxException, ExecutionException, InterruptedException {

        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN).get();
        assertNotNull("", capabilities.getCheckingCapabilities());

        assertNotNull("", capabilities.getCheckingCapabilities().getCheckTypes());
        assertNotNull("", capabilities.getCheckingCapabilities().getContentEncodings());
        assertNotNull("", capabilities.getCheckingCapabilities().getContentFormats());
        assertNotNull("", capabilities.getCheckingCapabilities().getReferencePattern());
        assertNotNull("", capabilities.getCheckingCapabilities().getReportTypes());

    }
}
