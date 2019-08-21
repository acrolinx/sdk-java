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

    CheckOptions(CheckOptionsBuilder checkOptionsBuilder)
    {
        this.guidanceProfileId = checkOptionsBuilder.getGuidanceProfileId();
        this.batchId = checkOptionsBuilder.getBatchId();
        this.reportTypes = checkOptionsBuilder.getReportTypes();
        this.checkType = checkOptionsBuilder.getCheckType();
        this.contentFormat = checkOptionsBuilder.getContentFormat();
        this.languageId = checkOptionsBuilder.getLanguageId();
        this.disableCustomFieldValidation = checkOptionsBuilder.isDisableCustomFieldValidation();
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
