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

package nl.tudelft.pds.granula.archiver.entity.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 26-2-15.
 */
public class InfoSource extends Source {
    List<String> infoUuids;

    public InfoSource(String name, List<Info> infos) {
        this.type = "InfoSource";
        this.name = name;
        infoUuids = new ArrayList<>();
        for (Info info : infos) {
            infoUuids.add(info.getUuid());
        }
    }

    public InfoSource(String name, Info info) {
        this.type = "InfoSource";
        this.name = name;
        infoUuids = new ArrayList<>();
        infoUuids.add(info.getUuid());
    }

    public List<String> getInfoUuids() {
        return infoUuids;
    }

    @Override
    public String export() {
        return exportBasic();
    }

    @Override
    public String exportBasic() {

        String uuidsText = "";
        for (String infoUuid : infoUuids) {
            uuidsText += (uuidsText.length() == 0) ? infoUuid : ";" + infoUuid;
        }

        return String.format("<Source name=\"%s\" type=\"%s\" infoUuid=\"%s\" />", name, type, uuidsText);
    }
}
