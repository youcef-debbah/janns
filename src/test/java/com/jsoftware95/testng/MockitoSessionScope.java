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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a sessions scope for mocks created by {@link Mockito} library.
 * <p>
 * Note that
 * <p>
 * see {@link #MOCKING_SCOPE} and {@link #MOCKING_STRICTNESS} for more details
 */
class MockitoSessionScope implements MockingSessionScope {

    /**
     * Define the parameter "mocking-scope".
     * <p>
     * TestNG listeners will use this field to read the corresponded parameter which will determine when they should
     * start a new mocking session and when it should be finished
     * <p>
     * known mocking scope:
     * <ul>
     * <li>{@code instances}: each test class instance will have at most one session, provided by
     * {@link ClassSupervisor}</li>
     * <li>{@code methods}: each test method invocation will have it's own session, provided by
     * {@link MethodsSupervisor}</li>
     * </ul>
     */
    public static final String MOCKING_SCOPE = "mocking-scope";

    /**
     * Define the parameter "mocking-strictness".
     * <p>
     * When the session ends it's possible to do some validation and detect some potential problems, this parameter
     * indicate how strict this validation is, supported values are:
     * <ol>
     * <li>{@code lenient} - no added validation.
     * <li>{@code warn} - log warning without intercepting test execution
     * <li>{@code strict_stubs} - throw exception with appropriate details if the validation fails (the default
     * behavior)
     * </ol>
     * <p>
     * Note that the values are case insensitive and any other value will be treated as {@code strict_stubs}
     * Warning! more options may be added in the future so it is not recommended to supply any values other then
     * the mentioned above
     */
    public static final String MOCKING_STRICTNESS = "mocking-strictness";

    private static final Logger log = LogManager.getLogger(MockitoSessionScope.class);

    private final Object lock = new Object();
    private final Map<Object, MockitoSession> sessions = new HashMap<>();

    private final String scope;

    /**
     * Create a new session scope for mocks created by {@link Mockito} library
     *
     * @param scope the name of this scope
     */
    MockitoSessionScope(final String scope) {
        if (scope == null || scope.isEmpty())
            throw new IllegalArgumentException("scope name must be not null or empty");
        this.scope = scope;
    }

    @Override
    public void startMocking(final Object instance, final Object key, final String strictness) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(key);

        log.trace("trying to create a new mocking session (inside scope: " + scope + ") for instance: " + instance);
        final MockitoSession newSession = Mockito.mockitoSession()
                .initMocks(instance)
                .strictness(getStrictness(strictness))
                .startMocking();

        final MockitoSession oldSession;

        synchronized (lock) {
            oldSession = sessions.putIfAbsent(key, newSession);
        }

        if (oldSession == null) {
            log.trace("new mocking session created (" + scope + " scope) for: " + instance);
        } else {
            log.warn("could not create a new mocking session (" + scope + " scope) because instance already have one): " + instance);
        }
    }

    /*
     * parse the the strictness parameter to an actual Strictness level of Mockito library
     */
    private Strictness getStrictness(final String strictness) {
        final Strictness[] values = Strictness.values();
        for (final Strictness value : values)
            if (value.name().equalsIgnoreCase(strictness))
                return value;

        return Strictness.STRICT_STUBS;
    }

    @Override
    public void finishMocking(final Object key) {
        Objects.requireNonNull(key);

        final MockitoSession session;
        synchronized (lock) {
            session = sessions.remove(key);
        }

        if (session == null) {
            log.warn("mocking session not found! scope: " + scope + ", key: " + key);
        } else {
            session.finishMocking();
            log.trace("mocking session finished (scope: " + scope + ")");
        }
    }
}
