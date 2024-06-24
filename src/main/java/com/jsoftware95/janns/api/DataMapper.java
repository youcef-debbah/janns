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
 * Represents a function that map a value from the data of one Neural Network instance it's equivalent value in a new
 * clone.
 */
@FunctionalInterface
public interface DataMapper {

    /**
     * Map an original value to another one for use by a clone.
     *
     * @param value      the value of the original instance
     * @param cloneIndex the index of the clone receiving the returned value (first one has index of {@code 0})
     * @param valueIndex the index of the value within the data of original instance
     *
     * @return the value that will be used by the clone
     */
    double apply(final double value, final int cloneIndex, final int valueIndex);
}
