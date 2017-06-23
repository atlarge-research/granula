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
package science.atlarge.granula.monitor.info;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessInfo {
    public enum Status {LIVE, DEAD, LOST, UNKNOWN}

    public static String Path2IpAddress(String path) {
        String ip = "unknown";
        try {
            final Pattern pattern = Pattern.compile("@(.+?)/");
            final Matcher matcher = pattern.matcher(path);
            matcher.find();
            ip = matcher.group(1);
        } catch (Exception e) {
            e.printStackTrace();
            ip = "unparsable";
        }
        return ip;
    }
}
