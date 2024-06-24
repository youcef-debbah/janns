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

import java.util.Objects;

/**
 * A wrapper that use identity equality of the wrapped object to determine equality with other Wrapper instances.<p>
 * Let {@code x} be an instance of this class, then:
 * <ul>
 * <li>{@code x.equals(x)} will return {@code true}</li>
 * <li>{@code x.equals(null)} will return {@code false}</li>
 * <li>{@code x.equals(y} will return {@code true} only and only if {@code x.getValue() == y.getValue()}</li>
 * </ul>
 * In case properties are not needed consider using {@link java.util.Optional} instead.
 * <p>
 * This class may be used to wrap keys of a map so it behave like an {@link java.util.IdentityHashMap}, so instead of
 * implementing such a functionality at class level you can just wrap the desired objects<p>
 * While this class is written to avoid violating well known contracts (such as using {@code IdentityHashMap}), it is
 * kind of a hack, and it is supposed to be used only by utility packages (where you don't control how some classes may
 * be implemented) or to implement a temporal hot fix.
 *
 * @param <T> Type of the wrapped instance
 */
public class Wrapper<T> {

    private T value;

    /**
     * Creates a new wrapper for the given {@code value}, wrappers use identity equality of their values to determine
     * their own equality
     *
     * @param value the instance to be wrapped by this wrapper
     */
    public Wrapper(final T value) {
        this.value = value;
    }

    /**
     * Returns the instance that this wrapper is holding.
     *
     * @return the wrapped value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets a new value for this wrapper to hold
     *
     * @param value the new instance to be wrapped
     */
    public void setValue(final T value) {
        this.value = value;
    }

    /**
     * Returns whether {@code that} instance is equal to {@code this} instance, using identity equality of the wrapped
     * value.<p>
     * Let {@code x} be an instance of this class, then:
     * <ul>
     * <li>{@code x.equals(x)} will return {@code true}</li>
     * <li>{@code x.equals(null)} will return {@code false}</li>
     * <li>{@code x.equals(y} will return {@code true} only and only if {@code x.getValue() == y.getValue()}</li>
     * </ul>
     *
     * @param that another wrapper to compare with
     *
     * @return {@code true} if {@code that} wrapper refer to the same instance as {@code this} wrapper.
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || this.getClass() != that.getClass()) return false;
        final Wrapper<?> thatWrapper = this.getClass().cast(that);
        return this.value == thatWrapper.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a string that describe the instance this wrapper is holding.
     *
     * @return a string that represent the wrapped value
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
