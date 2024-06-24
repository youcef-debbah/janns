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

/**
 * Contains listeners and reporters for tests suites that written using TestNG library, these classes provides
 * additional functionality to the tests class like mocking sessions scoops.
 * <p>
 * the test classes is not required to extend any class in order to get the functionality, but the listeners needs
 * to be registered to the TestNG instance that is running the test
 * <p>
 * Some listeners needs parameters to customise their behavior, Note that these parameters are case sensitive but
 * the values are not ({@code true} is the same as {@code TRUE}) and in case of boolean parameters any value is
 * considered {@code false} except "true" and "TRUE".
 *
 * @author youcef
 */

package com.jsoftware95.testng;