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

package com.jsoftware95.janns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.jsoftware95.janns.ActivationFunctions.RELU;
import static com.jsoftware95.janns.ActivationFunctions.RELU_CAPPED_BY_ONE;

@Test(groups = "unit-tests")
public class ActivationFunctionsTest {
    private static final Logger log = LogManager.getLogger(ActivationFunctionsTest.class);
    private static final double EPSILON = 0.000_000_1;

    private static final double[] NEGATIVE = {-10, -1, -0.5};
    private static final double[] POSITIVE = {0, 0.5, 1.0};

    private static final double BIG_POSITIVE = 10.0;

    @Test
    public void testApply() {
        for (final double x : NEGATIVE) {
            Assert.assertEquals(RELU.apply(x), 0.0, EPSILON);
            Assert.assertEquals(RELU_CAPPED_BY_ONE.apply(x), 0.0, EPSILON);
        }

        for (final double x : POSITIVE) {
            Assert.assertEquals(RELU.apply(x), x, EPSILON);
            Assert.assertEquals(RELU_CAPPED_BY_ONE.apply(x), x, EPSILON);
        }

        Assert.assertEquals(RELU.apply(BIG_POSITIVE), BIG_POSITIVE, EPSILON);
        Assert.assertEquals(RELU_CAPPED_BY_ONE.apply(BIG_POSITIVE), 1.0, EPSILON);

        log.info("testApply done.");
    }
}