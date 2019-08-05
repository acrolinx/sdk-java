/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

import java.util.List;

public class CheckOptions {
    private String guidanceProfileId;
    private String batchId;
    private List<ReportType> reportTypes;
    private CheckType checkType;
    private String contentFormat;
    private String languageId;
    private boolean disableCustomFieldValidation;

    public CheckOptions() {
    }

    public CheckOptions(String guidanceProfileId) {
        this.guidanceProfileId = guidanceProfileId;
    }

    public String getGuidanceProfileId() {
        return guidanceProfileId;
    }


    public void setGuidanceProfileId(String guidanceProfileId) {
        this.guidanceProfileId = guidanceProfileId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public List<ReportType> getReportTypes() {
        return reportTypes;
    }

    public CheckOptions setReportTypes(List<ReportType> reportTypes) {
        this.reportTypes = reportTypes;
        return this;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public boolean isDisableCustomFieldValidation() {
        return disableCustomFieldValidation;
    }

    public void setDisableCustomFieldValidation(boolean disableCustomFieldValidation) {
        this.disableCustomFieldValidation = disableCustomFieldValidation;
    }
}
