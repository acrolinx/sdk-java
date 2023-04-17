/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Goals
{
    private final List<Goal> goals;
    private final Map<String, Goal> goalsMap;

    public Goals(List<Goal> goals)
    {
        this.goals = goals;
        this.goalsMap = new HashMap<>();
        for (Goal goal : goals) {
            goalsMap.put(goal.getId(), goal);
        }
    }

    public List<Goal> getAll()
    {
        return goals;
    }

    public Goal ofIssue(Issue issue)
    {
        return goalsMap.get(issue.getGoalId());
    }
}
