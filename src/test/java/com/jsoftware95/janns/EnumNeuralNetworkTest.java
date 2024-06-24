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

import java.util.LinkedList;

public class EnumNeuralNetworkTest {
    private static final Logger log = LogManager.getLogger(EnumNeuralNetworkTest.class);

    @Test
    public void testAverageOutput() {
        final EnumNeuralNetwork<TestInputs, TestOutputs> nn = new EnumNeuralNetwork<>(TestInputs.values(), TestOutputs.values());

        for (final TestInputs input : TestInputs.values()) {
            nn.setInput(input, Math.random());
        }

        double sum = 0;
        for (final TestOutputs output : TestOutputs.values()) {
            final double result = nn.getOutput(output);
            log.info("result: " + result);
            sum += result;
        }

        final double average = sum / TestOutputs.values().length;
        log.info("average = " + average);
    }

    @Test(groups = "unit-tests")
    public void testData() {
        final EnumNeuralNetwork<TestInputs, TestOutputs> network1 = new EnumNeuralNetwork<>(TestInputs.values(), TestOutputs.values());
        network1.addHiddenLayer(TestInputs.values().length);
        network1.addHiddenLayer(TestOutputs.values().length);

        final EnumNeuralNetwork<TestInputs, TestOutputs> network2 = new EnumNeuralNetwork<>(TestInputs.values(), TestOutputs.values());
        network2.addHiddenLayer(TestInputs.values().length);
        network2.addHiddenLayer(TestOutputs.values().length);

        network2.setData(network1.getData());
        Assert.assertEquals(network1.getData(), network2.getData());

        Assert.assertThrows(() -> network1.setData(null));
        Assert.assertThrows(() -> network1.setData(new LinkedList<>()));

        final EnumNeuralNetwork<TestInputs, TestOutputs> network0 = network1.massClone(1, null).get(0);
        Assert.assertEquals(network0.getData(), network1.getData());
    }

    private enum TestInputs {I1, I2, I3, I4, I5, I6, I7, I8, I9, I10}

    private enum TestOutputs {O1, O2, O3, O4, O5}

}