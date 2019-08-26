/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

public class CheckOptions
{
    final private String guidanceProfileId;
    final private String batchId;
    final private List<ReportType> reportTypes;
    final private CheckType checkType;
    final private String contentFormat;
    final private String languageId;
    final private boolean disableCustomFieldValidation;

    CheckOptions(String guidanceProfileId, String batchId, List<ReportType> reportTypes, CheckType checkType,
            String contentFormat, String languageId, boolean disableCustomFieldValidation)
    {
        this.guidanceProfileId = guidanceProfileId;
        this.batchId = batchId;
        this.reportTypes = reportTypes;
        this.checkType = checkType;
        this.contentFormat = contentFormat;
        this.languageId = languageId;
        this.disableCustomFieldValidation = disableCustomFieldValidation;
    }

    public static CheckOptionsBuilder getBuilder()
    {
        return new CheckOptionsBuilder();
    }

    public String getGuidanceProfileId()
    {
        return guidanceProfileId;
    }

    public String getBatchId()
    {
        return batchId;
    }

    public List<ReportType> getReportTypes()
    {
        return reportTypes;
    }

    public CheckType getCheckType()
    {
        return checkType;
    }

    public String getContentFormat()
    {
        return contentFormat;
    }

    public String getLanguageId()
    {
        return languageId;
    }

    public boolean isDisableCustomFieldValidation()
    {
        return disableCustomFieldValidation;
    }
}
