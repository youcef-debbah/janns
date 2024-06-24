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

import java.io.Serializable;
import java.util.List;

/**
 * Represents an Artificial Neural Network layer that contain a number of {@link Neuron} instances.
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implements this Interface
 */
public interface Layer<T extends Neuron<T>> extends Serializable {

    /**
     * Returns a unique {@code id} of this layer (within it's neurons network)
     *
     * @return the {@code id} of this layer
     */
    int getId();

    /**
     * Adds a neuron to this layer.
     * <p>
     * Note that usually the <strong>neurons instances are supposed to call this method</strong> (extending
     * {@link com.jsoftware95.janns.NeuronModel} adds this functionality implicitly).
     *
     * @param neuron the {@code neuron} to be added to this layer
     */
    void add(T neuron);

    /**
     * Removes a {@code neuron} from this {@code layer}
     *
     * @param oldNeuron the {@code neuron} to be removed from this {@code layer}
     */
    void remove(T oldNeuron);

    /**
     * Removes all neurons from this layer
     */
    void clearNeurons();

    /**
     * Returns whenever this {@code layer} contains a {@code neuron}
     *
     * @param neuron the {@code neuron} that it's existence will be tested
     *
     * @return whether this {@code layer} contains the given {@code neuron}
     */
    boolean contains(T neuron);

    /**
     * Returns the number of neurons hosted in this layer.
     *
     * @return the number of neurons on this layer
     */
    int size();

    /**
     * Returns a all neurons that this layer is currently holding
     *
     * @return a neurons that this layer contains
     */
    List<T> getNeurons();
}
