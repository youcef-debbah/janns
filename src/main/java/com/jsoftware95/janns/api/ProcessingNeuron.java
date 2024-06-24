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

import com.jsoftware95.janns.Connection;

import java.util.Collection;

/**
 * Represents a ProcessingNeuron in an Artificial Neural Network that can calculate it's {@code output}, comprised of:
 * <ul>
 * <li>a {@code Collection} of input {@link Connection}</li>
 * <li>a {@link SignalCollector} function to sum up the input</li>
 * <li>an {@link ActivationFunction} which use input sum to determine the final output</li>
 * <li>a {@code bias} that modify input sum before applying the activation function</li>
 * </ul>
 * The algorithm used to calculate the output:
 * <pre>
 *     Output = ActivationFunction(SignalCollector(Input) + bias)
 * </pre>
 * Inheritance tree:<br>
 * <img src="doc-files/summary.png" alt="inheritance tree of the main types">
 *
 * @param <T> The type that implement this interface
 */
public interface ProcessingNeuron<T extends ProcessingNeuron<T>> extends Neuron<T> {

    /**
     * Applies the {@code SignalCollector} and {@code ActivationFunction} functions to the input, and cache the output
     * for future use.
     *
     * @return the new output of this {@code ProcessingNeuron}
     * @see #getOutput()
     */
    double processInput();

    /**
     * Returns the last cached output (calculated by calling: {@link #processInput()}) without further calculation.
     *
     * @return the last calculated output (without re-calculating)
     */
    @Override
    double getOutput();

    /**
     * Generate a new {@code bias} for this neuron instance.
     * <p>
     * The generated bias is greater than -1.0 and less or equal to 1.0, chosen pseudo-randomly with (approximately)
     * uniform distribution from that range.
     */
    void updateBias();

    /**
     * Returns the function that collect (sum up) the multiple inputs of this neuron into a single value (which will be
     * then used to calculate the output).
     *
     * @return the {@code SignalCollector} that is currently being used to sum the input
     */
    SignalCollector getSignalCollector();

    /**
     * Sets the function that collect (sum up) the multiple inputs of this neuron into a single value (which will be
     * then used to calculate the output).
     *
     * @param signalCollector the new {@code SignalCollector} that will be used to sum the input
     */
    void setSignalCollector(SignalCollector signalCollector);

    /**
     * Returns the function that determine the output of this neuron.
     *
     * @return the {@code ActivationFunction} that is currently being used to calc the output
     */
    ActivationFunction getActivationFunction();

    /**
     * Sets the function that determine the output of this neuron.
     *
     * @param activationFunction the new {@code ActivationFunction} that will be used to calc the output
     */
    void setActivationFunction(ActivationFunction activationFunction);

    /**
     * Returns the {@code connections} of this neuron with the other neurons (typically neurons on the upper layer).
     *
     * @return all the {@code connections} that this neuron has
     */
    Collection<WeightedConnection> getConnections();

    /**
     * Sets the input connection that link this neuron with the neurons on the upper layer.<p>
     *
     * @param connections new connection that this neuron will use to collect it's input (may be {@code null})
     */
    void setConnections(Collection<WeightedConnection> connections);

}
