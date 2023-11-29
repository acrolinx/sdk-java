/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import java.util.List;
import java.util.Map;

public class CheckResult {
  private final String id;
  private final Quality quality;
  private final Map<String, Report> reports;
  private final List<Issue> issues;
  private final List<Goal> goals;
  private Goals goalsObj;
  private final Counts counts;

  public CheckResult(
      final String id,
      final Quality quality,
      final Map<String, Report> reports,
      final List<Issue> issues,
      final List<Goal> goals,
      Counts counts) {
    this.id = id;
    this.quality = quality;
    this.reports = reports;
    this.issues = issues;
    this.goals = goals;
    this.counts = counts;
  }

  public Goals getGoals() {
    synchronized (goals) {
      if (goalsObj == null) {
        goalsObj = new Goals(goals);
      }
    }

    return goalsObj;
  }

  public String getId() {
    return id;
  }

  public Quality getQuality() {
    return quality;
  }

  public Map<String, Report> getReports() {
    return reports;
  }

  public Report getReport(final ReportType reportType) {
    return reports.get(reportType.toString());
  }

  public List<Issue> getIssues() {
    return issues;
  }

  public static class Report {
    private final String displayName;
    private final String link;

    public Report(final String displayName, final String link) {
      this.displayName = displayName;
      this.link = link;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getLink() {
      return link;
    }

    @Override
    public String toString() {
      return "Report{" + "displayName='" + displayName + '\'' + ", link='" + link + '\'' + '}';
    }
  }

  @Override
  public String toString() {
    return "CheckResult{"
        + "id='"
        + id
        + '\''
        + ", quality="
        + quality
        + ", reports="
        + reports
        + ", issues="
        + issues
        + '}';
  }

  public Counts getCounts() {
    return counts;
  }
}
