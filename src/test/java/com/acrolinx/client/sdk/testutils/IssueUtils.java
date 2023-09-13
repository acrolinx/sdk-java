/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.testutils;

import com.acrolinx.client.sdk.check.Issue;
import java.util.List;

public final class IssueUtils
{
    public static Issue findIssueWithSurface(List<Issue> issues, String surface)
    {
        for (Issue issue : issues) {
            if (issue.getDisplaySurface().equals(surface)) {
                return issue;
            }
        }

        return null;
    }

    public static Issue findIssueWithFirstSuggestion(List<Issue> issues, String suggestionSurface)
    {
        for (Issue issue : issues) {
            Issue.Suggestion suggestion = issue.getSuggestions().get(0);

            if (suggestion != null && suggestion.getSurface().equals(suggestionSurface)) {
                return issue;
            }
        }

        return null;
    }

    private IssueUtils()
    {
        throw new IllegalStateException();
    }
}
