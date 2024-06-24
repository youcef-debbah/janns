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

import com.jsoftware95.janns.api.ActivationFunction;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * Contains few implementations of a neuron activation function
 */
public enum ActivationFunctions implements ActivationFunction {

    /**
     * The ReLU is an activation function defined as the positive part of its argument.
     */
    RELU {
        @Override
        public Double apply(final Double collectedInput) {
            Objects.requireNonNull(collectedInput);
            if (collectedInput >= 0)
                return collectedInput;
            else
                return 0.0d;
        }
    },

    /**
     * An rectifier with maximum output of {@code 1.0}, see {@link #RELU}
     */
    RELU_CAPPED_BY_ONE {
        @Override
        public Double apply(final Double collectedInput) {
            Objects.requireNonNull(collectedInput);
            if (collectedInput >= 1)
                return 1.0d;
            else
                return RELU.apply(collectedInput);
        }
    },

    /**
     * A simple activation function that return {@code 1.0} if the input is positive, {@code -1.0} if it is negative and
     * {@code 0.0} if it is zero.
     */
    POSITIVE_ZERO_NEGATIVE {
        @Override
        public Double apply(final Double collectedInput) {
            Objects.requireNonNull(collectedInput);
            if (collectedInput > 0)
                return 1.0;
            else if (collectedInput < 0)
                return -1.0;
            else
                return 0.0;
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(value = "null -> fail;!null -> !null", pure = true)
    public abstract Double apply(final Double collectedInput);

}
