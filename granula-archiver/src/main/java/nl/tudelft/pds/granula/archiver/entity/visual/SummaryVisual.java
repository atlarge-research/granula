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

package nl.tudelft.pds.granula.archiver.entity.visual;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.info.Source;

/**
 * Created by wing on 16-3-15.
 */
public class SummaryVisual extends Visual {

    Source summarySource;

    public SummaryVisual(String name) {
        super(name, Identifier.SummaryVisual);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Visual type=\"%s\" name=\"%s\" uuid=\"%s\">", type, name, uuid));

        stringBuilder.append("<SummarySource>");
        stringBuilder.append(summarySource.export());
        stringBuilder.append("</SummarySource>");

        stringBuilder.append("<Sources>");
        for (Source source : sources) {
            stringBuilder.append(source.export());
        }
        stringBuilder.append("</Sources>");

        stringBuilder.append("</Visual>");
        return stringBuilder.toString();
    }

    public String exportBasic() {
        return String.format("<Visual type=\"%s\" name=\"%s\" uuid=\"%s\">", type, name, uuid);
    }

    public void setSummarySource(Source summarySource) {
        sources.add(summarySource);
        this.summarySource = summarySource;
    }
}
