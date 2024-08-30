/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.IssueUtils.findIssueWithSurface;
import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.check.CheckOptions;
import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckResponse;
import com.acrolinx.client.sdk.check.CheckResult;
import com.acrolinx.client.sdk.check.CheckType;
import com.acrolinx.client.sdk.check.CustomField;
import com.acrolinx.client.sdk.check.ExternalContentBuilder;
import com.acrolinx.client.sdk.check.Goal;
import com.acrolinx.client.sdk.check.Issue;
import com.acrolinx.client.sdk.check.Issue.Match;
import com.acrolinx.client.sdk.check.Metric;
import com.acrolinx.client.sdk.check.ProgressListener;
import com.acrolinx.client.sdk.check.Quality;
import com.acrolinx.client.sdk.check.ReportType;
import com.acrolinx.client.sdk.check.SimpleDocument;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.testutils.TestUtils;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

class CheckTest extends IntegrationTestBase {
  /** This text should need some seconds to check. */
  static final String LONG_TEST_TEXT = "This sentence is nice. \n".repeat(300);

  GuidanceProfile guidanceProfileEn;
  final ProgressListener progressListener = Mockito.mock(ProgressListener.class);

  @BeforeEach
  void beforeEach() throws AcrolinxException {
    assertNotNull(ACROLINX_API_TOKEN);
    Capabilities capabilities = acrolinxEndpoint.getCapabilities(ACROLINX_API_TOKEN);

    for (GuidanceProfile profile : capabilities.getCheckingCapabilities().getGuidanceProfiles()) {
      if (profile.getLanguage().getId().equals("en")) {
        guidanceProfileEn = profile;
        break;
      }
    }

    assertNotNull(guidanceProfileEn);
  }

  @Test
  void startACheck() throws AcrolinxException {
    CheckResponse checkResponse =
        acrolinxEndpoint.submitCheck(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This textt has ann erroor.")
                .withContentReference(("file.txt"))
                .withCheckOptions(
                    CheckOptions.getBuilder()
                        .withGuidanceProfileId(guidanceProfileEn.getId())
                        .build())
                .build());

    assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
    assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
    assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
  }

  @Test
  void checkXmlWithCustomEntities() throws AcrolinxException {
    final String documentName = "xmlWithReferences.xml";
    String xmlContent = "<x>&special;</x>";

    ExternalContentBuilder externalContentBuilder = new ExternalContentBuilder();
    externalContentBuilder.addEntity("special", "&special2;");
    externalContentBuilder.addEntity("special2", "This is an tesst!");

    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocument(new SimpleDocument(xmlContent, externalContentBuilder.build()))
                .withContentReference(documentName)
                .withCheckOptions(
                    CheckOptions.getBuilder()
                        .withGuidanceProfileId(guidanceProfileEn.getId())
                        .build())
                .build());

    for (Issue issue : checkResult.getIssues()) {
      for (Match match : issue.getPositionalInformation().getMatches()) {
        if ("tesst".equals(match.getOriginalPart())) {
          return;
        }
      }
    }

    fail(
        "Issues don't contain an expected referenced issue with surface 'tesst'. Issues: "
            + checkResult.getIssues());
  }

  @Test
  void checkXmlWithPlainTextCustomEntities() throws AcrolinxException {
    final String documentName = "xmlWithReferences.xml";
    String xmlContent = "<x>&special;</x>";

    ExternalContentBuilder externalContentBuilder = new ExternalContentBuilder();

    externalContentBuilder.addTextReplacement("special", "<y>This is &not; an tesst!</y>");
    externalContentBuilder.addTextReplacement("&not;", "tost");

    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocument(new SimpleDocument(xmlContent, externalContentBuilder.build()))
                .withContentReference(documentName)
                .withCheckOptions(
                    CheckOptions.getBuilder()
                        .withGuidanceProfileId(guidanceProfileEn.getId())
                        .build())
                .build());

    for (Issue issue : checkResult.getIssues()) {
      for (Match match : issue.getPositionalInformation().getMatches()) {
        if ("tost".equals(match.getOriginalPart())) {
          fail(
              "Content should not contain the second level entity 'tost' because it should not resolve recursively. Issues: "
                  + checkResult.getIssues());
        }
      }
    }

    for (Issue issue : checkResult.getIssues()) {
      for (Match match : issue.getPositionalInformation().getMatches()) {
        if ("tesst".equals(match.getOriginalPart())) {
          return;
        }
      }
    }

    fail(
        "Issues don't contain an expected referenced issue with surface 'tesst'. Issues: "
            + checkResult.getIssues());
  }

  @Test
  void checkDitaMap() throws AcrolinxException, IOException {
    final String documentName = "test.ditamap";
    String xmlContent = TestUtils.readResource("test.ditamap");

    ExternalContentBuilder externalContentBuilder = new ExternalContentBuilder();
    externalContentBuilder.addDitaReference("some.dita", TestUtils.readResource("test.topic"));

    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocument(new SimpleDocument(xmlContent, externalContentBuilder.build()))
                .withContentReference(documentName)
                .withCheckOptions(
                    CheckOptions.getBuilder()
                        .withGuidanceProfileId(guidanceProfileEn.getId())
                        .build())
                .build());

    for (Issue issue : checkResult.getIssues()) {
      for (Match match : issue.getPositionalInformation().getMatches()) {
        if ("tesst".equals(match.getOriginalPart())) {
          return;
        }
      }
    }

    fail(
        "Issues don't contain an expected referenced issue with surface 'tesst'. Issues: "
            + checkResult.getIssues());
  }

  @Test
  void checkAndGetResult() throws AcrolinxException {
    CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

    final Quality quality = checkResult.getQuality();
    assertThat(quality.getScore(), lessThan(100));
    assertThat(quality.getScore(), greaterThan(0));
    assertNotNull(quality.getStatus());
    assertNotNull(quality.getScoresByGoal());

    assertEquals(1, checkResult.getReports().size());
    CheckResult.Report scorecard = checkResult.getReport(ReportType.scorecard);
    assertEquals("Score Card", scorecard.getDisplayName());
    assertThat(scorecard.getLink(), startsWith(ACROLINX_URL));
  }

  @Test
  void checkAndGetGoals() throws AcrolinxException {
    CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

    assertThat(checkResult.getGoals().getAll(), not(empty()));
    assertThat(checkResult.getIssues(), not(empty()));
    assertSame(checkResult.getGoals(), checkResult.getGoals());
    assertSame(checkResult.getGoals().getAll(), checkResult.getGoals().getAll());

    for (Issue issue : checkResult.getIssues()) {
      Goal goal = checkResult.getGoals().ofIssue(issue);
      assertEquals(issue.getGoalId(), goal.getId());
      assertThat(goal.getIssues(), not(0));
      assertNotNull(goal.getColor());
      assertNotNull(goal.getDisplayName());
      assertThat(goal.getColor(), not(""));
      assertThat(goal.getDisplayName(), not(""));
    }

    for (Goal goal : checkResult.getGoals().getAll()) {
      assertNotNull(goal.getId());
      assertNotNull(goal.getColor());
      assertNotNull(goal.getDisplayName());
      assertThat(goal.getId(), not(""));
      assertThat(goal.getColor(), not(""));
      assertThat(goal.getDisplayName(), not(""));
    }
  }

  @Test
  void checkAndGetMetrics() throws AcrolinxException {
    CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

    assertNotNull(checkResult.getQuality().getMetrics());
    assertThat(checkResult.getQuality().getMetrics(), not(empty()));

    for (Metric metric : checkResult.getQuality().getMetrics()) {
      assertThat(metric.getId(), not(emptyOrNullString()));
      assertThat(metric.getScore(), not(lessThanOrEqualTo(0)));
    }
  }

  @Test
  void checkAndGetCounts() throws AcrolinxException {
    CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

    assertNotNull(checkResult.getCounts());
    assertThat(checkResult.getCounts().getSentences(), not(0));
    assertThat(checkResult.getCounts().getWords(), not(0));
    assertEquals(checkResult.getCounts().getIssues(), checkResult.getIssues().size());
  }

  @Test
  void checkWithUserAndSdkUrl() throws AcrolinxException, URISyntaxException {
    URI realAcrolinxURL = new URI(ACROLINX_URL);
    URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy");

    acrolinxEndpoint =
        new AcrolinxEndpoint(
            realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This textt has ann erroor.")
                .withContentReference(("file.txt"))
                .build());

    assertThat(
        checkResult.getReport(ReportType.scorecard).getLink(),
        startsWith("https://www.acrolinx.com/proxy/"));
    assertThat(
        checkResult.getReport(ReportType.scorecard).getLink(),
        not(startsWith("https://www.acrolinx.com/proxy//")));
  }

  @Test
  void checkWithUserAndSdkUrl2() throws AcrolinxException, URISyntaxException {
    URI realAcrolinxURL = new URI(ACROLINX_URL);
    URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy/");

    acrolinxEndpoint =
        new AcrolinxEndpoint(
            realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This textt has ann erroor.")
                .withContentReference(("file.txt"))
                .build());

    assertThat(
        checkResult.getReport(ReportType.scorecard).getLink(),
        startsWith("https://www.acrolinx.com/proxy/"));
    assertThat(
        checkResult.getReport(ReportType.scorecard).getLink(),
        not(startsWith("https://www.acrolinx.com/proxy//")));
  }

  @Test
  void checkUtf8() throws AcrolinxException {
    String documentContent = "an naïve approach";
    CheckResult checkResult = checkEnglishText(documentContent);

    Issue issue = findIssueWithSurface(checkResult.getIssues(), "an naïve");
    assertNotNull(issue);
  }

  @Test
  void testCheckWithoutProgressListener() throws AcrolinxException {
    CheckResult checkResult =
        acrolinxEndpoint.check(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This text contaanis error")
                .withCheckOptions(
                    CheckOptions.getBuilder()
                        .withContentFormat("TEXT")
                        .withCheckType(CheckType.automated)
                        .build())
                .build());

    assertNotNull(checkResult);
  }

  @Test
  void testSetCheckBaseline() throws AcrolinxException {
    CheckOptions checkOptions =
        CheckOptions.getBuilder()
            .withGuidanceProfileId(guidanceProfileEn.getId())
            .withCheckType(CheckType.baseline)
            .build();

    CheckResponse checkResponse =
        acrolinxEndpoint.submitCheck(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This textt has ann erroor.")
                .withContentReference(("file.txt"))
                .withCheckOptions(checkOptions)
                .build());

    assertNotNull(checkResponse);
  }

  /**
   * This test might become pretty flaky, when the server is faster than expected. When we notice
   * this problem, we should rewrite it using a mocked server.
   */
  @Test
  void checkALargeTextAndGetProgress() throws AcrolinxException {
    CheckResult checkResult = checkEnglishText(LONG_TEST_TEXT);

    verify(progressListener, atLeast(2)).onProgress(argThat(new ProgressMatcher()));

    assertThat(checkResult.getQuality().getScore(), not(lessThan(40)));
  }

  /**
   * This test might become pretty flaky, when the server is faster than expected. When we notice
   * this problem, we should rewrite it using a mocked server.
   */
  @Test
  void cancelCheck() throws InterruptedException {
    CheckRequest.ofDocumentContent(LONG_TEST_TEXT)
        .withContentReference(("file.txt"))
        .withCheckOptions(
            CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build())
        .build();

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Future<CheckResult> future =
        executorService.submit(
            new Callable<CheckResult>() {
              @Override
              public CheckResult call() throws Exception {
                return checkEnglishText(LONG_TEST_TEXT);
              }
            });

    Thread.sleep(100); // Give some time to start the check

    future.cancel(true);

    Thread.sleep(100); // Without waiting the Connection pool will shut down before we can
    // send cancel.

    Assertions.assertThrows(CancellationException.class, () -> future.get());
  }

  @Test
  void testFireMultipleChecksWithoutWaitingForResult() throws AcrolinxException {
    int numberOfChecks = 5;

    for (int i = 0; i < numberOfChecks; i++) {
      String uuid = UUID.randomUUID().toString();
      CheckOptions checkOptions =
          CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build();

      CheckResponse checkResponse =
          acrolinxEndpoint.submitCheck(
              ACROLINX_API_TOKEN,
              CheckRequest.ofDocumentContent(uuid)
                  .withContentReference((uuid + ".txt"))
                  .withCheckOptions(checkOptions)
                  .build());

      assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
      assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
      assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }
  }

  @Test
  void testFireMultipleChecksWaitingForResult() throws AcrolinxException {
    int numberOfChecks = 5;

    for (int i = 0; i < numberOfChecks; i++) {
      String uuid = UUID.randomUUID().toString();

      CheckResult checkResult =
          acrolinxEndpoint.check(
              ACROLINX_API_TOKEN,
              CheckRequest.ofDocumentContent(uuid)
                  .withContentReference((uuid + ".txt"))
                  .withCheckOptions(
                      CheckOptions.getBuilder()
                          .withGuidanceProfileId(guidanceProfileEn.getId())
                          .build())
                  .build(),
              progressListener);

      final Quality quality = checkResult.getQuality();
      assertThat(quality.getScore(), lessThanOrEqualTo(100));
      assertThat(quality.getScore(), not(lessThan(40)));
      assertNotNull(quality.getStatus());
    }
  }

  @Test
  void testMultipleChecksParallelWaitingForResult()
      throws InterruptedException, ExecutionException {
    int numberOfChecks = 5;

    ExecutorService executorService = Executors.newFixedThreadPool(numberOfChecks);

    List<Future<CheckResult>> checks = Lists.newArrayList();

    for (int i = 0; i < numberOfChecks; i++) {
      final String uuid = UUID.randomUUID().toString();
      Future<CheckResult> futureResult =
          executorService.submit(
              () -> {
                return acrolinxEndpoint.check(
                    ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent(uuid)
                        .withContentReference((uuid + ".txt"))
                        .withCheckOptions(
                            CheckOptions.getBuilder()
                                .withGuidanceProfileId(guidanceProfileEn.getId())
                                .build())
                        .build(),
                    progressListener);
              });

      checks.add(futureResult);
    }

    for (Future<CheckResult> futureCheckResult : checks) {
      final Quality quality = futureCheckResult.get().getQuality();
      assertThat(quality.getScore(), lessThanOrEqualTo(100));
      assertThat(quality.getScore(), not(lessThan(40)));
      assertNotNull(quality.getStatus());
    }
  }

  @Test
  void checkWithOptions() throws AcrolinxException {
    CheckOptions checkOptions =
        CheckOptions.getBuilder()
            .withGuidanceProfileId(guidanceProfileEn.getId())
            .withBatchId(UUID.randomUUID().toString())
            .withCheckType(CheckType.baseline)
            .withContentFormat("txt")
            .build();

    CheckResponse checkResponse =
        acrolinxEndpoint.submitCheck(
            ACROLINX_API_TOKEN,
            CheckRequest.ofDocumentContent("This textt has ann erroor.")
                .withContentReference("file.txt")
                .withCheckOptions(checkOptions)
                .build());

    assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
    assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
    assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
  }

  CheckResult checkEnglishText(String documentContent) throws AcrolinxException {
    return acrolinxEndpoint.check(
        ACROLINX_API_TOKEN,
        CheckRequest.ofDocumentContent(documentContent)
            .withContentReference("file.txt")
            .withCheckOptions(
                CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build())
            .build(),
        progressListener);
  }

  @Test
  void testCheckWithDocumentMetaData() {
    AcrolinxException acrolinxException =
        Assertions.assertThrows(
            AcrolinxException.class,
            () ->
                acrolinxEndpoint.check(
                    ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent("Thee sentencee contains errors")
                        .withContentReference("file.txt")
                        .withCustomField(new CustomField("Text Field", "Item"))
                        .withCustomField(new CustomField("List Field", "List Item 1"))
                        .withCheckOptions(
                            CheckOptions.getBuilder()
                                .withGuidanceProfileId(guidanceProfileEn.getId())
                                .build())
                        .build(),
                    progressListener));
    assertEquals("Incorrect user information", acrolinxException.getMessage());
  }

  @Test
  void testCheckWithDocumentMetaDataAsList() {
    List<CustomField> customFieldList =
        List.of(
            new CustomField(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
            new CustomField(UUID.randomUUID().toString(), UUID.randomUUID().toString()));

    Assertions.assertThrows(
        AcrolinxException.class,
        () ->
            acrolinxEndpoint.check(
                ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("Thee sentencee contains errors")
                    .withContentReference("file.txt")
                    .withCustomFields(customFieldList)
                    .withCheckOptions(
                        CheckOptions.getBuilder()
                            .withGuidanceProfileId(guidanceProfileEn.getId())
                            .build())
                    .build(),
                progressListener));
  }

  static class ProgressMatcher implements ArgumentMatcher<Progress> {
    private double prevPercent;

    @Override
    public boolean matches(Progress progress) {
      boolean valid = progress.getPercent() >= this.prevPercent && progress.getMessage() != null;
      this.prevPercent = progress.getPercent();
      return valid;
    }
  }
}
