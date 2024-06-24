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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements the base functionality of a {@link Layer}.
 * <p>
 * Note that sub classes doesn't have nay abstract method to implement instead they should provide additional
 * functionality.
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type of the subclass that actually implements this Interface
 */
public abstract class LayerModel<T extends NeuronModel<T>> implements Layer<T> {

    /**
     * The common {@code id} that each input layer within it's neural network should have
     */
    public static final int INPUT_LAYER_ID = 0;

    /**
     * The common {@code id} that each output layer within it's neural network should have
     */
    public static final int OUTPUT_LAYER_ID = -1;

    private static final long serialVersionUID = 7146019328730012412L;
    private static final Logger log = LogManager.getLogger();

    /*
     * neurons of this layer
     */
    private final List<T> neurons;
    /*
     * id of this layer (must be unique within neural network scope)
     */
    private int id;

    /**
     * Constructs a new layer with the given {@code id} (must be unique within network scope).
     *
     * @param id identifier of the new layer
     */
    protected LayerModel(final int id) {
        this.id = id;
        this.neurons = new ArrayList<>();
        log.trace("new instance: " + this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the {@code id} of this layer (must be unique within network scope).
     *
     * @param id the new identifier of this layer
     */
    protected void setId(final int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final T neuron) {
        Objects.requireNonNull(neuron);

        if (this != neuron.getParentLayer())
            neuron.setParentLayer(this);

        if (!neurons.contains(neuron))
            neurons.add(neuron);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final T oldNeuron) {
        neurons.remove(oldNeuron);
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setId(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearNeurons() {
        neurons.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final T neuron) {
        return neurons.contains(neuron);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return neurons.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getNeurons() {
        return neurons;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (!this.getClass().isInstance(that)) return false;
        final Layer<?> thatLayer = this.getClass().cast(that);
        return this.id == thatLayer.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Layer{" + "id=" + id + ", size=" + size() + '}';
    }
}
