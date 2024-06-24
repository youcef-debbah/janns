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

import com.jsoftware95.janns.api.SignalCollector;
import com.jsoftware95.janns.api.WeightedConnection;
import com.jsoftware95.toolkit.InputMappers;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Objects;

/**
 * Contains few implementations of a signal collectors (input summer)
 */
public enum SignalCollectors implements SignalCollector {

    /**
     * A simple signal collector that simply sum up all the {@linkplain WeightedConnection#getWeightedInput() weighted input}
     * of the input connections.
     */
    WEIGHTED_INPUT_SUM {
        @Override
        public Double apply(final Collection<WeightedConnection> weightedLinks) {
            Objects.requireNonNull(weightedLinks);
            double sum = 0;

            for (final WeightedConnection weightedLink : weightedLinks) {
                sum += weightedLink.getWeightedInput();
            }

            return sum;
        }
    },

    /**
     * This collector sum up all the {@linkplain WeightedConnection#getWeightedInput() weighted input}
     * of the input connections then map the result to [-1,1] interval (using a {@linkplain InputMappers#LINEAR linear mapping}).
     * <p>
     * how the mapping is done:
     * <ul>
     *     <li>if the result = max possible sum then it will be mapped to 1.0</li>
     *     <li>if the result = min possible sum then it will be mapped to -1.0</li>
     *     <li>if the result = 0 then it will be mapped to 0</li>
     *     <li>reset of the cases are handled in the same way using a linear mapping</li>
     * </ul>
     */
    WEIGHTED_INPUT_AVERAGE {
        @Override
        public Double apply(final Collection<WeightedConnection> weightedLinks) {
            final double sum = WEIGHTED_INPUT_SUM.apply(weightedLinks);
            return InputMappers.LINEAR.map(sum, -weightedLinks.size(), weightedLinks.size(), -1, 1);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(value = "null -> fail;!null -> !null", pure = true)
    public abstract Double apply(Collection<WeightedConnection> weightedLinks);
}
