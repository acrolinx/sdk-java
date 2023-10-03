/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import java.util.List;

public class CheckOptions {
  private final String guidanceProfileId;
  private final String batchId;
  private final List<ReportType> reportTypes;
  private final CheckType checkType;
  private final String contentFormat;

  CheckOptions(
      String guidanceProfileId,
      String batchId,
      List<ReportType> reportTypes,
      CheckType checkType,
      String contentFormat) {
    this.guidanceProfileId = guidanceProfileId;
    this.batchId = batchId;
    this.reportTypes = reportTypes;
    this.checkType = checkType;
    this.contentFormat = contentFormat;
  }

  public static CheckOptionsBuilder getBuilder() {
    return new CheckOptionsBuilder();
  }

  public String getGuidanceProfileId() {
    return guidanceProfileId;
  }

  public String getBatchId() {
    return batchId;
  }

  public List<ReportType> getReportTypes() {
    return reportTypes;
  }

  public CheckType getCheckType() {
    return checkType;
  }

  public String getContentFormat() {
    return contentFormat;
  }
}
