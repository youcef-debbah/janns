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

package org.mockito.configuration;

import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.stubbing.Answer;

/**
 * A Mockito configuration similar to {@code DefaultMockitoConfiguration} except that it enables "Smart Null" value in
 * the default answer.<p>
 * Example:
 * <pre><code>
 *     // using smart nulls with default configuration
 *     Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
 *     // using smart nulls with this configuration
 *     Foo mock = mock(Foo.class);
 * </code></pre>
 * see {@link #getDefaultAnswer()} for more details about "Smart null"
 */
public class MockitoConfiguration extends DefaultMockitoConfiguration {

    /**
     * Sets the default answer for mocks created by {@link Mockito} library to "Smart Null".
     * This answer will behave like the default answer except that it would return an Instance representing
     * {@code null} value instead of returning {@code null} itself, when calling a method on this object a
     * {@link SmartNullPointerException} (with more detailed exception message) will be thrown instead of
     * a {@code NullPointerException}, note that calling methods declared in {@code Object} class (such as
     * {@code toString}) will not throw any exceptions
     *
     * @return an {@code Answer} that return a "Smart Null" instance instead of {@code null} values
     */
    @Override
    public Answer<Object> getDefaultAnswer() {
        return new ReturnsSmartNulls();
    }

}
