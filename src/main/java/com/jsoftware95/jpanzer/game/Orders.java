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

@SuppressWarnings("BooleanParameter")
public class Orders {

    boolean moveRight;
    boolean moveLeft;
    boolean moveUp;
    boolean moveDown;
    boolean fire;

    public void setMoveRight(final boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setMoveLeft(final boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveUp(final boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(final boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setFire(final boolean fire) {
        this.fire = fire;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public boolean isFire() {
        return fire;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Orders orders = (Orders) o;
        return moveRight == orders.moveRight &&
                moveLeft == orders.moveLeft &&
                moveUp == orders.moveUp &&
                moveDown == orders.moveDown &&
                fire == orders.fire;
    }

    @Override
    public int hashCode() {

        return Objects.hash(moveRight, moveLeft, moveUp, moveDown, fire);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("{ ");

        if (fire)
            builder.append("fire ");

        if (moveUp)
            builder.append("move-up ");

        if (moveDown)
            builder.append("move-down ");

        if (moveRight)
            builder.append("move-right ");

        if (moveLeft)
            builder.append("move-left ");

        return builder.append('}').toString();
    }
}
