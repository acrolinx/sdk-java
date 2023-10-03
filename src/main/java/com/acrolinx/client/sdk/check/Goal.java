/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class Goal {
  private final String id;
  private final String displayName;
  private final String color;
  private final int issues;

  public Goal(final String id, final String displayName, final String color, int issues) {
    this.id = id;
    this.displayName = displayName;
    this.color = color;
    this.issues = issues;
  }

  public int getIssues() {
    return issues;
  }

  public String getColor() {
    return color;
  }

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return "Goal{color="
        + color
        + ", displayName="
        + displayName
        + ", id="
        + id
        + ", issues="
        + issues
        + "}";
  }
}
