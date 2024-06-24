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

import com.jsoftware95.janns.LayerModel;

import java.util.List;

/**
 * Represents an output layer that contain a number of {@link Neuron} instances. This layer <strong>perform</strong>
 * calculation to determine the output of the neurons it contains based on the output of neurons on the upper layer
 * (it may include output from other neurons -it's more like a special case-).
 * <p>
 * Each neuron in this layer represent a {@code property}, so this layer can serve as an interface between an ANN and
 * the rest of the API.
 * <p>
 * In case multiple equal {@code properties} are used to initialise an instance of this layer then
 * {@link #getMultipleOutput(Object)} should be used instead of {@link #getOutput(Object)} (or an exception will be
 * thrown)
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implements this Interface
 * @param <P> the type of properties constants
 */
public interface OutputLayer<P, T extends Neuron<T>> extends Layer<T> {

    /**
     * Clear all the neurons on this layer and add a new neuron for each given property
     *
     * @param properties the properties that this output layer will introduce to the rest of APIs
     */
    void initOutputs(P[] properties);

    /**
     * Returns the properties that this output layer is currently holding (duplicate are permitted).
     *
     * @return all the properties that is output layer expose
     */
    P[] getProperties();

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

    /**
     * Sets the upper layer of this layer and link all the neurons on this layer to the new upper layer.
     *
     * @param newUpperLayer the new upper layer of this layer
     *
     * @throws NullPointerException if {@code newUpperLayer} is {@code null}
     */
    void setUpperLayer(LayerModel<? extends Neuron<?>> newUpperLayer);

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
    double getOutput(P property);

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
    List<Double> getMultipleOutput(P property);

}
