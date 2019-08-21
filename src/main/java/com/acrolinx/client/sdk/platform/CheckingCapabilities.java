/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.platform;

import java.util.List;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckType;
import com.acrolinx.client.sdk.check.ReportType;

public class CheckingCapabilities
{
    private List<GuidanceProfile> guidanceProfiles;
    private List<ContentFormat> contentFormats;
    private List<CheckRequest.ContentEncoding> contentEncodings;
    private String referencePattern;
    private List<CheckType> checkTypes;
    private List<ReportType> reportTypes;

    public CheckingCapabilities(List<GuidanceProfile> guidanceProfiles, List<ContentFormat> contentFormats,
            List<CheckRequest.ContentEncoding> contentEncodings, String referencePattern, List<CheckType> checkTypes,
            List<ReportType> reportTypes)
    {
        this.guidanceProfiles = guidanceProfiles;
        this.contentFormats = contentFormats;
        this.contentEncodings = contentEncodings;
        this.referencePattern = referencePattern;
        this.checkTypes = checkTypes;
        this.reportTypes = reportTypes;
    }

    public List<GuidanceProfile> getGuidanceProfiles()
    {
        return guidanceProfiles;
    }

    public List<ContentFormat> getContentFormats()
    {
        return contentFormats;
    }

    public List<CheckRequest.ContentEncoding> getContentEncodings()
    {
        return contentEncodings;
    }

    public String getReferencePattern()
    {
        return referencePattern;
    }

    public List<CheckType> getCheckTypes()
    {
        return checkTypes;
    }

    public List<ReportType> getReportTypes()
    {
        return reportTypes;
    }
}
