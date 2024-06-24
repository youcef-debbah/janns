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

import com.jsoftware95.janns.api.InputLayer;
import com.jsoftware95.janns.api.OutputLayer;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Artificial Neural network (ANN) with one {@linkplain InputLayer Input Layer} and one
 * {@linkplain OutputLayer Output Layer} and zero or more {@linkplain com.jsoftware95.janns.api.HiddenLayer Hidden
 * Layers}.
 *
 * @param <P1> parameters type of the {@code input layer}
 * @param <P2> parameters type of the {@code output layer}
 * @param <I>  type of the {@code input layer}
 * @param <O>  type of the {@code output layer}
 */
public abstract class NeuralNetworkModel<P1, P2, I extends InputLayer<P1, ?>, O extends OutputLayer<P2, ?>> implements Serializable {

    private static final long serialVersionUID = 2657311867502063030L;

    /**
     * The input layer of this ANN
     */
    protected final I inputLayer;

    /**
     * The output layer of this ANN
     */
    protected final O outputLayer;

    /**
     * Creates a new ANN instance with the given {@code inputLayer} and {@code outputLayer}.
     *
     * @param inputLayer  an interface between the other APIs and this ANN
     * @param outputLayer an interface between this ANN and the other APIs
     */
    protected NeuralNetworkModel(final I inputLayer, final O outputLayer) {
        this.inputLayer = Objects.requireNonNull(inputLayer);
        this.outputLayer = Objects.requireNonNull(outputLayer);
    }

    /**
     * Adds a new {@link com.jsoftware95.janns.api.HiddenLayer} to this ANN instance with the given neurons count on it.
     *
     * @param neuronsCount the number of neurons to include in the new layer
     */
    public abstract void addHiddenLayer(final int neuronsCount);

    /**
     * Process the input of:
     * <ol>
     * <li>{@code Hidden Layers} (in the order they've been added with)</li>
     * <li>{@code Output layer}</li>
     * </ol>
     * Typically each layer will use the output of it's upper layer to calculate the output of it's own but it's not a
     * restriction (special cases can be implemented too)
     */
    public abstract void processInput();

    /**
     * Sets the value of a property in the input layer explicitly (as it is).
     *
     * @param property the input property
     * @param value the value of the given property
     */
    public abstract void setInput(final P1 property, final double value);

    /**
     * Returns the value that correspond to a given {@code property} constant.
     *
     * @param property a constant that correspond to exactly <strong>one</strong> value within this layer
     *
     * @return the value of the given {@code property}
     * @throws IllegalArgumentException if the {@code property} is not found
     * @throws RuntimeException         if multiple values are found for the given {@code property} (see {@linkplain
     *                                  #getMultipleOutput(Object) multiple outputs})
     */
    public double getOutput(final P2 property) {
        return outputLayer.getOutput(property);
    }

    /**
     * Returns all the <strong>values</strong> that correspond to the given {@code property} constant.
     *
     * @param property a constant that correspond <strong>multiple</strong> values within this layer
     *
     * @return all the values that correspond to the given {@code property}
     * @throws IllegalArgumentException if the {@code property} is not found
     * @throws RuntimeException         if only one value is found for the given {@code property} (see {@linkplain
     *                                  #getOutput(Object) getOutput})
     */
    public List<Double> getMultipleOutput(final P2 property) {
        return outputLayer.getMultipleOutput(property);
    }

}
