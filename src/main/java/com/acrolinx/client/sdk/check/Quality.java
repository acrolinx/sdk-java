package com.acrolinx.client.sdk.check;

public class Quality {
    public enum Status {
        red,
        yellow,
        green
    }

    private final int score;
    private final Status status;

    public Quality(int score, Status status) {
        this.score = score;
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public Status getStatus() {
        return status;
    }
}
