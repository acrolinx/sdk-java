/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class Metric {
  private final String id;
  private final int score;

  public int getScore() {
    return score;
  }

  public String getId() {
    return id;
  }

  public Metric(String id, int score) {
    this.id = id;
    this.score = score;
  }

  @Override
  public String toString() {
    return "Metric{id=" + id + ", score=" + score + "}";
  }
}
