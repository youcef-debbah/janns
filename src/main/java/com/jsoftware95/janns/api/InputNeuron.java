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
 * Represents an input neuron in an Artificial Neuron Network (ANN) that is contained in an input layer. This type of
 * neuron <strong>doesn't</strong> perform any calculation, it is instead a link between other API and the rest part of
 * the ANN
 * by holding a pair of (Property/Value).
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
 *     public class MyInputNeuron implements {@literal InputNeuron<String, MyInputNeuron>} {
 *         // ...
 *     }
 * </code></pre>
 * <p>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implement Neuron functionality
 * @param <P> Type of the input property that this input neuron work with
 */
public interface InputNeuron<P, T extends Neuron<T>> extends Neuron<T> {

    /**
     * Returns the {@code property} that this neuron represent ({@linkplain #hasProperty(Object) has it}).
     *
     * @return the {@code property} that this neuron instance represent.
     */
    P getProperty();

    /**
     * Sets a new {@code property} for this neuron to represent
     *
     * @param property the new {@code property} that this input neuron will represent
     *
     * @throws NullPointerException if {@code property} is {@code null}
     */
    void setProperty(P property);

    /**
     * Returns whether this instance represent the given {@code property}
     *
     * @param property the {@code property} that this input neuron may represent
     *
     * @return whether this input neuron represent the given {@code property}
     */
    boolean hasProperty(P property);

    /**
     * Sets the input of this input neuron, calling {@link #getOutput()} will return this value.
     *
     * @param input the new input of this input neuron
     *
     * @throws IllegalArgumentException if the {@code input} is outside [-1, 1]
     */
    void setInput(double input);

}
