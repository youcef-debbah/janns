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
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

/**
 * This listener adds methods mocking scope, before invoking <strong>each</strong> test method this listener will create a new
 * session and store it, after each invocation the session is finished (and any required validation is done) also the
 * state of mocks is reset, note that this resting doesn't include mocks created without annotation.
 * <pre><code>
 *     class Test {
 *         // the state of this mock instance will persist between methods invocations
 *         private Type field1 = Mockito.mock(Type.class);
 *        {@literal @Mock}
 *         private Type field2; // this one will lose it's state between methods invocations
 *         // ...
 *     }
 * </code></pre>
 * Note that if the test method failed then any validation exception will be only logged, but if the testing succeed
 * any
 * validation exception will be thrown causing the test to failed (this is done to prevent these exception from hiding
 * the actual exception -thrown by test logic-)
 */
public class MethodsSupervisor implements IInvokedMethodListener {
    private static final Logger log = LogManager.getLogger(MethodsSupervisor.class);
    private static final String METHODS_SCOPE = "methods";
    private final MockingSessionScope mockingManager = new MockitoSessionScope(METHODS_SCOPE);

    @Override
    public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {
        if (method.isTestMethod())
            log.trace("invoking: " + method.getTestMethod().getMethodName());

        final XmlTest xml = testResult.getTestContext().getCurrentXmlTest();
        if (isMockingAtMethodsScope(xml)) {
            final String strictness = xml.getParameter(MockitoSessionScope.MOCKING_STRICTNESS);
            mockingManager.startMocking(method.getTestMethod().getInstance(), method.getTestMethod().getQualifiedName(), strictness);
        }
    }

    @Override
    public void afterInvocation(final IInvokedMethod method, final ITestResult testResult) {
        final XmlTest xml = testResult.getTestContext().getCurrentXmlTest();
        try {
            if (isMockingAtMethodsScope(xml))
                mockingManager.finishMocking(method.getTestMethod().getQualifiedName());
        } catch (final Exception e) {
            if (testResult.isSuccess())
                throw e;
            else
                log.error(e);
        }
    }

    private boolean isMockingAtMethodsScope(final XmlTest xml) {
        final String scope = xml.getParameter(MockitoSessionScope.MOCKING_SCOPE);
        return scope == null || METHODS_SCOPE.equalsIgnoreCase(scope);
    }

}
