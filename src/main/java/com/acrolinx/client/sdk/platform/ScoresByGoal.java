/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.platform;

public class ScoresByGoal {

    private String id;
    private Integer score;

    public ScoresByGoal(String id, Integer score) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

}
