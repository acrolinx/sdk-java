/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

import java.util.Map;

public class CheckResult {
    private final String id;
    private final Quality quality;
    private final Map<String, Report> reports;

    public CheckResult(String id, Quality quality, Map<String, Report> reports) {
        this.id = id;
        this.quality = quality;
        this.reports = reports;
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

    public Report getReport(ReportType reportType) {
        return reports.get(reportType.toString());
    }

    public class Report {
        private final String displayName;
        private final String link;

        public Report(String displayName, String link) {
            this.displayName = displayName;
            this.link = link;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getLink() {
            return link;
        }
    }
}
