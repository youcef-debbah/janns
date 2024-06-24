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

package com.jsoftware95.toolkit;

/**
 * Contains implementations of InputMapper
 */
public enum InputMappers implements InputMapper {

    /**
     * An InputMapper that use a linear formula to map the input from [{@code min}, {@code max}] to [{@code newMin},
     * {@code newMax}]. such as:
     * <pre>
     *     d(min, input)/d(newMin, result) = d(input, max)/d(result, newMax)
     * </pre>
     */
    LINEAR {
        @Override
        public double map(final double input, final double min, final double max, final double newMin, final double newMax) {
            if (min > max)
                throw new IllegalArgumentException("InputMapper min can't be greater than the max: " + min + " and " + max);
            if (newMin > newMax)
                throw new IllegalArgumentException("InputMapper newMin can't be greater than the newMax: " + newMin + " and " + newMax);

            if (input <= min) {
                return newMin;
            } else if (input >= max) {
                return newMax;
            } else {
                // input in [min, max] but we need it in [newMin, newMax]
                double newInput = input - min;
                // new input is between [0, max - min]
                final double scaleFactor = (newMax - newMin) / (max - min);
                newInput *= scaleFactor;
                // new input is between [0, newMax - newMin]
                return newInput + newMin;
            }
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract double map(double input, double min, double max, double newMin, double newMax);

    /**
     * {@inheritDoc}
     */
    @Override
    public double mapToZeroOne(final double input, final double min, final double max) {
        return InputMapper.super.mapToZeroOne(input, min, max); // this method is here just to show up on the javadoc
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double map(final double input, final double min, final double max) {
        return InputMapper.super.map(input, min, max); // this method is here just to show up on the javadoc
    }

}
