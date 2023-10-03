/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import java.util.List;

public class Issue {
  private final String displayNameHtml;
  private final String guidanceHtml;
  private final String displaySurface;
  private final List<Suggestion> suggestions;
  private final PositionalInformation positionalInformation;
  private final String goalId;

  public Issue(
      String displayNameHtml,
      String guidanceHtml,
      String displaySurface,
      List<Suggestion> suggestions,
      PositionalInformation positionalInformation,
      String goalId) {
    this.displayNameHtml = displayNameHtml;
    this.guidanceHtml = guidanceHtml;
    this.displaySurface = displaySurface;
    this.suggestions = suggestions;
    this.positionalInformation = positionalInformation;
    this.goalId = goalId;
  }

  public String getDisplayNameHtml() {
    return displayNameHtml;
  }

  public String getGuidanceHtml() {
    return guidanceHtml;
  }

  public String getDisplaySurface() {
    return displaySurface;
  }

  public List<Suggestion> getSuggestions() {
    return suggestions;
  }

  public PositionalInformation getPositionalInformation() {
    return positionalInformation;
  }

  @Override
  public String toString() {
    return "Issue{"
        + "displayNameHtml='"
        + displayNameHtml
        + '\''
        + ", guidanceHtml='"
        + guidanceHtml
        + '\''
        + ", displaySurface='"
        + displaySurface
        + '\''
        + ", suggestions="
        + suggestions
        + ", positionalInformation="
        + positionalInformation
        + '}';
  }

  public static class Suggestion {
    private final String surface;

    public Suggestion(String surface) {
      this.surface = surface;
    }

    public String getSurface() {
      return surface;
    }

    @Override
    public String toString() {
      return "Suggestion{" + "surface='" + surface + '\'' + '}';
    }
  }

  public static class PositionalInformation {
    private final List<Match> matches;

    public PositionalInformation(List<Match> matches) {
      this.matches = matches;
    }

    public List<Match> getMatches() {
      return matches;
    }

    @Override
    public String toString() {
      return "PositionalInformation{" + "matches=" + matches + '}';
    }
  }

  public static class Match {
    private final long originalBegin;
    private final long originalEnd;
    private final String originalPart;

    public Match(long originalBegin, long originalEnd, String originalPart) {
      this.originalBegin = originalBegin;
      this.originalEnd = originalEnd;
      this.originalPart = originalPart;
    }

    public long getOriginalBegin() {
      return originalBegin;
    }

    public long getOriginalEnd() {
      return originalEnd;
    }

    public String getOriginalPart() {
      return originalPart;
    }

    @Override
    public String toString() {
      return "Match{"
          + "originalBegin="
          + originalBegin
          + ", originalEnd="
          + originalEnd
          + ", originalPart='"
          + originalPart
          + '\''
          + '}';
    }
  }

  public String getGoalId() {
    return this.goalId;
  }
}
