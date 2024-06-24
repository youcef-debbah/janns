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

package com.jsoftware95.jpanzer.game;

import java.util.Objects;

@SuppressWarnings({"MethodParameterOfConcreteClass", "InstanceVariableOfConcreteClass"})
public class Context implements AutoCloseable {

    private TankClient tankClient;
    private Tank tank;
    private Tank enemy;
    private boolean isClosed;

    public Context(final TankClient tankClient, final Tank tank, final Tank enemy) {
        this.tankClient = Objects.requireNonNull(tankClient);
        this.tank = Objects.requireNonNull(tank);
        this.enemy = Objects.requireNonNull(enemy);
    }

    public boolean isLoaded() {
        ensureOpen();
        return tank.isLoaded();
    }

    public boolean isEnemyLoaded() {
        ensureOpen();
        return enemy.isLoaded();
    }

    public int getDistanceFromEnemyX() {
        ensureOpen();
        return tank.getX() - enemy.getX();
    }

    public int getDistanceFromEnemyY() {
        ensureOpen();
        return tank.getY() - enemy.getY();
    }

    public double getCurrentDirectionX() {
        ensureOpen();

        switch (tank.getDirection()) {
            case L:
                return -1.0;
            case R:
                return 1.0;
            default:
                return 0.0;
        }
    }

    public double getCurrentDirectionY() {
        ensureOpen();

        switch (tank.getDirection()) {
            case U:
                return -1.0;
            case D:
                return 1.0;
            default:
                return 0.0;
        }
    }

    public int getSpeedX() {
        ensureOpen();
        return tank.getCurrentSpeedX();
    }

    public int getSpeedY() {
        ensureOpen();
        return tank.getCurrentSpeedY();
    }

    public int getEnemySpeedX() {
        ensureOpen();
        return enemy.getCurrentSpeedX();
    }

    public int getEnemySpeedY() {
        ensureOpen();
        return enemy.getCurrentSpeedY();
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void close() {
        isClosed = true;
        tankClient = null;
        tank = null;
        enemy = null;
    }

    private void ensureOpen() {
        if (isClosed)
            throw new IllegalStateException("context is already closed");
    }
}
