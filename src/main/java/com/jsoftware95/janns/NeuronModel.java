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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class implements the base functionality of a {@link Neuron} in an Artificial Neural Network.
 * <p>
 * Subclasses have to provide implementation for only: {@link #getOutput()} and {@link #getActualNeuron()}
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 */
public abstract class NeuronModel<T extends Neuron<T>> implements Neuron<T> {
    private static final long serialVersionUID = 520200823682832207L;

    private static final Logger log = LogManager.getLogger();

    /*
     * identifier of this instance (must be unique within parent layer scope)
     */
    private int id;

    /*
     * the layer that holds this neuron
     */
    private Layer<T> parentLayer;

    /**
     * Constructs a ProcessingNeuron instance with the given parent Layer and an auto generated id, then add it to it's
     * parent.
     *
     * @param parentLayer the layer that will contain the new neuron instance
     *
     * @throws NullPointerException if {@code parentLayer} is {@code null}
     */
    protected NeuronModel(final Layer<T> parentLayer) {
        Objects.requireNonNull(parentLayer);
        helpSettingParentLayer(parentLayer); // this will set a unique id within the parent layer scope implicitly
        log.trace("new instance: " + this);
    }

    /*
     * A helper method that's used by the constructor and parentLayer setter.
     */
    private void helpSettingParentLayer(final Layer<T> parentLayer) {
        assert parentLayer != null;
        this.parentLayer = parentLayer;
        this.id = parentLayer.size();

        parentLayer.add(getActualNeuron());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getParentLayerId() {
        return parentLayer.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Layer<T> getParentLayer() {
        return parentLayer;
    }

    /**
     * Sets the given {@code parentLayer} as the containing layer (parent layer) of this neuron, remove this instance
     * from the current parent layer and sets a new {@code id} for this neuron (a call to
     * {@link LayerModel#add(NeuronModel)} on the parent layer is needed to register this instance).
     *
     * @param parentLayer the new parent layer (container layer)
     *
     * @throws IllegalArgumentException if the {@code parentLayer} is {@code null}
     */
    protected void setParentLayer(final Layer<T> parentLayer) {
        Objects.requireNonNull(parentLayer, "parent layer must be no null for a neuron instance: " + toString());

        if (parentLayer.equals(this.parentLayer)) {
            log.info("re-setting an already assigned parent layer was skipped for: " + this);
            return;
        }

        if (this.parentLayer != null) {
            assert this.parentLayer.contains(getActualNeuron());
            this.parentLayer.remove(getActualNeuron());
        }

        helpSettingParentLayer(parentLayer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final @NotNull Neuron<?> that) {
        Objects.requireNonNull(that);
        return Double.compare(this.getOutput(), that.getOutput());
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (!this.getClass().isInstance(that)) return false;
        final Neuron<?> thatNeuron = this.getClass().cast(that);
        return id == thatNeuron.getId() && getParentLayerId() == thatNeuron.getParentLayerId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getParentLayerId());
    }

    @Override
    public String toString() {
        return "ProcessingNeuron{" +
                "id=" + id +
                ", output=" + getOutput() +
                ", parentLayer=" + parentLayer +
                '}';
    }
}
