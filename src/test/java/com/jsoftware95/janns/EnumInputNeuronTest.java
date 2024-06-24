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

import com.jsoftware95.janns.api.Layer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "unit-tests")
public class EnumInputNeuronTest {
    private static final Logger log = LogManager.getLogger(EnumInputNeuronTest.class);
    private static final double EPSILON = 0.000_000_1;
    private static final int ID = 10;
    private static final int SIZE = 5;

    @Mock
    private Layer<EnumInputNeuron<Inputs>> parent;

    @Mock
    private Layer<EnumInputNeuron<Inputs>> newParent;

    @Test
    public void testConstructors() {
        Mockito.when(parent.getId()).thenReturn(ID);
        Mockito.when(parent.size()).thenReturn(SIZE);
        final EnumInputNeuron<Inputs> neuron = new EnumInputNeuron<>(parent, Inputs.INPUT1);

        Assert.assertEquals(neuron.getParentLayer(), parent);
        Assert.assertEquals(neuron.getParentLayerId(), ID);
        Assert.assertTrue(neuron.getId() >= 5);

        Assert.assertTrue(neuron.hasProperty(Inputs.INPUT1));

        log.info("testConstructors done.");
    }

    @Test
    public void testGettersAndSetters() {
        final EnumInputNeuron<Inputs> neuron = new EnumInputNeuron<>(parent, Inputs.INPUT1);
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

        neuron.setProperty(Inputs.INPUT2);
        Assert.assertTrue(neuron.hasProperty(Inputs.INPUT2));

        final double value = 0.5;
        neuron.setInput(value);
        Assert.assertEquals(neuron.getOutput(), value, EPSILON);

        Assert.assertNotNull(neuron.getActualNeuron());

        log.info("testGettersAndSetters done.");
    }

    @Test
    public void testSpecialCases() {
        Mockito.when(parent.size()).thenReturn(SIZE);
        final EnumInputNeuron<Inputs> neuron = new EnumInputNeuron<>(parent, Inputs.INPUT1);

        neuron.setInput(0);
        neuron.setInput(1);

        Assert.assertThrows(() -> new EnumInputNeuron<>(null, Inputs.INPUT1));
        Assert.assertThrows(() -> new EnumInputNeuron<>(parent, null));

        Assert.assertThrows(() -> neuron.setParentLayer(null));
        Assert.assertThrows(() -> neuron.setProperty(null));

        Assert.assertThrows(() -> neuron.setInput(2));
        Assert.assertThrows(() -> neuron.setInput(-2));

        log.info("testSpecialCases done.");
    }

    private enum Inputs {INPUT1, INPUT2}
}