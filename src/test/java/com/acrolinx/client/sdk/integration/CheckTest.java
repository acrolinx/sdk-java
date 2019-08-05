/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.check.*;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CheckTest extends IntegrationTestBase {
    /**
     * This text should should need some seconds to check.
     */
    static final String longTestText = Strings.repeat("This sentence is nice. \n", 300);
    GuidanceProfile guidanceProfileEn;

    @Mock
    private ProgressListener progressListener;

    @Before
    public void beforeTest() throws AcrolinxException, ExecutionException, InterruptedException {
        assumeTrue(ACROLINX_API_TOKEN != null);
        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN).get();

        for (GuidanceProfile profile : capabilities.getCheckingCapabilities().getGuidanceProfiles()) {
            if (profile.getLanguage().getId().equals("en")) {
                guidanceProfileEn = profile;
                break;
            }
        }
        assumeTrue(guidanceProfileEn != null);
    }

    @Test
    public void startACheck() throws AcrolinxException, InterruptedException, ExecutionException, IOException, URISyntaxException {
        CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build()
        ).get();

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }

    @Test
    public void checkAndGetResult() throws AcrolinxException, InterruptedException {
        CheckResult checkResult = endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build(),
                progressListener
        );

        final Quality quality = checkResult.getQuality();
        assertThat(quality.getScore(), lessThan(100));
        assertThat(quality.getScore(), not(lessThan(40)));
        assertNotNull(quality.getStatus());

        assertEquals(1, checkResult.getReports().size());
        CheckResult.Report scorecard = checkResult.getReport(ReportType.scorecard);
        assertEquals("Score Card", scorecard.getDisplayName());
        assertThat(scorecard.getLink(), startsWith(ACROLINX_URL));
    }

    @Test
    public void checkResultContainsIssues() throws AcrolinxException, InterruptedException {
        CheckResult checkResult = endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("A textt")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build(),
                progressListener
        );

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

    @Test
    public void getTermHarvestingReport() throws AcrolinxException, InterruptedException {
        ArrayList<ReportType> reportTypes = Lists.newArrayList(ReportType.termHarvesting);
        CheckResult checkResult = endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()).setReportTypes(reportTypes))
                        .build(),
                progressListener
        );

        assertEquals(1, checkResult.getReports().size());
        CheckResult.Report termHarvestingReport = checkResult.getReport(ReportType.termHarvesting);
        assertEquals("Term Harvesting", termHarvestingReport.getDisplayName());
        assertThat(termHarvestingReport.getLink(), startsWith(ACROLINX_URL));
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice this problem,
     * we should rewrite it using a mocked server.
     */
    @Test
    public void checkALargeTextAndGetProgress() throws AcrolinxException, InterruptedException, ExecutionException, IOException, URISyntaxException {
        CheckResult checkResult = endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent(longTestText)
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build(),
                progressListener
        );

        verify(progressListener, atLeast(2)).onProgress(argThat(new ProgressMatcher()));

        assertThat(checkResult.getQuality().getScore(), not(lessThan(40)));
    }

    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice this problem,
     * we should rewrite it using a mocked server.
     */
    @Test(expected = CancellationException.class)
    public void cancelCheck() throws InterruptedException, ExecutionException {
        final CheckRequest checkRequest = CheckRequest.ofDocumentContent(longTestText)
                .setDocument(new DocumentDescriptorRequest("file.txt"))
                .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<CheckResult> future = executorService.submit(new Callable<CheckResult>() {
            @Override
            public CheckResult call() throws Exception {
                return endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                        checkRequest,
                        progressListener
                );
            }
        });

        Thread.sleep(500); // Give some time to start the check

        future.cancel(true);
        future.get();
    }

    @Test
    public void testFireMultipleChecksWithoutWaitingForResult() throws AcrolinxException, ExecutionException, InterruptedException {

        int numberOfChecks = 10;

        for (int i = 0; i < numberOfChecks; i++) {

            String uuid = UUID.randomUUID().toString();

            CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent(uuid)
                            .setDocument(new DocumentDescriptorRequest(uuid + ".txt"))
                            .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                            .build()
            ).get();

            assertNotNull(checkResponse);
            assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
            assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
            assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
        }


    }

    @Test
    public void testFireMultipleChecksWaitingForResult() throws AcrolinxException, InterruptedException, ExecutionException, IOException, URISyntaxException {

        int numberOfChecks = 5;

        for (int i = 0; i < numberOfChecks; i++) {

            String uuid = UUID.randomUUID().toString();

            CheckResult checkResult = endpoint.checkAndGetResult(ACROLINX_API_TOKEN,
                    CheckRequest.ofDocumentContent(uuid)
                            .setDocument(new DocumentDescriptorRequest(uuid + ".txt"))
                            .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                            .build(),
                    progressListener
            );

            final Quality quality = checkResult.getQuality();
            assertThat(quality.getScore(), lessThanOrEqualTo(100));
            assertThat(quality.getScore(), not(lessThan(40)));
            assertNotNull(quality.getStatus());
        }
    }

    @Test
    public void checkWithOptions() throws AcrolinxException, ExecutionException, InterruptedException {
        CheckOptions checkOptions = new CheckOptions(guidanceProfileEn.getId());
        checkOptions.setBatchId(UUID.randomUUID().toString());
        checkOptions.setCheckType(CheckType.baseline);
        checkOptions.setContentFormat("txt");
        checkOptions.setDisableCustomFieldValidation(true);
        checkOptions.setLanguageId("en");

        List<ReportType> rtl = new ArrayList<ReportType>();
        rtl.add(ReportType.scorecard);
        rtl.add((ReportType.termHarvesting));
        CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build()
        ).get();

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }


    public static class ProgressMatcher implements ArgumentMatcher<Progress> {
        private double prevPercent = 0;

        @Override
        public boolean matches(Progress value) {
            boolean valid = value.getPercent() >= this.prevPercent && value.getMessage() != null;
            this.prevPercent = value.getPercent();
            return valid;
        }
    }

    Issue findIssueWithFirstSuggestion(List<Issue> issues, String suggestionSurface) {
        for (Issue issue : issues) {
            Issue.Suggestion suggestion = issue.getSuggestions().get(0);
            if (suggestion != null && suggestion.getSurface().equals(suggestionSurface)) {
                return issue;
            }
        }

        return null;
    }
}
