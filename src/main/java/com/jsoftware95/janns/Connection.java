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

import com.jsoftware95.janns.api.Neuron;
import com.jsoftware95.janns.api.WeightedConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a connection between two neurons with the associated weight.
 */
public class Connection implements WeightedConnection {
    private static final long serialVersionUID = 8462164713941077025L;
    private static final Logger log = LogManager.getLogger();

    /*
     * identifier of this instance (must be unique within the parent layer scope)
     */
    private int id;

    /*
     * Source neuron for this connection instance.
     * The output of this {@code source} neuron can be collected as an input by the {@code target} neuron
     */
    private Neuron<? extends Neuron<?>> source;

    /*
     * Connection weight
     */
    private double weight;

    /**
     * Creates a new connection instance with {@code source} as input source and a random weight.
     * <p>
     * The generated random weight is greater than 0.0 and less or equal to 1.0, chosen pseudo-randomly with
     * (approximately) uniform distribution from that range.
     *
     * @param id     identifier of this connection (unique within the parent layer scope)
     * @param source a neuron which it's output is considered the input of this connection
     *
     * @throws NullPointerException if the {@code source} is {@code null}
     */
    public Connection(final int id, final Neuron<? extends Neuron<?>> source) {
        this(id, source, null);
    }

    /**
     * Creates a new connection with the given weight and {@code source} as input source.
     *
     * @param id     identifier of this connection (unique within the parent layer scope)
     * @param source a neuron which it's output is considered the input of this connection
     * @param weight the weight of this connection (if {@code null} a new weight will be generated)
     *
     * @throws NullPointerException if the {@code source} is {@code null}
     */
    public Connection(final int id, final Neuron<? extends Neuron<?>> source, final Double weight) {
        this.id = id;
        setSource(source);

        if (weight != null)
            this.weight = weight;
        else
            this.weight = calcNewWeight();

        log.trace("new instance: " + this);
    }

    private double calcNewWeight() {
        return (1 - Math.random()) * 2 - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double updateWeight() {
        return weight = calcNewWeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getInput() {
        return source.getOutput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWeightedInput() {
        return source.getOutput() * weight;
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
    public final void setId(final int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLayerID() {
        return source.getParentLayer().getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Neuron<? extends Neuron<?>> getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setSource(final Neuron<? extends Neuron<?>> source) {
        this.source = Objects.requireNonNull(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWeight(final double weight) {
        if (weight < -1 || weight > 1)
            throw new IllegalArgumentException("connection weight must be in [-1, 1], for: " + weight);
        this.weight = weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final @NotNull WeightedConnection that) {
        Objects.requireNonNull(that);
        return Double.compare(weight, that.getWeight());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WeightedConnection that = (WeightedConnection) o;
        return id == that.getId() && getLayerID() == that.getLayerID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getLayerID());
    }

    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", source=" + source +
                ", weight=" + weight +
                '}';
    }
}