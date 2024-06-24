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
import com.jsoftware95.toolkit.InputMappers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * A simple implementation of an {@link InputLayer} that uses an Enum constants as properties (for the seek of an extra
 * type safety). This layer <strong>does not</strong> preform any processing, instead it serves as an interface for the
 * underlying ANN by holding pairs of properties and correspond values (see {@linkplain InputLayer Input Layer} for more
 * details)
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <E> the Enum type that holds properties constants
 */
public class EnumInputLayer<E extends Enum<E>> extends LayerModel<EnumInputNeuron<E>> implements InputLayer<Enum<E>, EnumInputNeuron<E>> {
    private static final long serialVersionUID = 1145557656590536807L;

    private static final Logger log = LogManager.getLogger();
    private Enum<E>[] properties;

    /**
     * Create a new input layer and initialize it with the given properties
     *
     * @param properties the properties that this input layer will introduce to the rest of the ANN
     */
    public EnumInputLayer(final Enum<E>[] properties) {
        super(LayerModel.INPUT_LAYER_ID);
        if (properties == null || properties.length < 1)
            throw new IllegalArgumentException("at least one property is required as an input");

        helpInitInputs(properties);
    }

    /*
     * do the actual -dirty- work of clearing all the neurons and creating new ones
     */
    private void helpInitInputs(final Enum<E>[] properties) {
        assert properties.length > 0;
        this.properties = properties;

        clearNeurons();
        for (final Enum<E> property : properties) {
            // this neuron will add itself to his parent (this layer)
            new EnumInputNeuron<>(this, property);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initInputs(final Enum<E>[] properties) {
        if (properties == null || properties.length < 1)
            throw new IllegalArgumentException("at least one property is required as an input");

        helpInitInputs(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enum<E>[] getProperties() {
        return properties;
    }

    /*
     * Sets the input of all the neurons that have the given property
     * (If no such neurons are found this method will flip the table...uhm, I mean throw an exception)
     */
    private void helpSettingInput(final Enum<E> property, final double input) {
        assert property != null;
        boolean found = false;
        for (final EnumInputNeuron<E> neuron : getNeurons())
            if (neuron.hasProperty(property)) {
                neuron.setInput(input);
                found = true;
            }

        if (!found)
            throw new IllegalArgumentException("property not found: " + property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInput(final Enum<E> property, final double value) {
        Objects.requireNonNull(property);
        helpSettingInput(property, value);
        log.trace(property + " is set to: " + value);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The value {@code true} will be mapped to 1.0 and {@code false} to 0.0
     */
    @Override
    public void setInput(final Enum<E> property, final boolean value) {
        Objects.requireNonNull(property);
        final double input = value ? 1 : 0;
        helpSettingInput(property, input);
        log.trace(property + " is set to: " + value + " -> " + input);
    }

    /**
     * {@inheritDoc}
     * <p>
     * A {@link InputMappers#LINEAR linear mapper} is used to map the value to [-1.0, 1.0]
     */
    @Override
    public void setInput(final Enum<E> property, final double value, final double min, final double max) {
        Objects.requireNonNull(property);
        final double input = InputMappers.LINEAR.map(value, min, max);
        helpSettingInput(property, input);
        log.trace(property + " is set to: " + value + " [" + min + ", " + max + "] -> " + input);
    }

}
