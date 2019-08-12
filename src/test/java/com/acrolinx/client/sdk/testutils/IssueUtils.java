/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.testutils;

import com.acrolinx.client.sdk.check.Issue;

import java.util.List;

public class IssueUtils {
    public static Issue findIssueWithSurface(List<Issue> issues, String surface) {
        for(Issue issue: issues) {
            if (issue.getDisplaySurface().equals(surface)) {
                return issue;
            }
        }
        return null;
    }
}
