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

import nl.tudelft.pds.granula.ArchiverConfiguration;
import nl.tudelft.pds.granula.archiver.entity.Attribute;
import nl.tudelft.pds.granula.archiver.entity.Identifier;

import java.util.List;

public class Info extends Attribute {
    String value;
    String description;

    public Info(String name) {
        this(name, Identifier.BasicInfo);
    }

    public Info(String name, String type) {
        super(name, type);
        this.description = "It is not certain how this information is derived.";
    }

    public void addInfo(String value, List<Source> sources) {
        this.value = value;
        for (Source source : sources) {
            addSource(source);
        }

    }

    public String exportBasic() {
        return String.format("<Info name=\"%s\" value=\"%s\" type=\"%s\" uuid=\"%s\"/>", name, value, type, uuid);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Info name=\"%s\" value=\"%s\" type=\"%s\" uuid=\"%s\">", name, value, type, uuid));

        if(ArchiverConfiguration.ExportDescription) {
            stringBuilder.append(String.format("<Description>%s</Description>", description));
        } else {
            stringBuilder.append(String.format("<Description>%s</Description>", ""));
        }


        stringBuilder.append("<Sources>");
        for (Source source : sources) {
            stringBuilder.append(source.export());
        }
        stringBuilder.append("</Sources>");
        stringBuilder.append("</Info>");
        return stringBuilder.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void addSource(Source source) {
        sources.add(source);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
