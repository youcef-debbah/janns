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
import com.jsoftware95.janns.api.Neuron;
import com.jsoftware95.janns.api.OutputLayer;
import com.jsoftware95.janns.api.ProcessingNeuron;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A simple implementation of {@link OutputLayer} that uses an Enum constants as properties (for the seek of an extra
 * type safety). This layer <strong>perform</strong> the needed calculations to determine the output of the neurons it
 * contains and in the same time work as an interface between the ANN and the rest of APIs (see {@linkplain OutputLayer
 * Output Layer} for more details).
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <E> the Enum type that holds properties constants
 */
public class EnumOutputLayer<E extends Enum<E>> extends LayerModel<EnumOutputNeuron<E>> implements OutputLayer<Enum<E>, EnumOutputNeuron<E>> {
    private static final long serialVersionUID = -3990675388629696884L;

    private static final Logger log = LogManager.getLogger();

    private LayerModel<? extends Neuron<?>> upperLayer;

    private Enum<E>[] properties;

    /**
     * Create a new output layer with the given upper layer and properties.
     * <p>
     * Note {@code upperLayer} can be {@code null}, but setting the {@code upperLayer} to {@code null} using the setter
     * will throw an exception, also most methods will throw an exception while the upper layer is not initialized yet.
     *
     * @param upperLayer a layer that it's output will be used to calc the output of this layer (may be {@code null})
     * @param properties the properties that this output layer will introduce to the rest of APIs
     *
     * @throws IllegalArgumentException if {@code properties} are {@code null} or {@code empty}
     */
    public EnumOutputLayer(final LayerModel<? extends Neuron<?>> upperLayer, final Enum<E>[] properties) {
        super(LayerModel.OUTPUT_LAYER_ID);
        if (properties == null || properties.length < 1)
            throw new IllegalArgumentException("at least one property is required as an output");

        this.upperLayer = upperLayer;
        helpInitOutputs(properties);
    }

    private void checkState() {
        if (upperLayer == null)
            throw new IllegalStateException("output layer is not properly initialized yet (has null upper layer)");
    }

    /*
     * clear all neurons and create a new one for each given property
     */
    private void helpInitOutputs(final Enum<E>[] properties) {
        assert properties != null && properties.length > 0;
        this.properties = properties;

        clearNeurons();
        for (final Enum<E> property : properties) {
            // this neuron will add itself to his parent (this layer)
            new EnumOutputNeuron<>(this, getConnectionsWith(upperLayer), property);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if {@code upperLayer} is {@code null}
     */
    @Override
    public void initOutputs(final Enum<E>[] properties) {
        checkState();
        if (properties == null || properties.length < 1)
            throw new IllegalArgumentException("at least one property is required as an output");

        helpInitOutputs(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enum<E>[] getProperties() {
        return properties;
    }

    /*
     * create new connections with all neurons on the given layer
     */
    private List<WeightedConnection> getConnectionsWith(final Layer<? extends Neuron<?>> upperLayer) {
        final List<WeightedConnection> weightedLinks = new LinkedList<>();

        if (upperLayer != null) {
            for (int connectionID = 0; connectionID < upperLayer.size(); connectionID++)
                weightedLinks.add(new Connection(connectionID, upperLayer.getNeurons().get(connectionID)));
        }

        return weightedLinks;
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
    public void setUpperLayer(final LayerModel<? extends Neuron<?>> newUpperLayer) {
        this.upperLayer = Objects.requireNonNull(newUpperLayer);

        for (final EnumOutputNeuron<E> neuron : getNeurons()) {
            neuron.setConnections(getConnectionsWith(upperLayer));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if {@code upperLayer} is {@code null}
     */
    @Override
    public void processInput() {
        checkState();
        for (final ProcessingNeuron<?> processingNeuron : getNeurons())
            processingNeuron.processInput();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if {@code upperLayer} is {@code null}
     */
    @Override
    public double getOutput(final Enum<E> property) {
        checkState();
        Objects.requireNonNull(property);
        final List<Double> results = getAllOutputs(property);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("no output found for: " + property);
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new RuntimeException("multiple output found, instead of calling this method call: "
                    + getClass().getName() + ".getMultipleOutput(property) for property: " + property);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if {@code upperLayer} is {@code null}
     */
    @Override
    public List<Double> getMultipleOutput(final Enum<E> property) {
        checkState();
        Objects.requireNonNull(property);
        final List<Double> results = getAllOutputs(property);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("no output found for: " + property);
        } else if (results.size() == 1) {
            throw new RuntimeException("only one output found, instead of calling this method call: "
                    + getClass().getName() + ".getOutput(property) for property: " + property);
        } else {
            return results;
        }
    }

    /*
     * returns all the values that correspond to the given property
     */
    private List<Double> getAllOutputs(final Enum<E> property) {
        assert property != null;

        final List<Double> results = new ArrayList<>(1);
        for (final ProcessingNeuron<EnumOutputNeuron<E>> processingNeuron : getNeurons())
            if (processingNeuron.getActualNeuron().hasProperty(property))
                results.add(processingNeuron.getOutput());

        return results;
    }

}
