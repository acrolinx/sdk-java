/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.platform;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckType;
import com.acrolinx.client.sdk.check.ReportType;
import java.util.List;

public class CheckingCapabilities {
  private final List<GuidanceProfile> guidanceProfiles;
  private final List<ContentFormat> contentFormats;
  private final List<CheckRequest.ContentEncoding> contentEncodings;
  private final String referencePattern;
  private final List<CheckType> checkTypes;
  private final List<ReportType> reportTypes;

  public CheckingCapabilities(
      List<GuidanceProfile> guidanceProfiles,
      List<ContentFormat> contentFormats,
      List<CheckRequest.ContentEncoding> contentEncodings,
      String referencePattern,
      List<CheckType> checkTypes,
      List<ReportType> reportTypes) {
    this.guidanceProfiles = guidanceProfiles;
    this.contentFormats = contentFormats;
    this.contentEncodings = contentEncodings;
    this.referencePattern = referencePattern;
    this.checkTypes = checkTypes;
    this.reportTypes = reportTypes;
  }

  public List<GuidanceProfile> getGuidanceProfiles() {
    return guidanceProfiles;
  }

  public List<ContentFormat> getContentFormats() {
    return contentFormats;
  }

  public List<CheckRequest.ContentEncoding> getContentEncodings() {
    return contentEncodings;
  }

  public String getReferencePattern() {
    return referencePattern;
  }

  public List<CheckType> getCheckTypes() {
    return checkTypes;
  }

  public List<ReportType> getReportTypes() {
    return reportTypes;
  }
}
