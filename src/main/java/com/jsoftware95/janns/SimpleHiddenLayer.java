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

import com.jsoftware95.janns.api.HiddenLayer;
import com.jsoftware95.janns.api.Layer;
import com.jsoftware95.janns.api.Neuron;
import com.jsoftware95.janns.api.ProcessingNeuron;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represent a simple hidden layer that use the output of neurons on the upper layer to calculate the output of it's own
 * neurons.
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 */
public class SimpleHiddenLayer extends LayerModel<SimpleHiddenNeuron> implements HiddenLayer<SimpleHiddenNeuron> {
    private static final long serialVersionUID = -1879346778012833105L;
    private static final Logger log = LogManager.getLogger();

    private final LayerModel<? extends Neuron<?>> upperLayer;

    /**
     * Create a new layer instance with the given upper layer and neurons count
     *
     * @param upperLayer   the upper layer of this layer (output of this upper layer will be used to calc output of this
     *                     layer)
     * @param neuronsCount the initial number of neurons that this layer will hold
     */
    public SimpleHiddenLayer(final LayerModel<? extends Neuron<?>> upperLayer, final int neuronsCount) {
        super(upperLayer.getId() + 1);
        this.upperLayer = Objects.requireNonNull(upperLayer);
        if (upperLayer.size() < 1)
            throw new IllegalArgumentException("upper layer must have at least one neuron");
        if (neuronsCount < 1)
            throw new IllegalArgumentException("at least one neuron is required in a Hidden Layer");

        helpInitNeurons(neuronsCount);
    }

    /*
     * initialize the neurons of this layer (and clear the old ones)
     */
    private void helpInitNeurons(final int neuronsCount) {
        assert neuronsCount >= 1;
        clearNeurons();
        for (int neuronID = 0; neuronID < neuronsCount; neuronID++) {
            final List<WeightedConnection> weightedLinks = new ArrayList<>(upperLayer.size());

            for (int connectionID = 0; connectionID < upperLayer.size(); connectionID++) {
                weightedLinks.add(new Connection(connectionID, upperLayer.getNeurons().get(connectionID)));
            }

            // this neuron will add itself to his parent (this layer)
            new SimpleHiddenNeuron(this, weightedLinks);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initNeurons(final int neuronsCount) {
        if (neuronsCount < 1)
            throw new IllegalArgumentException("at least one neuron is required in a Hidden Layer");

        helpInitNeurons(neuronsCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInput() {
        for (final ProcessingNeuron<?> processingNeuron : getNeurons())
            processingNeuron.processInput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Layer<? extends Neuron<?>> getUpperLayer() {
        return upperLayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setId(final int id) {
        super.setId(id);
    }

}
