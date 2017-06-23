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

package science.atlarge.granula.modeller.environment;

import science.atlarge.granula.modeller.entity.Containable;
import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 26-2-15.
 */
public class Process extends Containable {

    public Process(String name) {
        super();
        this.name = name;
    }

    public Process() {
        super();
        this.name = "unspecified";
    }

    public void addResourceInfo(TimeSeriesInfo info) {
        infos.put(info.getName(), info);
    }

    public List<Info> getInfos() {
        return new ArrayList<>(infos.values());
    }

    public Info getInfo(String name) {
        if(infos.containsKey(name)) {
            return infos.get(name);
        }
        else {
            throw new IllegalStateException();
        }
    }

//    public String exportBasic() {
//        return String.format("<Process name=\"%s\" uuid=\"%s\">", name, uuid);
//    }
//
//    public String export() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(String.format("<Process name=\"%s\" uuid=\"%s\">", name, uuid));
//
//        stringBuilder.append(exportInfos());
//        stringBuilder.append(exportVisuals());
//
//        stringBuilder.append("</Process>");
//        return stringBuilder.toString();
//    }
}
