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

import java.util.Collection;

/**
 * Represents a hidden neuron contained in a hidden layer of an Artificial Neural Network
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 */
public class SimpleHiddenNeuron extends processingNeuronModel<SimpleHiddenNeuron> {
    private static final long serialVersionUID = 7177045689750876390L;
    private static final Logger log = LogManager.getLogger();

    /**
     * Constructs a new neuron with the given input connections and the default the {@link SignalCollector}
     * and {@link ActivationFunction}, and add it to the parentLayer .<p>
     * Note that {@code inputConnections} can be {@code null} or empty<p>
     * Default {@code SignalCollector} is {@link SignalCollectors#WEIGHTED_INPUT_AVERAGE}<p>
     * Default {@code ActivationFunction} is {@link ActivationFunctions#RELU_CAPPED_BY_ONE}<p>
     * Note that the newly constructed instance will have a unique {@code id} within the {@code parentLayer}
     *
     * @param parentLayer        the layer that holds this neuron
     * @param inputWeightedLinks connections that feed the input to this instance (may be {@code null})
     *
     * @throws NullPointerException if the parentLayer is {@code null}
     */
    public SimpleHiddenNeuron(final Layer<SimpleHiddenNeuron> parentLayer, final Collection<WeightedConnection> inputWeightedLinks) {
        super(parentLayer, inputWeightedLinks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleHiddenNeuron getActualNeuron() {
        return this;
    }

}
