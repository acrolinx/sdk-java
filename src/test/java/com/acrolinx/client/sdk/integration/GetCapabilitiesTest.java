/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.CheckingCapabilities;
import com.acrolinx.client.sdk.platform.CustomFieldDescriptor;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.platform.Language;

class GetCapabilitiesTest extends IntegrationTestBase
{
    @Test
    void testGetCapabilities() throws AcrolinxException
    {
        Capabilities capabilities = acrolinxEndpoint.getCapabilities(ACROLINX_API_TOKEN);
        assertNotNull(capabilities.getCheckingCapabilities());

        List<GuidanceProfile> guidanceProfiles = capabilities.getCheckingCapabilities().getGuidanceProfiles();
        assertTrue(guidanceProfiles.size() > 0);

        GuidanceProfile guidanceProfile = guidanceProfiles.get(0);
        assertNotNull(guidanceProfile.getId());
        assertNotNull(guidanceProfile.getDisplayName());

        Language language = guidanceProfile.getLanguage();
        assertNotNull(language.getId());
        assertNotNull(language.getDisplayName());
    }

    @Test
    void testGetCapablitiesExtended() throws AcrolinxException
    {
        Capabilities capabilities = acrolinxEndpoint.getCapabilities(ACROLINX_API_TOKEN);

        CheckingCapabilities checkingCapabilities = capabilities.getCheckingCapabilities();
        assertNotNull(checkingCapabilities.getCheckTypes());
        assertNotNull(checkingCapabilities.getContentEncodings());
        assertNotNull(checkingCapabilities.getContentFormats());
        assertNotNull(checkingCapabilities.getReferencePattern());
        assertNotNull(checkingCapabilities.getReportTypes());
        assertNotNull(capabilities.getDocument().getCustomFields());

        for (CustomFieldDescriptor customFieldDescriptor : capabilities.getDocument().getCustomFields()) {
            assertNotNull(customFieldDescriptor.getDisplayName());
            assertNotNull(customFieldDescriptor.getInputType());
            assertNotNull(customFieldDescriptor.getKey());
            String type = customFieldDescriptor.getType();

            if (type.equals("list")) {
                assertNotNull(customFieldDescriptor.getPossibleValues());
            }
        }
    }
}
