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

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Represents the inputs summing part of a neuron (also called signal collector).
 */
@FunctionalInterface
public interface SignalCollector extends Function<Collection<WeightedConnection>, Double>, Serializable {

    /**
     * Collect (Sum up) the output values of the neurons on the on the upper layer.
     *
     * @param weightedLinks links to neurons on the upper layer
     *
     * @return total input for the neuron having this {@code connections}
     */
    @Override
    Double apply(Collection<WeightedConnection> weightedLinks);
}
