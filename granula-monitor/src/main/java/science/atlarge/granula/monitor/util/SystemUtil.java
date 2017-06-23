/*
 * Copyright 2015 - 2017 Atlarge Research Team,
 * operating at Technische Universiteit Delft
 * and Vrije Universiteit Amsterdam, the Netherlands.
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
package science.atlarge.granula.monitor.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by wlngai on 4/26/16.
 */
public class SystemUtil {

    public static String getComputerName() {
        String hostname = null;
        if (System.getProperty("os.name").startsWith("Windows")) {
            // Windows
            hostname = System.getenv("COMPUTERNAME");
        } else if (System.getenv("HOSTNAME") != null) {
            // MacOS and some Linux systems
            hostname = System.getenv("HOSTNAME");
        } else {
            // other Linux systems
            try {
                Process proc = Runtime.getRuntime().exec("hostname");
                try (InputStream stream = proc.getInputStream()) {
                    try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                        hostname = s.hasNext() ? s.next() : "";
                        hostname = hostname.replaceAll("\n", "");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hostname;
    }
}
