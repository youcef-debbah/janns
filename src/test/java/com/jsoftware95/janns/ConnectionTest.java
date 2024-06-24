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

import com.jsoftware95.janns.api.Neuron;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

@Test(groups = "unit-tests")
public class ConnectionTest {
    private static final Logger log = LogManager.getLogger(ConnectionTest.class);

    private static final double EPSILON = 0.000_000_1;
    private static final int id = 10;
    private static final double weight = 0.5;

    @Mock
    private Neuron<? extends Neuron<?>> neuron1;

    @Mock
    private Neuron<? extends Neuron<?>> neuron2;

    @Test
    public void testConstructors() {
        final WeightedConnection connection = new Connection(id, neuron1, weight);
        Assert.assertEquals(connection.getId(), id);
        Assert.assertEquals(connection.getSource(), neuron1);
        Assert.assertEquals(connection.getWeight(), weight, EPSILON);

        log.info("testConstructors done.");
    }

    @Test
    public void testGettersAndSetters() {
        final WeightedConnection connection = new Connection(0, neuron1);

        connection.setId(id);
        connection.setSource(neuron2);
        connection.setWeight(weight);

        Assert.assertEquals(connection.getId(), id);
        Assert.assertEquals(connection.getSource(), neuron2);
        Assert.assertEquals(connection.getWeight(), weight, EPSILON);

        log.info("testGettersAndSetters done.");
    }

    @Test
    public void testSpecialCases() {
        final WeightedConnection connection = new Connection(0, neuron1);

        connection.setWeight(-1);
        connection.setWeight(1);

        Assert.assertThrows(() -> new Connection(id, null, weight));
        Assert.assertNotNull(new Connection(id, neuron1, null).getWeight());

        Assert.assertThrows(() -> connection.setSource(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> connection.setWeight(-2));
        Assert.assertThrows(IllegalArgumentException.class, () -> connection.setWeight(2));

        log.info("testSpecialCases done.");
    }

    @Test
    public void testUpdateWeight() {
        final WeightedConnection connection = new Connection(id, neuron1);

        double oldWeight = connection.getWeight();
        double newWeight;

        for (int i = 0; i < 100; i++) { // probably I should write a better test
            newWeight = connection.updateWeight();
            Assert.assertFalse(newWeight < -1 || newWeight > 1);
            Assert.assertNotEquals(oldWeight, newWeight, EPSILON); // even the epsilon thing is a bad idea >.<
            oldWeight = newWeight;
        }

        log.info("testUpdateWeight done.");
    }

    @Test
    public void testGetWeightedInput() {
        final double output1 = 0.5;
        final double output2 = -0.5;

        when(neuron1.getOutput()).thenReturn(output1);
        when(neuron2.getOutput()).thenReturn(output2);

        final WeightedConnection connection1 = new Connection(1, neuron1, weight);
        final WeightedConnection connection2 = new Connection(2, neuron2, weight);

        Assert.assertEquals(connection1.getWeightedInput(), output1 * weight, EPSILON);
        Assert.assertEquals(connection2.getWeightedInput(), output2 * weight, EPSILON);

        log.info("testGetWeightedInput done.");
    }
}