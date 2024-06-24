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

import com.jsoftware95.janns.api.DataMapper;
import com.jsoftware95.janns.api.Neuron;
import com.jsoftware95.janns.api.WeightedConnection;
import com.jsoftware95.toolkit.InputMappers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class model an Artificial Neural Network using Enum constants to represent input and output properties (for the
 * extra type safety). see {@link NeuralNetworkModel} for more details.
 *
 * @param <I> the Enum type that contains input properties
 * @param <O> the Enum type that contains output properties
 */
public class EnumNeuralNetwork<I extends Enum<I>, O extends Enum<O>> extends NeuralNetworkModel<Enum<I>, Enum<O>, EnumInputLayer<I>, EnumOutputLayer<O>> {

    private static final long serialVersionUID = -925051289668491911L;
    private static final Logger log = LogManager.getLogger(EnumNeuralNetwork.class);

    private final Deque<SimpleHiddenLayer> hiddenLayers = new LinkedList<>();

    /**
     * Creates a new neural network instance with the given {@code inputs} and {@code outputs}. The instance will have
     * an input neuron for each {@code input} and an output neuron for each {@code output} but without any hidden layers
     * between them.
     *
     * @param inputs  all properties that should have a corresponding input neuron
     * @param outputs all properties that should have a corresponding output neuron
     *
     * @throws IllegalArgumentException if one of {@code inputs} or {@code outputs} is either {@code null} or
     *                                  {@code empty}
     */
    public EnumNeuralNetwork(final Enum<I>[] inputs, final Enum<O>[] outputs) {
        super(new EnumInputLayer<>(inputs), new EnumOutputLayer<>(null, outputs));
        outputLayer.setUpperLayer(inputLayer);
        log.trace("new enum neural network with inputs: " + Arrays.toString(inputs) + " and outputs: " + Arrays.toString(outputs));
    }

    /**
     * Returns all data that controls the behavior of this Neural Network (biases and connections weights). This data
     * doesn't include the Inputs and
     * Outputs parameters, number of hidden layers and number of neurons per layer as these information represent the
     * structure of this Neural Network rather than it's behavior.
     * <p>
     * Example:
     * <pre><code>
     *    {@literal EnumNeuralNetwork<Inputs, Outputs> network =
     *             new EnumNeuralNetwork<>(Inputs.values(), Outputs.values());}
     *
     *     network.addHiddenLayer(Inputs.values().length);
     *     network.addHiddenLayer(Outputs.values().length);
     *
     *    {@literal EnumNeuralNetwork<Inputs, Outputs> network2 =
     *             new EnumNeuralNetwork<>(Inputs.values(), Outputs.values());}
     *
     *     network2.addHiddenLayer(Inputs.values().length);
     *     network2.addHiddenLayer(Outputs.values().length);
     *
     *    {@literal List<Double>} data = network.getData();
     *     network2.setData(data);
     *     // at this point network and network2 are identical!
     * </code></pre>
     *
     * @return parameters that represents the behavior of this Neural Network
     */
    public Deque<Double> getData() {
        final Deque<Double> data = new LinkedList<>();

        for (final EnumOutputNeuron<O> neuron : outputLayer.getNeurons()) {
            for (final WeightedConnection connection : neuron.getConnections()) {
                data.add(connection.getWeight());
            }
            data.add(neuron.getBias());
        }

        for (final SimpleHiddenLayer hiddenLayer : hiddenLayers) {
            for (final SimpleHiddenNeuron neuron : hiddenLayer.getNeurons()) {
                for (final WeightedConnection connection : neuron.getConnections()) {
                    data.add(connection.getWeight());
                }
                data.add(neuron.getBias());
            }
        }

        return data;
    }

    /**
     * Sets the behavior of this Neural Network by setting all the biases and connections weights.
     * <p>
     * Each element used from the given deque will get <strong>removed</strong> (from the original instance).
     * <p>
     * Note that if any value in the given deque is NaN then a new value it will be generated instead of it.
     *
     * @param data the new biases and connections of this Neural Network
     *
     * @throws NullPointerException     if {@code data} is {@code null}
     * @throws IllegalArgumentException if the size of {@code data} is not sufficient for the whole NN
     */
    public void setData(final Deque<Double> data) {
        Objects.requireNonNull(data);
        final Deque<Double> oldData = getData();

        try {
            setDataForNeurons(outputLayer.getNeurons(), data);

            for (final SimpleHiddenLayer hiddenLayer : hiddenLayers) {
                setDataForNeurons(hiddenLayer.getNeurons(), data);
            }
        } catch (final NoSuchElementException e) {
            setData(oldData);
            throw new IllegalArgumentException("not enough data for this NN (expected: " + oldData.size() + ", found: " + data.size() + ")", e);
        }
    }

    /*
     * set the data for each neuron and if a value is NaN then a new value will be generated instead
     */

    private void setDataForNeurons(final Iterable<? extends processingNeuronModel<?>> neurons, final Deque<Double> data) {
        assert neurons != null;
        assert data != null;

        for (final processingNeuronModel<?> neuron : neurons) {
            for (final WeightedConnection connection : neuron.getConnections()) {
                final Double value = data.pop();
                if (Double.isNaN(value))
                    connection.updateWeight();
                else
                    connection.setWeight(value);
            }

            final Double value = data.pop();
            if (Double.isNaN(value))
                neuron.updateBias();
            else
                neuron.setBias(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHiddenLayer(final int neuronsCount) {
        if (neuronsCount < 1)
            throw new IllegalArgumentException("you can't add an empty hidden layer (at least one neuron is required)");

        addNewHiddenLayerAfter(getBeforeLastLayer(), neuronsCount);
    }

    /*
     * adds a new hidden with the given neurons count and link it the given upperLayer
     */

    private void addNewHiddenLayerAfter(final LayerModel<? extends Neuron<?>> upperLayer, final int neuronsCount) {
        final SimpleHiddenLayer newHiddenLayer = new SimpleHiddenLayer(upperLayer, neuronsCount);
        outputLayer.setUpperLayer(newHiddenLayer);
        hiddenLayers.add(newHiddenLayer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInput() {
        for (final SimpleHiddenLayer hiddenLayer : hiddenLayers)
            hiddenLayer.processInput();

        outputLayer.processInput();
    }

    /*
     * returns the layer that the output layer is currently linked to
     */

    private LayerModel<? extends Neuron<?>> getBeforeLastLayer() {
        if (hiddenLayers.isEmpty())
            return inputLayer;
        else
            return hiddenLayers.getLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInput(final Enum<I> property, final double value) {
        inputLayer.setInput(property, value);
    }

    /**
     * Sets the value of a property in the input layer.
     * <p>
     * The value {@code true} will be mapped to 1.0 and {@code false} to 0.0
     *
     * @param property the input property
     * @param value    the value of the given property
     */
    public void setInput(final Enum<I> property, final boolean value) {
        inputLayer.setInput(property, value);
    }

    /**
     * Sets the value of this property by mapping the given {@code value} from [{@code min}, {@code max}] to [0.0, 1.0].
     * <p>
     * A {@link InputMappers#LINEAR linear mapper} is used to map the value to [0.0, 1.0]
     *
     * @param property the {@code property} to be set
     * @param value    the new value of this {@code property}
     * @param min      the minimum {@code value} this {@code property} can take
     * @param max      the maximum {@code value} this {@code property} can take
     *
     * @throws IllegalArgumentException is {@code property} is not found
     */
    public void setInput(final Enum<I> property, final double value, final double min, final double max) {
        inputLayer.setInput(property, value, min, max);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOutput(final Enum<O> property) {
        return super.getOutput(property);
    }

    public boolean getOutputAsBoolean(final Enum<O> property) {
        return outputLayer.getOutput(property) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Double> getMultipleOutput(final Enum<O> property) {
        return super.getMultipleOutput(property);
    }

    /**
     * Creates a new copies of this instance with identical structure and behavior but with a null input.
     * <p>
     * It's possible to modify the data of the new created copies by providing a {@code dataMapper}, which will be
     * passed each value of the original instance along the number of the copy that currently is being made ({@code 0}
     * will be passed for the first copy). This mean that {@code dataMapper} will be called
     * {@code this.getData().size()} time for each copy that's why it's highly recommended to avoid pouting any heavy
     * possessing inside your {@code dataMapper}.
     *
     * @param clonesCount number of new instance to create
     * @param dataMapper  a function that map each value in the data of original instance to it's equivalent for the
     *                    copy instance (if {@code null} the same value will be kept)
     *
     * @return copies of this instance with null inputs
     * @throws IllegalArgumentException if {@code clonesCount} is less or equals zero
     */
    public List<EnumNeuralNetwork<I, O>> massClone(final int clonesCount, final DataMapper dataMapper) {
        if (clonesCount <= 0)
            throw new IllegalArgumentException("clones count must be more than 0");

        final List<EnumNeuralNetwork<I, O>> clones = new ArrayList<>(clonesCount);
        final Deque<Double> originalData = getData();

        int cloneIndex = 0;
        while (cloneIndex < clonesCount) {
            final EnumNeuralNetwork<I, O> newNetwork = new EnumNeuralNetwork<>(inputLayer.getProperties(), outputLayer.getProperties());
            for (final SimpleHiddenLayer hiddenLayer : hiddenLayers)
                newNetwork.addHiddenLayer(hiddenLayer.size());

            final Deque<Double> data;
            if (dataMapper == null) {
                data = new LinkedList<>(originalData);
            } else {
                data = new LinkedList<>();
                int valueIndex = 0;
                for (final Double value : originalData) {
                    data.add(dataMapper.apply(value, cloneIndex, valueIndex));
                    valueIndex++;
                }
            }

            newNetwork.setData(data);
            clones.add(newNetwork);
            cloneIndex++;
        }

        return clones;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        try (final Formatter formatter = new Formatter(builder)) {

            builder.append("Output layer:\n");
            for (final EnumOutputNeuron<O> neuron : outputLayer.getNeurons()) {
                builder.append(neuron.getProperty()).append("\t");
                for (final WeightedConnection connection : neuron.getConnections()) {
                    formatter.format("%+.2f ", connection.getWeight());
                }
                formatter.format("(%+.2f)\n", neuron.getBias());
            }

            int level = 0;
            for (final SimpleHiddenLayer hiddenLayer : hiddenLayers) {
                builder.append("Hidden layer#").append(level++).append(": \n");
                int i = 0;
                for (final SimpleHiddenNeuron neuron : hiddenLayer.getNeurons()) {
                    builder.append("neuron#").append(i++).append("\t");
                    for (final WeightedConnection connection : neuron.getConnections()) {
                        formatter.format("%+.2f ", connection.getWeight());
                    }
                    formatter.format("(%+.2f)\n", neuron.getBias());
                }
            }
        }
        return builder.toString();
    }

}
