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

package science.atlarge.granula.modeller.platform.info;

import science.atlarge.granula.modeller.entity.Attribute;
import science.atlarge.granula.modeller.entity.BasicType;

import javax.xml.bind.annotation.*;
import java.util.List;

public abstract class Info extends Attribute {
    String value;
    private String description;


    private Info() {
        this("unspecified");
    }

    public Info(String name) {
        this(name, BasicType.BasicInfo);
    }

    public Info(String name, String type) {
        super(name, type);
        this.description = "";
    }

    @XmlAttribute
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
