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

import com.jsoftware95.janns.api.ActivationFunction;
import com.jsoftware95.janns.api.Layer;
import com.jsoftware95.janns.api.SignalCollector;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.when;

@Test(groups = "unit-tests")
public class EnumOutputNeuronTest {
    private static final Logger log = LogManager.getLogger(EnumOutputNeuronTest.class);
    private static final double EPSILON = 0.000_000_1;
    private static final int ID = 10;
    private static final int SIZE = 5;
    private static final double[] OUTPUTS = {-1, -0.5, 0, 0.2, 0.5, 0.8, 1};
    private final Collection<WeightedConnection> connections;
    @Mock
    private Layer<EnumOutputNeuron<Outputs>> parent;
    @Mock
    private Layer<EnumOutputNeuron<Outputs>> newParent;
    @Mock
    private ActivationFunction activationFunction;
    @Mock
    private SignalCollector signalCollector;
    @Mock
    private Collection<WeightedConnection> connections2;

    {
        connections = new ArrayList<>(OUTPUTS.length);
        for (final double output : OUTPUTS) {
            final WeightedConnection connection = Mockito.mock(WeightedConnection.class);
            when(connection.getWeightedInput()).thenReturn(output);
            connections.add(connection);
        }
    }

    @Test
    public void testConstructors() {
        Mockito.when(parent.getId()).thenReturn(ID);
        Mockito.when(parent.size()).thenReturn(SIZE);
        final EnumOutputNeuron<Outputs> neuron = new EnumOutputNeuron<>(parent, connections, Outputs.OUTPUT1);

        Assert.assertEquals(neuron.getParentLayer(), parent);
        Assert.assertEquals(neuron.getParentLayerId(), ID);
        Assert.assertTrue(neuron.getId() >= 5);

        Assert.assertEquals(neuron.getConnections(), connections);
        Assert.assertNotNull(neuron.getActivationFunction());
        Assert.assertNotNull(neuron.getSignalCollector());

        Assert.assertTrue(neuron.getBias() != 0);

        Assert.assertTrue(neuron.hasProperty(Outputs.OUTPUT1));

        log.info("testConstructors done.");
    }

    @Test
    public void testGettersAndSetters() {
        final EnumOutputNeuron<Outputs> neuron = new EnumOutputNeuron<>(parent, connections, Outputs.OUTPUT1);
        Mockito.when(parent.contains(neuron)).thenReturn(true);

        final int newId = 100;
        final int newSize = 50;
        Mockito.when(newParent.getId()).thenReturn(newId);
        Mockito.when(newParent.size()).thenReturn(newSize);
        neuron.setParentLayer(newParent);

        Assert.assertEquals(neuron.getParentLayer(), newParent);
        Assert.assertEquals(neuron.getParentLayerId(), newId);
        Assert.assertTrue(neuron.getId() >= newSize);

        final int newNeuronId = 1000;
        neuron.setId(newNeuronId);
        Assert.assertEquals(neuron.getId(), newNeuronId);

        Assert.assertNotNull(neuron.getActualNeuron());

        final double bias = 0.5;
        neuron.setBias(bias);
        Assert.assertEquals(neuron.getBias(), bias, EPSILON);

        neuron.setActivationFunction(activationFunction);
        Assert.assertEquals(neuron.getActivationFunction(), activationFunction);

        neuron.setSignalCollector(signalCollector);
        Assert.assertEquals(neuron.getSignalCollector(), signalCollector);

        neuron.setConnections(connections2);
        Assert.assertEquals(neuron.getConnections(), connections2);

        neuron.setProperty(Outputs.OUTPUT2);
        Assert.assertTrue(neuron.hasProperty(Outputs.OUTPUT2));

        log.info("testGettersAndSetters done.");

    }

    @Test
    public void testSpecialCases() {
        Mockito.when(parent.getId()).thenReturn(ID);
        Mockito.when(parent.size()).thenReturn(SIZE);
        final EnumOutputNeuron<Outputs> neuron = new EnumOutputNeuron<>(parent, connections, Outputs.OUTPUT1);

        Assert.assertEquals(neuron.getParentLayer(), parent);
        Assert.assertEquals(neuron.getParentLayerId(), ID);
        Assert.assertTrue(neuron.getId() >= 5);

        Assert.assertEquals(neuron.getConnections(), connections);
        Assert.assertNotNull(neuron.getActivationFunction());
        Assert.assertNotNull(neuron.getSignalCollector());

        Assert.assertTrue(neuron.getBias() != 0);

        Assert.assertThrows(() -> neuron.setProperty(null));
        log.info("testConstructors done.");
    }

    @Test
    public void testGetOutput() {
        final EnumOutputNeuron<Outputs> neuron = new EnumOutputNeuron<>(parent, connections, Outputs.OUTPUT1);

        final double output = neuron.getActivationFunction().apply(neuron.getSignalCollector().apply(connections) + neuron.getBias());
        Assert.assertEquals(neuron.getOutput(), output, EPSILON);

        log.info("testGetOutput done.");
    }

    private enum Outputs { OUTPUT1, OUTPUT2}
}