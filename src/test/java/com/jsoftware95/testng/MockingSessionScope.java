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

package com.jsoftware95.testng;

/**
 * Represents a scope for storing mocking sessions, each instance of this class should be used to store as many as
 * needed mocking sessions for a <strong>single</strong> scope.
 */
public interface MockingSessionScope {
    /**
     * Adds a new session to this scope, in case another session with the given {@code key} already exists then the
     * old session will be kept instead of creating a new one (a warning will be logged in this case)
     *
     * @param instance   test class instance that contains fields with Mockito annotations to be initialized
     * @param key        a unique key that identify this session
     * @param strictness the strictness parameter value (if {@code null} then {@code strict_stubs} will be used instead)
     *
     * @throws NullPointerException if any of {@code instance} or {@code key} is {@code null}
     */
    void startMocking(Object instance, Object key, String strictness);

    /**
     * Removes the session with associated {@code key} from this scope and perform any required validation.
     *
     * @param key session identifier
     *
     * @throws NullPointerException if {@code key} is {@code null}
     */
    void finishMocking(Object key);
}
