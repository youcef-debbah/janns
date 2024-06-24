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
import com.jsoftware95.janns.api.OutputNeuron;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Objects;

/**
 * A simple implementation of {@link OutputNeuron} using an Enum constant as property (instead of String) for extra
 * safety at compilation time.
 * <p>
 * Note that it is not required that each neuron (within one layer) should have a different {@code parameter}, the same
 * {@code parameter} can be used by many (or none) neurons, so that the ANN can have multiple output values labeled by
 * one {@code parameter} (or no values at all for certain {@code parameters}).
 * <p>
 * The default activation function in this implementation is {@link ActivationFunctions#POSITIVE_ZERO_NEGATIVE}
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 */
public class EnumOutputNeuron<E extends Enum<E>> extends processingNeuronModel<EnumOutputNeuron<E>> implements OutputNeuron<Enum<E>, EnumOutputNeuron<E>> {

    private static final long serialVersionUID = -4351838398920219521L;
    private static final Logger log = LogManager.getLogger();

    private Enum<E> property;

    /**
     * Constructs an output neuron with the given parent Layer, property and an auto generated id, then add it to it's
     * parent.
     * <p>
     * The default activation function in this implementation is {@link ActivationFunctions#POSITIVE_ZERO_NEGATIVE}
     *
     * @param parentLayer the output layer that will contain the new neuron instance
     * @param connections connections that feed the input to this instance
     * @param property    the {@code property} that this output neuron will represent
     *
     * @throws NullPointerException if {@code parentLayer} is {@code null}
     */
    public EnumOutputNeuron(final Layer<EnumOutputNeuron<E>> parentLayer, final Collection<WeightedConnection> connections, final Enum<E> property) {
        super(parentLayer, connections, ActivationFunctions.POSITIVE_ZERO_NEGATIVE);
        this.property = Objects.requireNonNull(property, "you can not use null as a property constant!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnumOutputNeuron<E> getActualNeuron() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enum<E> getProperty() {
        return property;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProperty(final Enum<E> property) {
        return this.property == property;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperty(final Enum<E> property) {
        this.property = Objects.requireNonNull(property);
    }
}
