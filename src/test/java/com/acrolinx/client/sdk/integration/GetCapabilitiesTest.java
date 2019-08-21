/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.CustomFieldDescriptor;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.platform.Language;

public class GetCapabilitiesTest extends IntegrationTestBase
{
    @Before
    public void beforeTest()
    {
        assumeTrue(ACROLINX_API_TOKEN != null);
    }

    @Test
    public void testGetCapabilities() throws AcrolinxException
    {
        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN);
        assertNotNull("", capabilities.getCheckingCapabilities());

        List<GuidanceProfile> guidanceProfiles = capabilities.getCheckingCapabilities().getGuidanceProfiles();
        assertNotNull(guidanceProfiles);
        assertTrue("There should be some guidance profiles", guidanceProfiles.size() > 0);

        GuidanceProfile guidanceProfile = guidanceProfiles.get(0);
        assertNotNull(guidanceProfile.getId());
        assertNotNull(guidanceProfile.getDisplayName());

        Language language = guidanceProfile.getLanguage();
        assertNotNull(language);
        assertNotNull(language.getId());
        assertNotNull(language.getDisplayName());
    }

    @Test
    public void testGetCapablitiesExtended() throws AcrolinxException
    {

        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN);
        assertNotNull("", capabilities.getCheckingCapabilities());

        assertNotNull("", capabilities.getCheckingCapabilities().getCheckTypes());
        assertNotNull("", capabilities.getCheckingCapabilities().getContentEncodings());
        assertNotNull("", capabilities.getCheckingCapabilities().getContentFormats());
        assertNotNull("", capabilities.getCheckingCapabilities().getReferencePattern());
        assertNotNull("", capabilities.getCheckingCapabilities().getReportTypes());
        assertNotNull("", capabilities.getDocument());
        assertNotNull("", capabilities.getDocument().getCustomFields());

        for (CustomFieldDescriptor cf : capabilities.getDocument().getCustomFields()) {
            assertNotNull(cf.getDisplayName());
            assertNotNull(cf.getInputType());
            assertNotNull(cf.getKey());
            assertNotNull(cf.getPossibleValues());
            assertNotNull(cf.getType());
        }

    }

}
