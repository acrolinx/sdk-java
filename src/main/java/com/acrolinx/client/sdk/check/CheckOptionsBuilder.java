/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.utils.BatchCheckIdGenerator;

public class CheckOptionsBuilder
{
    private String guidanceProfileId;
    private String batchId;
    private List<ReportType> reportTypes;
    private CheckType checkType;
    private String contentFormat = "AUTO";

    /**
     * A builder to generate CheckOptions.
     */
    CheckOptionsBuilder()
    {
    }

    /**
     * Optional. Available guidance profiles depend on your Acrolinx Platform configuration.
     *
     * @see com.acrolinx.client.sdk.AcrolinxEndpoint#getCapabilities(AccessToken)
     */
    public CheckOptionsBuilder withGuidanceProfileId(String guidanceProfileId)
    {
        this.guidanceProfileId = guidanceProfileId;
        return this;
    }

    /**
     * Optional. Per default a unique id is generated for each batch check. If you want to generate your
     * own ids you can use this setting.
     * 
     * @see BatchCheckIdGenerator
     */
    public CheckOptionsBuilder withBatchId(String batchId)
    {
        this.batchId = batchId;
        return this;
    }

    /**
     * Optional. Per default a scorecard is generated for each check. Available report types depend on
     * the current users permission and the Acrolinx Platform configuration.
     *
     * @see com.acrolinx.client.sdk.AcrolinxEndpoint#getCapabilities(AccessToken)
     */
    public CheckOptionsBuilder withGenerateReportTypes(List<ReportType> reportTypes)
    {
        this.reportTypes = reportTypes;
        return this;
    }

    /**
     * Optional. Default setting is 'interactive'.
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
     * @param checkType Based on the origin of check
     * @return CheckOptionsBuilder
     */
    public CheckOptionsBuilder withCheckType(CheckType checkType)
    {
        this.checkType = checkType;
        return this;
    }

    /**
     * Optional. Per default this is set to "AUTO" which enables the Acrolinx Platforms default mapping
     * for input types to common file extensions.
     * 
     * @see <a href=
     *      "https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/text-extraction.md#input-types-and-integrating-with-multiformat-editors">Input
     *      Types and Integrating with Multiformat Editors</a>
     */
    public CheckOptionsBuilder withContentFormat(String contentFormat)
    {
        this.contentFormat = contentFormat;
        return this;
    }

    public CheckOptions build()
    {
        return new CheckOptions(guidanceProfileId, batchId, reportTypes, checkType, contentFormat);
    }
}
