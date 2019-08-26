/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.IssueUtils.findIssueWithFirstSuggestion;
import static com.acrolinx.client.sdk.testutils.IssueUtils.findIssueWithSurface;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.check.*;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
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
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withDocumentReference(
                        ("file.txt")).withCheckOptions(
                                CheckOptions.getBuilder().withGuidanceProfileId(
                                        guidanceProfileEn.getId()).build()).build());

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
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
    public void checkUtf8() throws AcrolinxException
    {
        String documentContent = "an naïve approach";
        CheckResult checkResult = checkEnglishText(documentContent);

        Issue issue = findIssueWithSurface(checkResult.getIssues(), "an naïve");
        assertNotNull(issue);
    }

    @Test
    public void testSetCheckBaseline() throws AcrolinxException
    {
        CheckOptions checkOptions = CheckOptions.getBuilder().withGuidanceProfileId(
                guidanceProfileEn.getId()).withCheckType(CheckType.baseline).build();

        CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withDocumentReference(
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
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice
     * this problem, we should rewrite it using a mocked server.
     */
    @Test
    public void checkALargeTextAndGetProgress() throws AcrolinxException
    {
        CheckResult checkResult = checkEnglishText(longTestText);

        verify(progressListener, atLeast(2)).onProgress(argThat(new ProgressMatcher()));

        assertThat(checkResult.getQuality().getScore(), not(lessThan(40)));
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice
     * this problem, we should rewrite it using a mocked server.
     */
    @Test(expected = CancellationException.class)
    public void cancelCheck() throws InterruptedException, ExecutionException
    {
        final CheckRequest checkRequest = CheckRequest.ofDocumentContent(longTestText).withDocumentReference(
                ("file.txt")).withCheckOptions(
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

        Thread.sleep(100); // TODO: Without waiting the Connection pool will shut down before we can
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
                    CheckRequest.ofDocumentContent(uuid).withDocumentReference((uuid + ".txt")).withCheckOptions(
                            checkOptions).build());

            assertNotNull(checkResponse);
            assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
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
                    CheckRequest.ofDocumentContent(uuid).withDocumentReference((uuid + ".txt")).withCheckOptions(
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
        int numberOfChecks = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfChecks);

        List<Future<CheckResult>> checks = Lists.newArrayList();

        for (int i = 0; i < numberOfChecks; i++) {
            final String uuid = UUID.randomUUID().toString();
            Future<CheckResult> futureResult = executorService.submit(new Callable<CheckResult>() {
                @Override
                public CheckResult call() throws Exception
                {
                    return endpoint.check(ACROLINX_API_TOKEN,
                            CheckRequest.ofDocumentContent(uuid).withDocumentReference(
                                    (uuid + ".txt")).withCheckOptions(
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
                        CheckType.baseline).withContentFormat("txt").withCustomFieldValidationDisabled(
                                true).withLanguageId("en").build();

        List<ReportType> rtl = new ArrayList<>();
        rtl.add(ReportType.scorecard);
        rtl.add((ReportType.termHarvesting));
        CheckResponse checkResponse = endpoint.submitCheck(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.").withDocumentReference(
                        "file.txt").withCheckOptions(checkOptions).build());

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }

    private CheckResult checkEnglishText(String documentContent) throws AcrolinxException
    {
        return endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent(documentContent).withDocumentReference("file.txt").withCheckOptions(
                        CheckOptions.getBuilder().withGuidanceProfileId(guidanceProfileEn.getId()).build()).build(),
                progressListener);
    }

    @Test
    public void testCheckWithDocumentMetaData()
    {
        try {
            CheckResult checkResult = endpoint.check(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent("Thee sentencee contains errors").withDocumentReference(
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
                CheckRequest.ofDocumentContent("Thee sentencee contains errors").withDocumentReference(
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
