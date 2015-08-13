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
@XmlSeeAlso({BasicInfo.class, SummaryInfo.class, TimeSeriesInfo.class})
public abstract class Info extends Attribute {
    String value;
    String description;

    private Info() {
        this("unspecified");
    }

    public Info(String name) {
        this(name, Identifier.BasicInfo);
    }

    public Info(String name, String type) {
        super(name, type);
        this.description = "It is not certain how this information is derived.";
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlElementWrapper(name="Sources")
    @XmlElementRef
    public List<Source> getSources() {
        return sources;
    }

    public void addSource(Source source) {
        sources.add(source);
    }

    @XmlElement(name="Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
