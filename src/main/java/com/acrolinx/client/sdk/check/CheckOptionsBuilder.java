/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.utils.BatchCheckIdGenerator;
import java.util.List;

public class CheckOptionsBuilder {
  private String guidanceProfileId;
  private String batchId;
  private List<ReportType> reportTypes;
  private CheckType checkType;
  private String contentFormat = "AUTO";

  /** A builder to generate CheckOptions. */
  CheckOptionsBuilder() {}

  /**
   * Optional. Available guidance profiles depend on your Acrolinx Platform configuration.
   *
   * @see com.acrolinx.client.sdk.AcrolinxEndpoint#getCapabilities(AccessToken)
   */
  public CheckOptionsBuilder withGuidanceProfileId(String guidanceProfileId) {
    this.guidanceProfileId = guidanceProfileId;
    return this;
  }

  /**
   * Optional. Per default a unique id is generated for each batch check. If you want to generate
   * your own ids you can use this setting.
   *
   * @see BatchCheckIdGenerator
   */
  public CheckOptionsBuilder withBatchId(String batchId) {
    this.batchId = batchId;
    return this;
  }

  /**
   * Optional. Per default a scorecard is generated for each check. Available report types depend on
   * the current users permission and the Acrolinx Platform configuration.
   *
   * @see com.acrolinx.client.sdk.AcrolinxEndpoint#getCapabilities(AccessToken)
   */
  public CheckOptionsBuilder withGenerateReportTypes(List<ReportType> reportTypes) {
    this.reportTypes = reportTypes;
    return this;
  }

  /**
   * Optional. Default setting is 'interactive'.
   *
   * @param checkType Based on the origin of check
   * @return CheckOptionsBuilder
   */
  public CheckOptionsBuilder withCheckType(CheckType checkType) {
    this.checkType = checkType;
    return this;
  }

  /**
   * Optional. Per default this is set to "AUTO" which enables the Acrolinx Platforms default
   * mapping for input types to common file extensions.
   *
   * @see <a href=
   *     "https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/text-extraction.md#input-types-and-integrating-with-multiformat-editors">Input
   *     Types and Integrating with Multiformat Editors</a>
   */
  public CheckOptionsBuilder withContentFormat(String contentFormat) {
    this.contentFormat = contentFormat;
    return this;
  }

  public CheckOptions build() {
    return new CheckOptions(guidanceProfileId, batchId, reportTypes, checkType, contentFormat);
  }
}
