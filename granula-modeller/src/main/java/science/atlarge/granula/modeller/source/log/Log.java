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

package science.atlarge.granula.modeller.source.log;

import science.atlarge.granula.modeller.entity.Archivable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wing on 30-1-15.
 */
public class Log extends Archivable {
    Map<String, String> attrs = new LinkedHashMap<>();
    LogLocation location;

    public void addLogInfo(String name, String value) {
        if(attrs.containsKey(name)) {
            throw new IllegalStateException();
        }
        else {
            attrs.put(name, value);
        }
    }

    public String getAttr(String name) {
        if(attrs.containsKey(name)) {
            return attrs.get(name);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public String getAttrs() {

        StringBuilder str = new StringBuilder();

        for (Map.Entry<String, String> attribute : attrs.entrySet()) {
            str.append(String.format("[%s, %s]\n", attribute.getKey(), attribute.getValue()));
        }
        return str.toString();
    }

    public LogLocation getLocation() {
        return location;
    }

    public void setLocation(LogLocation location) {
        this.location = location;
    }

    public static boolean Match(Log logLeft, Log logRight, String logInfoName) {
        return logLeft.getAttr(logInfoName).equals(logRight.getAttr(logInfoName));
    }

    public boolean isOf(String name, String value) {
        if(attrs.containsKey(LogInfo.InfoName)) {
            return attrs.get(name).equals(value);
        }
        else {
            throw new IllegalStateException(String.format("Log line does not contain attributes %s", name));
        }
    }

    @Override
    public String toString() {
        return "Log \n" +
                getAttrs() +
                "[" + location + ']';
    }
}
