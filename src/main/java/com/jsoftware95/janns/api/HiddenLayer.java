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

/**
 * Represents an hidden layer that contain a number of {@link Neuron} instances.
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implements this Interface
 */
public interface HiddenLayer<T extends ProcessingNeuron<T>> extends Layer<T> {

    /**
     * Clear all neurons on this layer then create new set of neurons, each neuron has connections to all neurons on the
     * upper
     * layer of this layer.
     *
     * @param neuronsCount the number of new neurons to be created
     */
    void initNeurons(int neuronsCount);

    /**
     * Calculate the output of all neurons on this layer
     */
    void processInput();

    /**
     * Return a reference to the upper layer of this layer. each layer uses the output of neurons on it's upper layer to
     * calculate the output of it's own neurons (the output of neurons from other layers may also be used but usually
     * it's not the case)
     *
     * @return return the upper layer of this layer
     */
    Layer<? extends Neuron<?>> getUpperLayer();

}
