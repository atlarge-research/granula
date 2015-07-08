/*
 * Copyright 2015 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.tudelft.pds.granula.util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.InputStream;


public class Notifier {

    public static void main(String[] args) {
        Notifier notifier = new Notifier();
        notifier.playEnd();
    }

    public void playEnd() {

        playSound("notification/end.mid");
    }

    public void playStart() {

        playSound("notification/start.mid");
    }

    public void playMilestone() {

        playSound("notification/milestone.mid");
    }

    public void playSound(String file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
