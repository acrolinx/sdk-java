/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.platform.ScoresByGoal;

import java.util.List;

public class Quality {
    public enum Status {
        red,
        yellow,
        green
    }

    private final int score;
    private final Status status;
    private List<ScoresByGoal> scoresByGoal;

    public Quality(int score, Status status, List<ScoresByGoal> scoresByGoal) {
        this.score = score;
        this.status = status;
        this.scoresByGoal = scoresByGoal;
    }

    public int getScore() {
        return score;
    }

    public Status getStatus() {
        return status;
    }

    public List<ScoresByGoal> getScoresByGoal() {
        return scoresByGoal;
    }
}
