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
    private String contentFormat = "AUTO";
    private String languageId;
    private boolean disableCustomFieldValidation;

    CheckOptionsBuilder()
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

    /**
     * <p>
     * How to choose a correct Check Type? Following are different use cases of the check with
     * corresponding check type:
     * <table summary="" border="1">
     * <tr>
     * <td>Check Type</td>
     * <td>Ux</td>
     * <td>Use case</td>
     * </tr>
     * <tr>
     * <td>interactive</td>
     * <td>Interactive</td>
     * <td>User checks her own document.</td>
     * </tr>
     * <tr>
     * <td>batch</td>
     * <td>Interactive</td>
     * <td>User checks set of her own documents.</td>
     * </tr>
     * <tr>
     * <td>baseline</td>
     * <td>Noninteractive</td>
     * <td>A repository of documents are checked as part of build job running nightly and the user
     * doesn't own the documents.</td>
     * </tr>
     * <tr>
     * <td>automated</td>
     * <td>Noninteractive</td>
     * <td>Check of a single document for automated scenarios as for example a git hook or save a
     * document.</td>
     * </tr>
     * </table>
     *
     *
     * @param checkType Based on the origin of check
     * @return CheckOptionsBuilder
     */
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
        return new CheckOptions(guidanceProfileId, batchId, reportTypes, checkType, contentFormat,
                disableCustomFieldValidation);
    }
}
