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

import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.DoubleStream;

import static com.jsoftware95.janns.SignalCollectors.WEIGHTED_INPUT_AVERAGE;
import static com.jsoftware95.janns.SignalCollectors.WEIGHTED_INPUT_SUM;
import static org.mockito.Mockito.when;

@Test(groups = "unit-tests")
public class SignalCollectorsTest {
    private static final Logger log = LogManager.getLogger(SignalCollectorsTest.class);
    private static final double EPSILON = 0.000_000_1;

    private static final double[] OUTPUTS = {-1, -0.5 , 0, 0.2, 0.5, 0.8, 1};
    private static final double OUTPUTS_SUM = DoubleStream.of(OUTPUTS).sum();
    private static final double OUTPUTS_AVERAGE = DoubleStream.of(OUTPUTS).summaryStatistics().getAverage();

    private final Collection<WeightedConnection> connections;

    {
        connections = new ArrayList<>(OUTPUTS.length);
        for (final double output : OUTPUTS) {
            final WeightedConnection mock = Mockito.mock(WeightedConnection.class);
            when(mock.getWeightedInput()).thenReturn(output);
            connections.add(mock);
        }
    }

    @Test
    public void testApply() {
        Assert.assertEquals(WEIGHTED_INPUT_SUM.apply(connections), OUTPUTS_SUM, EPSILON);
        Assert.assertEquals(WEIGHTED_INPUT_AVERAGE.apply(connections), OUTPUTS_AVERAGE, EPSILON);
        log.info("testApply done.");
    }
}