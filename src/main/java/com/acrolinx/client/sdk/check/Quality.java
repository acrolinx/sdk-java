/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

import com.acrolinx.client.sdk.platform.ScoresByGoal;

public class Quality
{
    public enum Status
    {
        red, yellow, green
    }

    private final int score;
    private final Status status;
    private List<ScoresByGoal> scoresByGoal;

    public Quality(int score, Status status, List<ScoresByGoal> scoresByGoal)
    {
        this.score = score;
        this.status = status;
        this.scoresByGoal = scoresByGoal;
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
        return "Quality{" + "score=" + score + ", status=" + status + ", scoresByGoal=" + scoresByGoal + '}';
    }
}
