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

package com.jsoftware95.jpanzer.agents;

import com.jsoftware95.jpanzer.game.GameClientConnection;

import java.util.Deque;
import java.util.LinkedList;

public class TrainingDummy extends Player {
    public TrainingDummy(final String name) {
        super(name, false);

        final Deque<Double> newData = new LinkedList<>();

        final int size = brain.getData().size();
        for (int i = 0; i < size; i++) {
            newData.add(0.0);
        }

        brain.setData(newData);
        super.stopPlaying();
    }

    @Override
    public void prepare(GameClientConnection connection) {
    }

    @Override
    public void roundEnded(final boolean won, final Player opponent) {
    }

    @Override
    public void startPlaying() {
    }

    @Override
    public void stopPlaying() {
    }

    @Override
    public boolean stillPlaying() {
        return false;
    }

    @Override
    public boolean isEvolving() {
        return false;
    }

    @Override
    public void setEvolving(boolean evolving) {
    }
}
