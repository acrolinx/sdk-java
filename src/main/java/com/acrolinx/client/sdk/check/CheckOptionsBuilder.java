/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

public class CheckOptionsBuilder
{
    private String guidanceProfileId;
    private String batchId;
    private List<ReportType> reportTypes;
    private CheckType checkType;
    private String contentFormat;
    private String languageId;
    private boolean disableCustomFieldValidation;

    public CheckOptionsBuilder()
    {
    }

    public CheckOptionsBuilder withGuidanceProfileId(String guidanceProfileId)
    {
        this.guidanceProfileId = guidanceProfileId;
        return this;
    }

    public CheckOptionsBuilder withBatchId(String batchId)
    {
        this.batchId = batchId;
        return this;
    }

    public CheckOptionsBuilder withGenerateReportTypes(List<ReportType> reportTypes)
    {

        this.reportTypes = reportTypes;
        return this;
    }

    public CheckOptionsBuilder withCheckType(CheckType checkType)
    {
        this.checkType = checkType;
        return this;
    }

    public CheckOptionsBuilder withContentFormat(String contentFormat)
    {
        this.contentFormat = contentFormat;
        return this;
    }

    public CheckOptionsBuilder withLanguageId(String languageId)
    {
        this.languageId = languageId;
        return this;
    }

    public CheckOptionsBuilder withCustomFieldValidationDisabled(boolean disableCustomFieldValidation)
    {
        this.disableCustomFieldValidation = disableCustomFieldValidation;
        return this;
    }

    public CheckOptions build()
    {
        return new CheckOptions(this);
    }

    String getGuidanceProfileId()
    {
        return guidanceProfileId;
    }

    protected String getBatchId()
    {
        return batchId;
    }

    protected List<ReportType> getReportTypes()
    {
        return reportTypes;
    }

    protected CheckType getCheckType()
    {
        return checkType;
    }

    protected String getContentFormat()
    {
        return contentFormat;
    }

    protected String getLanguageId()
    {
        return languageId;
    }

    protected boolean isDisableCustomFieldValidation()
    {
        return disableCustomFieldValidation;
    }
}
