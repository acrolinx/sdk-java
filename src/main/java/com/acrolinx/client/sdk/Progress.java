/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk;

public class Progress {
  private final Double retryAfter;
  private final Double percent;
  private final String message;

  public Progress(Double retryAfter, Double percent, String message) {
    this.retryAfter = retryAfter;
    this.percent = percent;
    this.message = message;
  }

  public Double getPercent() {
    return percent;
  }

  public String getMessage() {
    return message;
  }

  /** In seconds */
  public Double getRetryAfter() {
    return retryAfter;
  }

  /** In milliseconds */
  public long getRetryAfterMs() {
    return Math.round(this.getRetryAfter() * 1000.0);
  }

  @Override
  public String toString() {
    return "Progress{"
        + "retryAfter="
        + retryAfter
        + ", percent="
        + percent
        + ", message='"
        + message
        + '\''
        + '}';
  }
}
