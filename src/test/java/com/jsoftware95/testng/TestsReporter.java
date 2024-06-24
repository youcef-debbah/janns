/*
 * Copyright 2017-2018 Youcef DEBBAH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsoftware95.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import java.util.List;
import java.util.Map;

/**
 * This TestNG reporter is a simple reporter that log the following (to a logger named:
 * "com.jsoftware95.testng.TestsReporter"):
 * <pre>
 *     Test suite finished: {suite name}
 *     {first test name}{@literal ->} Passed tests: {passed count}, Failed tests: {failed count}, Skipped tests: {skipped count}
 *     {second test name}{@literal ->} ...
 * </pre>
 * such output is automatically logged to the console but without using the default logging facility, as a result this
 * output will only be shown on the console by configuring the logger of this reporter, it is possible to log an
 * equivalent output to other logging channels (like system log files but probably not to the console as it would
 * produce a redundant output) without implementing hacks (such as capturing console output to save it in a log file)
 */
public class TestsReporter implements IReporter {

    // this special logger should write its output to log file directly (without writing anything to the console)
    private static final Logger log = LogManager.getLogger(TestsReporter.class);

    @Override
    public void generateReport(final List<XmlSuite> xmlSuites, final List<ISuite> suites, final String outputDirectory) {
        //Iterating over each suite included in the test
        for (final ISuite suite : suites) {
            log.info("Test suite finished: " + suite.getName());

            final Map<String, ISuiteResult> suiteResults = suite.getResults();

            for (final ISuiteResult result : suiteResults.values()) {
                final ITestContext context = result.getTestContext();

                final String output = context.getName() + " -> " +
                        "Passed tests: " + context.getPassedTests().getAllResults().size() +
                        ", Failed tests: " + context.getFailedTests().getAllResults().size() +
                        ", Skipped tests: " + context.getSkippedTests().getAllResults().size();

                log.info(output);
            }

        }
    }
}
