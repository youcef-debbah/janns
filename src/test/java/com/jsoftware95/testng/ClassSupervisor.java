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

import com.jsoftware95.toolkit.Wrapper;
import org.testng.IClass;
import org.testng.IClassListener;
import org.testng.ITestClass;
import org.testng.TestNGException;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * This listener adds instances mocking scope. After creating a new instance of a any test class and just before
 * invoking any test method on it this listener will create a new mocking session and store it, then after invoking all
 * test methods on the given test class instance the session is finished (and any required validation is done) also the
 * state of mocks is reset, note that this resting doesn't include mocks created without annotation.
 */
public class ClassSupervisor implements IClassListener {

    public static final String INSTANCES_SCOPE = "instances";

    private final MockingSessionScope mockingManager = new MockitoSessionScope(INSTANCES_SCOPE);

    @Override
    public void onBeforeClass(final ITestClass testClass) {
        if (isMockingAtInstancesScope(testClass)) {
            final XmlTest xml = testClass.getXmlTest();
            if (XmlSuite.ParallelMode.METHODS == xml.getParallel())
                throw new TestNGException("mocking session scope 'instances' is not available when test parallel mode is 'methods'");

            final Object[] instances = testClass.getInstances(false);
            if (instances != null) {
                final String strictness = xml.getParameter(MockitoSessionScope.MOCKING_STRICTNESS);

                for (final Object instance : instances)
                    mockingManager.startMocking(instance, new Wrapper<>(instance), strictness);
            }
        }
    }

    @Override
    public void onAfterClass(final ITestClass testClass) {
        if (isMockingAtInstancesScope(testClass)) {
            final Object[] instances = testClass.getInstances(false);
            if (instances != null)
                for (final Object instance : instances)
                    mockingManager.finishMocking(new Wrapper<>(instance));
        }
    }

    private boolean isMockingAtInstancesScope(final IClass testClass) {
        final String scope = testClass.getXmlTest().getParameter(MockitoSessionScope.MOCKING_SCOPE);
        return INSTANCES_SCOPE.equalsIgnoreCase(scope);
    }

}
