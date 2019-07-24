package com.acrolinx.client.sdk.platform;

import java.util.List;

public class CheckingCapabilities
{
    private List<GuidanceProfile> guidanceProfiles;

    public CheckingCapabilities(List<GuidanceProfile> guidanceProfiles) {
        this.guidanceProfiles = guidanceProfiles;
    }

    public List<GuidanceProfile> getGuidanceProfiles() {
        return guidanceProfiles;
    }
}
