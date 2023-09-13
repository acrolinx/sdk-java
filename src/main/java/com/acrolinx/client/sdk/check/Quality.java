/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.platform.ScoresByGoal;
import java.util.List;

public class Quality
{
    public enum Status
    {
        red, yellow, green
    }

    private final int score;
    private final Status status;
    private final List<ScoresByGoal> scoresByGoal;
    private final List<Goal> goals;
    private final List<Metric> metrics;

    public Quality(int score, Status status, List<ScoresByGoal> scoresByGoal, List<Goal> goals, List<Metric> metrics)
    {
        this.score = score;
        this.status = status;
        this.scoresByGoal = scoresByGoal;
        this.goals = goals;
        this.metrics = metrics;
    }

    public List<Metric> getMetrics()
    {
        return metrics;
    }

    public List<Goal> getGoals()
    {
        return goals;
    }

    public int getScore()
    {
        return score;
    }

    public Status getStatus()
    {
        return status;
    }

    public List<ScoresByGoal> getScoresByGoal()
    {
        return scoresByGoal;
    }

    @Override
    public String toString()
    {
        return "Quality{goals=" + goals + ", score=" + score + ", scoresByGoal=" + scoresByGoal + ", metrics=" + metrics
                + ", status=" + status + "}";
    }
}
