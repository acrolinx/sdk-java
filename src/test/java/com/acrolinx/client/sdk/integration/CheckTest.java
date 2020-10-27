/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.IssueUtils.findIssueWithFirstSuggestion;
import static com.acrolinx.client.sdk.testutils.IssueUtils.findIssueWithSurface;
import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.check.CheckOptions;
import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckRequest.ContentEncoding;
import com.acrolinx.client.sdk.check.CheckResponse;
import com.acrolinx.client.sdk.check.CheckResult;
import com.acrolinx.client.sdk.check.CheckType;
import com.acrolinx.client.sdk.check.CustomField;
import com.acrolinx.client.sdk.check.Goal;
import com.acrolinx.client.sdk.check.Issue;
import com.acrolinx.client.sdk.check.ProgressListener;
import com.acrolinx.client.sdk.check.Quality;
import com.acrolinx.client.sdk.check.Quality.Status;
import com.acrolinx.client.sdk.check.ReportType;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.acrolinx.client.sdk.testutils.TestUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CheckTest extends IntegrationTestBase
{
    /**
     * This text should should need some seconds to check.
     */
    private static final String longTestText = Strings.repeat("This sentence is nice. \n", 300);
    private GuidanceProfile guidanceProfileEn;

    @Mock
    private ProgressListener progressListener;

    @Before
    public void beforeTest() throws AcrolinxException
    {
        assumeTrue(ACROLINX_API_TOKEN != null);
        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN);

        for (GuidanceProfile profile : capabilities.getCheckingCapabilities().getGuidanceProfiles()) {
            if (profile.getLanguage().getId().equals("en")) {
                guidanceProfileEn = profile;
                break;
            }
        }
        assumeTrue(guidanceProfileEn != null);
    }

    @Test
    public void startACheck() throws AcrolinxException
    {
        CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withContentReference(
                        ("file.txt")).withCheckOptions(
                                CheckOptions.getBuilder().withGuidanceProfileId(
                                        guidanceProfileEn.getId()).build()).build());

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }

    @Test
    public void checkAWordDocument() throws AcrolinxException
    {
        final String base64FileContent = TestUtils.readResourceAsBase64("document.docx");
        final String wordDocumentName = "document.docx";
        CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent(base64FileContent).withContentEncoding(
                        ContentEncoding.base64).withContentReference(wordDocumentName).withCheckOptions(
                                CheckOptions.getBuilder().withGuidanceProfileId(
                                        guidanceProfileEn.getId()).build()).build());
        assertEquals(Status.red, checkResult.getQuality().getStatus());
        assertFalse(checkResult.getReports().get("scorecard").getLink().length() == 0);
    }

    @Test
    public void checkAndGetResult() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

        final Quality quality = checkResult.getQuality();
        assertThat(quality.getScore(), lessThan(100));
        assertThat(quality.getScore(), not(lessThan(40)));
        assertNotNull(quality.getStatus());
        assertNotNull(quality.getScoresByGoal());

        assertEquals(1, checkResult.getReports().size());
        CheckResult.Report scorecard = checkResult.getReport(ReportType.scorecard);
        assertEquals("Score Card", scorecard.getDisplayName());
        assertThat(scorecard.getLink(), startsWith(ACROLINX_URL));
    }

    @Test
    public void checkAndGetGoals() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

        assertNotNull(checkResult.getGoals());
        assertThat(checkResult.getGoals().getAll(), not(empty()));
        assertThat(checkResult.getIssues(), not(empty()));
        assertSame(checkResult.getGoals(), checkResult.getGoals());
        assertSame(checkResult.getGoals().getAll(), checkResult.getGoals().getAll());

        for (Issue issue : checkResult.getIssues()) {
            assertEquals(issue.getGoalId(), checkResult.getGoals().ofIssue(issue).getId());
            assertThat(checkResult.getGoals().ofIssue(issue).getIssues(), not(0));
            assertNotNull(checkResult.getGoals().ofIssue(issue).getColor());
            assertNotNull(checkResult.getGoals().ofIssue(issue).getDisplayName());
            assertThat(checkResult.getGoals().ofIssue(issue).getColor(), not(""));
            assertThat(checkResult.getGoals().ofIssue(issue).getDisplayName(), not(""));
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
    public void checkAndGetCounts() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText("This textt has ann erroor.");

        assertNotNull(checkResult.getCounts());
        assertThat(checkResult.getCounts().getSentences(), not(0));
        assertThat(checkResult.getCounts().getWords(), not(0));
        assertEquals(checkResult.getCounts().getIssues(), checkResult.getIssues().size());
    }

    @Test
    public void checkWithUserAndSdkUrl() throws AcrolinxException, URISyntaxException
    {
        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy");

        endpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

        CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withContentReference(
                        ("file.txt")).build());

        assertThat(checkResult.getReport(ReportType.scorecard).getLink(),
                startsWith("https://www.acrolinx.com/proxy/"));
        assertThat(checkResult.getReport(ReportType.scorecard).getLink(),
                not(startsWith("https://www.acrolinx.com/proxy//")));
    }

    @Test
    public void checkWithUserAndSdkUrl2() throws AcrolinxException, URISyntaxException
    {
        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy/");

        endpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

        CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withContentReference(
                        ("file.txt")).build());

        assertThat(checkResult.getReport(ReportType.scorecard).getLink(),
                startsWith("https://www.acrolinx.com/proxy/"));
        assertThat(checkResult.getReport(ReportType.scorecard).getLink(),
                not(startsWith("https://www.acrolinx.com/proxy//")));
    }

    @Test
    public void checkUtf8() throws AcrolinxException
    {
        String documentContent = "an naïve approach";
        CheckResult checkResult = checkEnglishText(documentContent);

        Issue issue = findIssueWithSurface(checkResult.getIssues(), "an naïve");
        assertNotNull(issue);
    }

    @Test
    public void testCheckWithoutProgressListener() throws AcrolinxException
    {
        CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This text contaanis error").withCheckOptions(
                        CheckOptions.getBuilder().withContentFormat("TEXT").withCheckType(
                                CheckType.automated).build()).build());

        assertNotNull(checkResult);
    }

    @Test
    public void testSetCheckBaseline() throws AcrolinxException
    {
        CheckOptions checkOptions = CheckOptions.getBuilder().withGuidanceProfileId(
                guidanceProfileEn.getId()).withCheckType(CheckType.baseline).build();

        CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withContentReference(
                        ("file.txt")).withCheckOptions(checkOptions).build());

        assertNotNull(checkResponse);
    }

    @Test
    public void checkResultContainsIssues() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText("A textt");

        Issue issue = findIssueWithFirstSuggestion(checkResult.getIssues(), "text");

        assertNotNull("Expected issue not found", issue);

        assertThat(issue.getDisplayNameHtml(), not(emptyOrNullString()));
        assertThat(issue.getGuidanceHtml(), not(emptyOrNullString()));
        assertEquals("textt", issue.getDisplaySurface());
        assertEquals("textt", issue.getDisplaySurface());

        Issue.Suggestion suggestion = issue.getSuggestions().get(0);
        assertEquals("text", suggestion.getSurface());

        List<Issue.Match> matches = issue.getPositionalInformation().getMatches();
        assertThat(matches, hasSize(1));
        Issue.Match match = matches.get(0);

        assertEquals(2, match.getOriginalBegin());
        assertEquals(7, match.getOriginalEnd());
        assertEquals("textt", match.getOriginalPart());

        assertNotNull(issue.getGoalId());
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice this
     * problem, we should rewrite it using a mocked server.
     */
    @Test
    public void checkALargeTextAndGetProgress() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText(longTestText);

        verify(progressListener, atLeast(2)).onProgress(argThat(new ProgressMatcher()));

        assertThat(checkResult.getQuality().getScore(), not(lessThan(40)));
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice this
     * problem, we should rewrite it using a mocked server.
     */
    @Test(expected = CancellationException.class)
    public void cancelCheck() throws InterruptedException, ExecutionException
    {
        CheckRequest.ofDocumentContent(longTestText).withContentReference(("file.txt")).withCheckOptions(
                CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build()).build();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<CheckResult> future = executorService.submit(new Callable<CheckResult>() {
            @Override
            public CheckResult call() throws Exception
            {
                return checkEnglishText(longTestText);
            }
        });

        Thread.sleep(100); // Give some time to start the check

        future.cancel(true);

        Thread.sleep(100); // Without waiting the Connection pool will shut down before we can
        // send cancel.

        future.get();
    }

    @Test
    public void testFireMultipleChecksWithoutWaitingForResult() throws AcrolinxException
    {
        int numberOfChecks = 5;

        for (int i = 0; i < numberOfChecks; i++) {
            String uuid = UUID.randomUUID().toString();
            CheckOptions checkOptions = CheckOptions.getBuilder().withGuidanceProfileId(
                    guidanceProfileEn.getId()).build();

            CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent(uuid).withContentReference((uuid + ".txt")).withCheckOptions(
                            checkOptions).build());

            assertNotNull(checkResponse);
            assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
            assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
            assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
        }
    }

    @Test
    public void testFireMultipleChecksWaitingForResult() throws AcrolinxException
    {
        int numberOfChecks = 5;

        for (int i = 0; i < numberOfChecks; i++) {
            String uuid = UUID.randomUUID().toString();

            CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent(uuid).withContentReference((uuid + ".txt")).withCheckOptions(
                            CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build()).build(),
                    progressListener);

            final Quality quality = checkResult.getQuality();
            assertThat(quality.getScore(), lessThanOrEqualTo(100));
            assertThat(quality.getScore(), not(lessThan(40)));
            assertNotNull(quality.getStatus());
        }
    }

    @Test
    public void testMultipleChecksParallelWaitingForResult() throws InterruptedException, ExecutionException
    {
        int numberOfChecks = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfChecks);

        List<Future<CheckResult>> checks = Lists.newArrayList();

        for (int i = 0; i < numberOfChecks; i++) {
            final String uuid = UUID.randomUUID().toString();
            Future<CheckResult> futureResult = executorService.submit(new Callable<CheckResult>() {
                @Override
                public CheckResult call() throws Exception
                {
                    return endpoint.check(ACROLINX_API_TOKEN,
                            CheckRequest.ofDocumentContent(uuid).withContentReference((uuid + ".txt")).withCheckOptions(
                                    CheckOptions.getBuilder().withGuidanceProfileId(
                                            guidanceProfileEn.getId()).build()).build(),
                            progressListener);
                }
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
    public void checkWithOptions() throws AcrolinxException
    {
        CheckOptions checkOptions = CheckOptions.getBuilder().withGuidanceProfileId(
                guidanceProfileEn.getId()).withBatchId(UUID.randomUUID().toString()).withCheckType(
                        CheckType.baseline).withContentFormat("txt").build();

        List<ReportType> rtl = new ArrayList<>();
        rtl.add(ReportType.scorecard);
        rtl.add((ReportType.termHarvesting));
        CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withContentReference(
                        "file.txt").withCheckOptions(checkOptions).build());

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(emptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }

    private CheckResult checkEnglishText(String documentContent) throws AcrolinxException
    {
        return endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent(documentContent).withContentReference("file.txt").withCheckOptions(
                        CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build()).build(),
                progressListener);
    }

    @Test
    public void testCheckWithDocumentMetaData()
    {
        try {
            CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent("Thee sentencee contains errors").withContentReference(
                            "file.txt").withCustomField(new CustomField("Text Field", "Item")).withCustomField(
                                    new CustomField("List Field", "List Item 1")).withCheckOptions(
                                            CheckOptions.getBuilder().withGuidanceProfileId(
                                                    guidanceProfileEn.getId()).build()).build(),
                    progressListener);

            assertNotNull(checkResult);
        } catch (AcrolinxException e) {
            assertEquals(e.getMessage(), "Custom field values are incorrect");
        }
    }

    @Test(expected = AcrolinxException.class)
    public void testCheckWithDocumentMetaDataAsList() throws AcrolinxException
    {
        List<CustomField> customFieldList = new ArrayList<>();
        customFieldList.add(new CustomField(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        customFieldList.add(new CustomField(UUID.randomUUID().toString(), UUID.randomUUID().toString()));

        endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("Thee sentencee contains errors").withContentReference(
                        "file.txt").withCustomFields(customFieldList).withCheckOptions(
                                CheckOptions.getBuilder().withGuidanceProfileId(
                                        guidanceProfileEn.getId()).build()).build(),
                progressListener);
    }

    public static class ProgressMatcher implements ArgumentMatcher<Progress>
    {
        private double prevPercent = 0;

        @Override
        public boolean matches(Progress value)
        {
            boolean valid = value.getPercent() >= this.prevPercent && value.getMessage() != null;
            this.prevPercent = value.getPercent();
            return valid;
        }
    }
}
