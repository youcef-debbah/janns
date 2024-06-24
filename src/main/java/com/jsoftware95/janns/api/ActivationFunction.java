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

package com.jsoftware95.janns.api;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Represents a the activation function of a neuron. The activation function take the collected signals
 * (typically summed inputs) and use it to calculate the output of this neuron.
 */
@FunctionalInterface
public interface ActivationFunction extends Function<Double, Double>, Serializable {

    /**
     * Performs calculation based on the collected signals (sum of all inputs) from neurons on the upper layer.
     *
     * @param collectedInput collected signals (sum of inputs) from neurons on the upper layer
     *
     * @return the output of this neuron based on {@code collectedInput}
     */
    @Override
    Double apply(Double collectedInput);

}
