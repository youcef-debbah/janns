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

import com.jsoftware95.janns.api.InputNeuron;
import com.jsoftware95.janns.api.Layer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * A simple implementation of {@link InputNeuron} using an Enum constant as property (instead of String) for extra
 * safety at compilation time.
 * <p>
 * Note that it is not required that each neuron (within one layer) should have a different {@code parameter}, the same
 * {@code parameter} can be used by many (or none) neurons, so that the ANN can have multiple input values labeled by
 * one {@code parameter} (or no values at all for certain {@code parameters}).
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 */
public class EnumInputNeuron<E extends Enum<E>> extends NeuronModel<EnumInputNeuron<E>> implements InputNeuron<Enum<E>, EnumInputNeuron<E>> {
    private static final long serialVersionUID = 1701725713864670942L;
    private static final Logger log = LogManager.getLogger();

    private double input;
    private Enum<E> property;

    /**
     * Constructs an input neuron with the given parent Layer, property and an auto generated id, then add it to it's parent.
     *
     * @param parentLayer the input layer that will contain the new neuron instance
     * @param property the {@code property} that this input neuron will represent
     *
     * @throws NullPointerException if {@code parentLayer} is {@code null}
     */
    public EnumInputNeuron(final Layer<EnumInputNeuron<E>> parentLayer, final Enum<E> property) {
        super(parentLayer);
        this.property = Objects.requireNonNull(property, "you can not use null as a property constant!");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOutput() {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnumInputNeuron<E> getActualNeuron() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInput(final double input) {
        if (input < -1 || input > 1)
            throw new IllegalArgumentException("neuron input must be in [-1, 1] interval, got: " + input);

        this.input = input;
    }

}
