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
 * Represents an Input layer that contain a number of {@link Neuron} instances. This layer <strong>does not</strong>
 * perform any calculation to determine the output instead it represent a collection of (property/value) paris, each
 * value is set explicitly.
 * <p>
 * Note that this layer can contain multiple occurrence of the same property (typically it doesn't), and setting the
 * value of such property will set the value of all the occurrences (to create such an input layer call {@link
 * #initInputs(Object[])} and pass it an array that contains equal instances).
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implements this Interface
 * @param <P> the type of properties constants
 */
public interface InputLayer<P, T extends Neuron<T>> extends Layer<T> {

    /**
     * Clear all the neurons on this layer and add a new neuron for each given property
     *
     * @param properties the properties that this input layer will introduce to the rest of the ANN
     */
    void initInputs(P[] properties);

    /**
     * Returns all the properties that this input layer represents (duplication are allowed)
     *
     * @return properties that this input layer instance introduce to the underlying Neural Network
     */
    P[] getProperties();

    /**
     * Sets the value of this property.
     *
     * @param property the {@code property} to be set
     * @param value    the new value of this {@code property}
     *
     * @throws IllegalArgumentException is {@code property} is not found
     */
    void setInput(P property, boolean value);

    /**
     * Sets the value of this property explicitly (as it is).
     *
     * @param property the {@code property} to be set
     * @param value    the new value of this {@code property}
     *
     * @throws IllegalArgumentException is {@code property} is not found or the value is outside [-1.0, 1.0]
     */
    void setInput(P property, double value);

    /**
     * Sets the value of this property by mapping the given {@code value} from [{@code min}, {@code max}] to [0.0, 1.0].
     *
     * @param property the {@code property} to be set
     * @param value    the new value of this {@code property}
     * @param min      the minimum {@code value} this {@code property} can take
     * @param max      the maximum {@code value} this {@code property} can take
     *
     * @throws IllegalArgumentException is {@code property} is not found
     */
    void setInput(P property, double value, double min, double max);

}
