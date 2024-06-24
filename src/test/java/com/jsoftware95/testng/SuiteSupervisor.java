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

import com.jsoftware95.toolkit.SystemLogger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * A listener that adds a decent logging for standard output stream ({@code System.out} and
 * {@code System.err}). see {@link #LOG_SYSTEM_OUTPUT} and {@link #DEBUG_INFO} for more details
 */
public class SuiteSupervisor implements ISuiteListener {

    /**
     * Define "log-system-output" parameter.
     * <p>
     * if set to {@code true} then the standard output streams ({@code System.out} and
     * {@code System.err}) will forward the input to a logger named "com.jsoftware95.testng.SystemLogger",
     * default to {@code false}
     */
    public static final String LOG_SYSTEM_OUTPUT = "log-system-output";

    /**
     * Define "debug-info" parameter.
     * <p>
     * if parameter "log-system-output" is {@code true} then setting this parameter to {@code true} will
     * show additional about the method that contains the corresponding {@code System.out}/{@code System.err}
     * call, if "log-system-output" is set to {@code false} than this parameter is ignored
     */
    public static final String DEBUG_INFO = "debug-info";

    @Override
    public void onStart(final ISuite suite) {
        final String logSysout = suite.getParameter(LOG_SYSTEM_OUTPUT);
        final String debugInfo = suite.getParameter(DEBUG_INFO);

        if ("true".equalsIgnoreCase(logSysout))
            SystemLogger.linkWithSystem("true".equalsIgnoreCase(debugInfo));
    }

    @Override
    public void onFinish(final ISuite suite) {
        final String logSysout = suite.getParameter(LOG_SYSTEM_OUTPUT);
        if ("true".equalsIgnoreCase(logSysout))
            SystemLogger.unlink();
    }

}
