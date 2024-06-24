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
 * Represents an Output {@link Neuron} in an Artificial Neuron Network (ANN) that is contained in an output layer. This type of
 * neuron <strong>perform calculation</strong> to set it's output, in the same time it plays the role of an interface between it's ANN and
 * the other API.
 * <p>
 * The type parameter {@code T} is the type of the actual implementation of this interface, in case the class
 * implementing this interface is just a wrapper of another neuron instance (which has the actual implementation) then
 * {@code T} should reference the type of that instance other that this {@code T} parameter should represent the type
 * that implement this interface
 * <p>
 * The type parameter {@code P} is the type of property that the output value of this neuron represent (typically a
 * String or Enum type)
 * <p>
 * A simple implementation may look like this:
 * <pre><code>
 *     public class MyOutputNeuron implements {@literal OutputNeuron<String, MyOutputNeuron>} {
 *         // ...
 *     }
 * </code></pre>
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implement Neuron functionality
 * @param <P> Type of the output property that this output neuron work with
 */
public interface OutputNeuron<P, T extends ProcessingNeuron<T>> extends ProcessingNeuron<T> {

    /**
     * Returns the {@code property} that this output neuron instance represent
     * @return the {@code property} that this neuron represent
     */
    P getProperty();

    /**
     * Returns whether this instance represent the given {@code property}
     *
     * @param property the {@code property} that this output neuron represent
     *
     * @return whether this output neuron represent the given {@code property}
     */
    boolean hasProperty(P property);

    /**
     * Sets a new {@code property} for this neuron to represent
     *
     * @param property the new {@code property} that this output neuron will represent
     *
     * @throws NullPointerException if {@code property} is {@code null}
     */
    void setProperty(P property);

}
