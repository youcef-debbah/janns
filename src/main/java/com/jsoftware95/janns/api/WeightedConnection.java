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

package com.jsoftware95.janns.api;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represents a connection between two neurons with the associated weight.
 */
public interface WeightedConnection extends Comparable<WeightedConnection>, Serializable {

    /**
     * Sets then returns a new {@code weight} for this connection instance.
     * <p>
     * The generated weight is greater than -1.0 and less or equal to 1.0, chosen pseudo-randomly with (approximately)
     * uniform distribution from that range.
     *
     * @return the new weight
     */
    double updateWeight();

    /**
     * Returns the raw input of this connection instance (same as the output of {@code source} neuron)
     *
     * @return raw input received through this connection {@code source}
     * @see #getWeightedInput
     */
    double getInput();

    /**
     * Returns the weighted input of this connection (the input of this connection multiplied by it's {@code weight}).
     * Note that the result fall in the interval [-1, 1]
     *
     * @return weighted input of the connection
     * @see #getInput()
     * @see #getWeight()
     */
    double getWeightedInput();

    /**
     * Returns the id of this connection instance (unique within it's parent layer scope).
     *
     * @return id of this connection
     * @see #getLayerID()
     */
    int getId();

    /**
     * Sets the id of this connection instance (it <strong>must</strong> be unique within it's parent layer scope).
     *
     * @param id the new id of this instance
     */
    void setId(int id);

    /**
     * Returns the {@code id} of the source's parent layer. this {@code id} combined with the {@code id} of this
     * connection instance provide a unique {@code id} for this instance within a {@code NeuralNetwork}
     *
     * @return id of the layer that {@code source Neural} belongs to
     * @see #getId()
     */
    int getLayerID();

    /**
     * Returns the neuron that it's output will be used as raw input by this instance.
     *
     * @return the {@code source} neuron of this connection
     */
    Neuron<? extends Neuron<?>> getSource();

    /**
     * Sets the neuron that it's output will be used as raw input by this connection instance.
     *
     * @param source the new {@code source} neuron of this connection
     *
     * @throws NullPointerException if {@code source} is {@code null}
     */
    void setSource(Neuron<? extends Neuron<?>> source);

    /**
     * Returns weight for this connection (falls in interval [-1, 1]).
     *
     * @return weight for this connection
     */
    double getWeight();

    /**
     * Set the weight of this connection instance explicitly (must be in [-1, 1]).
     *
     * @param weight the new weight of this connection
     *
     * @throws IllegalArgumentException if {@code weight} is outside [-1, 1]
     */
    void setWeight(double weight);

    /**
     * Compares {@code this} connection to {@code that} connection based on natural order of their {@code output}.<p>
     * Soring using this method will order the instances from the one with smallest weight to the biggest
     *
     * @param that the other connection instance to be compared with {@code this} instance
     *
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     * the specified object
     * @throws NullPointerException if {@code that} is {@code null}
     */
    @Override
    int compareTo(@NotNull WeightedConnection that);
}
