/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class Counts {
  private final int sentences;
  private final int words;
  private final int issues;

  public int getSentences() {
    return sentences;
  }

  public int getWords() {
    return words;
  }

  public int getIssues() {
    return issues;
  }

  public Counts(int sentences, int words, int issues) {
    this.sentences = sentences;
    this.words = words;
    this.issues = issues;
  }

  @Override
  public String toString() {
    return "Counts [issues=" + issues + ", sentences=" + sentences + ", words=" + words + "]";
  }
}
