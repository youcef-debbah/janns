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

import com.jsoftware95.jpanzer.game.TankClient;

public class PlayerVsAI {
    public static void main(final String... args) {
        final Player player = new Player("Human", false);
        final Player bot = new Player("Bot", true);
        bot.loadData("Alpha_g-202_at_21-08-16");

        final TankClient game = new TankClient("Player VS Bot", true);
        game.requestFocus();

        bot.prepare(game.getConnectionToTank1());
        bot.startPlaying();
        game.addListener((p1, p2) -> bot.stopPlaying());
    }
}
