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
 * Represents a input mapper that can map a number in a [min, max] interval to it's equivalent in another interval
 * [newMin, newMax].
 */
@FunctionalInterface
public interface InputMapper {

    /**
     * Maps an {@code input} within [{@code min}, {@code max}] to it's equivalent within [{@code newMin}, {@code
     * newMax}] interval.
     * <p>
     * special cases:
     * <ul>
     * <li>if {@code input <= min} then return {@code newMin}</li>
     * <li>if {@code input >= max} then return {@code newMax}</li>
     * </ul>
     *
     * @param input  the original input value
     * @param min    the minimum value that {@code input} can ever take
     * @param max    the maximum value that {@code input} can ever take
     * @param newMin the minimum value that can be returned
     * @param newMax the maximum value that can be returned
     *
     * @return a representation of the original input within the new interval [{@code newMin}, {@code newMax}]
     *         throws IllegalArgumentException if {@code min > max} or {@code newMin > newMax}
     */
    double map(double input, double min, double max, double newMin, double newMax);

    /**
     * Maps an {@code input} within [{@code min}, {@code max}] to it's equivalent within [0.0, 1.0] interval.
     * <p>
     * special cases:
     * <ul>
     *   <li>if {@code input <= min} then return {@code 0.0}</li>
     *   <li>if {@code input >= max} then return {@code 1.0}</li>
     * </ul>
     *
     * @param input the original input value
     * @param min   the minimum value that {@code input} can ever take
     * @param max   the maximum value that {@code input} can ever take
     *
     * @return a representation of the original input within the new interval [0.0, 1.0]
     */
    default double mapToZeroOne(final double input, final double min, final double max) {
        return map(input, min, max, 0.0, 1.0);
    }

    /**
     * Maps an {@code input} within [{@code min}, {@code max}] to it's equivalent within [-1.0, 1.0] interval.
     * <p>
     * special cases:
     * <ul>
     *   <li>if {@code input <= min} then return {@code -1.0}</li>
     *   <li>if {@code input >= max} then return {@code 1.0}</li>
     * </ul>
     *
     * @param input the original input value
     * @param min   the minimum value that {@code input} can ever take
     * @param max   the maximum value that {@code input} can ever take
     *
     * @return a representation of the original input within the new interval [-1.0, 1.0]
     */
    default double map(final double input, final double min, final double max) {
        return map(input, min, max, -1.0, 1.0);
    }
}
