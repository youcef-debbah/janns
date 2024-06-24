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

package com.jsoftware95.toolkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "unit-tests")
public class InputMappersTest {
    private static final Logger log = LogManager.getLogger(InputMappersTest.class);
    private static final double EPSILON = 0.000_000_1;

    private static final double min = -100, max = 100;
    private static final double[] inputs = {2 * min, min, -10, -2, -1, -0.5, 0, 0.5, 1, 2, 10, max, max * 2};

    private static final double newMin = 1000, newMax = 3000;

    @Test
    public void testMap() {
        for (final double input : inputs) {
            final double result = InputMappers.LINEAR.map(input, min, max, newMin, newMax);
            if (input >= max)
                Assert.assertEquals(result, newMax, EPSILON);
            else if (input <= min)
                Assert.assertEquals(result, newMin, EPSILON);
            else
                Assert.assertEquals(d(min, input) / d(newMin, result), d(input, max) / d(result, newMax), EPSILON);
        }
        log.info("testMap done.");
    }

    /*
     * calc distance between two numbers
     */
    private double d(final double x, final double y) {
        return Math.abs(x - y);
    }
}