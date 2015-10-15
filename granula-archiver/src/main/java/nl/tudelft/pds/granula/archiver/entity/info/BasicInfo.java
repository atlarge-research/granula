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

import nl.tudelft.pds.granula.archiver.entity.Attribute;
import nl.tudelft.pds.granula.archiver.entity.Identifier;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="Info")
public class BasicInfo extends Info {

    private BasicInfo() {
        this("unspecified");
    }

    public BasicInfo(String name) {
        this(name, Identifier.BasicInfo);
    }

    public BasicInfo(String name, String type) {
        super(name, type);
        this.description = "It is not certain how this information is derived.";
    }

    public void addInfo(String value, List<Source> sources) {
        this.value = value;
        for (Source source : sources) {
            addSource(source);
        }
    }
}
