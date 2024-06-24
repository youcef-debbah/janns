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

public class Main {
    public static void main(final String... args) {
        final Player alpha = new Player("Alpha");
        final Player beta = new Player("Beta");

        alpha.loadData("Player-Alpha_g-437_at_13-46-28");
        beta.loadData("Player-Beta_g-437_at_13-46-28");

        final Competition competition = new Competition(alpha, beta);
        competition.start();
    }
}
