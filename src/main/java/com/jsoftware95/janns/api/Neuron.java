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
 * Represents a neuron in an Artificial Neuron Network (ANN) that is contained in a layer (parent layer). the type
 * parameter
 * {@code T} is the type of the actual implementation of this interface, in case the class implementing this interface
 * is just a wrapper of another neuron instance (which has the actual implementation) then {@code T} should reference
 * the type of that instance other that this {@code T} parameter should represent the type that implement this
 * interface, a simple implementation may look like this:
 * <pre><code>
 *     public class MyNeuron implements{@literal Neuron<MyNeuron>} {
 *         // ...
 *     }
 * </code></pre>
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implement this interface
 */
public interface Neuron<T extends Neuron<T>> extends Comparable<Neuron<?>>, Serializable {
    /**
     * Returns the output of this neuron. An implementation of this abstract method should avoid preforming any
     * calculations during this method call, instead it is supposed to return a cached value to avoid performance
     * issues due to frequently invoking it by the rest of the API.
     *
     * @return output of this neuron instance
     */
    double getOutput();

    /**
     * Returns the actual implementation of this interface, a wrapper should return the wrapped implementation while
     * the implementation itself should simply return a reference to {@code this}.
     * <p>
     * This method should be used to avoid casting a reference to a {@code Neuron} to a sub type (while presence of this
     * method itself is a bad OO design, unchecked casting super type to a sub type is by far worst).
     *
     * @return an actual implementation of this interface (typically a reference to {@code this} will be returned)
     */
    T getActualNeuron();

    /**
     * Returns the {@code id} of this neuron parent's layer. this {@code id} combined with the {@code id} of this neuron
     * instance itself provides a unique {@code id} for this instance within a {@code NeuralNetwork}
     *
     * @return the {@code id} of the parent layer of this instance
     * @see #getId()
     */
    int getParentLayerId();

    /**
     * Returns the current layer that is holding this neuron instance.
     *
     * @return current parent layer that contain this neuron
     */
    Layer<T> getParentLayer();

    /**
     * Returns the {@code id} of this {@code ProcessingNeuron} instance (unique within parent layer scope). this id
     * combined with
     * the id of parent layer of this neuron instance itself provides a unique {@code id} for this instance within a
     * {@code NeuralNetwork}
     *
     * @return the {@code id} of this instance
     * @see #getParentLayerId()
     */
    int getId();

    /**
     * Sets the id of this neuron instance (it <tt>must</tt> be unique within it's parent layer scope).
     *
     * @param id the new {@code id} of this neuron
     */
    void setId(int id);

    /**
     * Compares {@code this} neuron to {@code that} neuron based on natural order of their {@code output}.<p>
     * Soring using this method will order the instances from the one with smallest output to the biggest
     *
     * @param that the other neuron instance to be compared with {@code this} instance
     *
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     * the specified object
     * @throws NullPointerException if {@code that} is {@code null}
     */
    @Override
    int compareTo(@NotNull Neuron<?> that);
}
