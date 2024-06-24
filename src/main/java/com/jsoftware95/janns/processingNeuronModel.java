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
import com.jsoftware95.janns.api.ProcessingNeuron;
import com.jsoftware95.janns.api.SignalCollector;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Objects;

/**
 * This class implements the base functionality of a {@link ProcessingNeuron}.
 * <p>
 * Subclasses have to implement only: {@link #getActualNeuron()}
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implement this interface
 */
public abstract class processingNeuronModel<T extends processingNeuronModel<T>> extends NeuronModel<T> implements ProcessingNeuron<T> {
    private static final long serialVersionUID = 3653418001280253678L;
    private static final Logger log = LogManager.getLogger();

    /*
     * the latest output calculated by this neuron
     */
    private double output;

    /*
     * bias that is being used to process the input
     */
    private double bias;

    /*
     * the function that sum all the inputs (collect the signals) before processing it
     */
    private SignalCollector signalCollector;

    /*
     * the function that determine the output based on the sum of collected signals and the bias
     */
    private ActivationFunction activationFunction;

    /*
     * connections that feed the input to this instance
     */
    private Collection<WeightedConnection> connections;

    /**
     * Constructs a new neuron with the given input connections and the default the {@link SignalCollector}
     * and {@link ActivationFunction} then add it to the parentLayer.
     * <p>
     * Note that {@code inputConnections} can be {@code null} or empty
     * <p>
     * Default {@code SignalCollector} is {@link SignalCollectors#WEIGHTED_INPUT_AVERAGE}
     * <p>
     * Default {@code ActivationFunction} is {@link ActivationFunctions#RELU_CAPPED_BY_ONE}
     * <p>
     * Note that the newly constructed instance will have a unique {@code id} within the {@code parentLayer}
     *
     * @param parentLayer the layer that holds this neuron
     * @param connections connections that feed the input to this instance
     *
     * @throws NullPointerException if the parentLayer is {@code null}
     */
    protected processingNeuronModel(final Layer<T> parentLayer, final Collection<WeightedConnection> connections) {
        this(parentLayer, connections, ActivationFunctions.RELU_CAPPED_BY_ONE);
    }

    /**
     * Constructs a new neuron with the given input connections, {@link ActivationFunction} and the default {@link
     * SignalCollector} then add it to the parentLayer.
     * <p>
     * Note that {@code inputConnections} can be {@code null} or empty
     * <p>
     * Default {@code SignalCollector} is {@link SignalCollectors#WEIGHTED_INPUT_AVERAGE}
     * <p>
     * Note that the newly constructed instance will have a unique {@code id} within the {@code parentLayer}
     *
     * @param parentLayer        the layer that holds this neuron
     * @param connections        connections that feed the input to this instance
     * @param activationFunction the activation function of this neuron (if {@code null}
     *                           {@link ActivationFunctions#RELU_CAPPED_BY_ONE} will be used)
     *
     * @throws NullPointerException if the parentLayer is {@code null}
     */
    protected processingNeuronModel(final Layer<T> parentLayer, final Collection<WeightedConnection> connections,
                                    final ActivationFunction activationFunction) {
        super(parentLayer);
        Objects.requireNonNull(connections);

        if (activationFunction != null)
            this.activationFunction = activationFunction;
        else
            this.activationFunction = ActivationFunctions.RELU_CAPPED_BY_ONE;

        signalCollector = SignalCollectors.WEIGHTED_INPUT_AVERAGE;
        bias = getNewBias();
        helpSettingInputConnections(connections); // this will process the input implicitly
    }

    /*
     * A helper method that generate a new bias
     */
    private double getNewBias() {
        return (1 - Math.random()) * 2 - 1;
    }

    /*
     * A helper method that sets input connections
     */
    private void helpSettingInputConnections(final Collection<WeightedConnection> connections) {
        this.connections = connections;
        processInput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double processInput() {
        return output = activationFunction.apply(signalCollector.apply(connections) + bias);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOutput() {
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBias() {
        bias = getNewBias();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignalCollector getSignalCollector() {
        return signalCollector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSignalCollector(final SignalCollector signalCollector) {
        this.signalCollector = Objects.requireNonNull(signalCollector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivationFunction(final ActivationFunction activationFunction) {
        this.activationFunction = Objects.requireNonNull(activationFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<WeightedConnection> getConnections() {
        return connections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnections(final Collection<WeightedConnection> connections) {
        Objects.requireNonNull(connections);
        helpSettingInputConnections(connections);
    }

    /**
     * Returns the bias that is currently being used to calculates the output.
     * <p>
     * Note that bias is always in interval [-1, 1]
     *
     * @return the bias that this neuron is currently using
     */
    protected double getBias() {
        return bias;
    }

    /**
     * Sets the bias that will be used to calculates the output.
     *
     * @param bias the new bias for this neuron
     *
     * @throws IllegalArgumentException if the new bias is outside [-1, 1]
     */
    protected void setBias(final double bias) {
        if (bias < -1 || bias > 1)
            throw new IllegalArgumentException("bias must be in [-1, 1], found: " + bias);
        this.bias = bias;
    }
}