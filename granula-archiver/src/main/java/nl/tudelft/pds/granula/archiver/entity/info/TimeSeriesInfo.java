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

import nl.tudelft.pds.granula.archiver.entity.Identifier;

import java.util.List;

/**
 * Created by wing on 26-2-15.
 */
public class TimeSeriesInfo extends Info {

    TimeSeries timeSeries;
    String metricUnit;


    public TimeSeriesInfo(String name) {
        super(name, Identifier.TimeSeriesInfo);
    }

    public void addInfo(String metricUnit, TimeSeries timeSeries, List<Source> sources) {
        this.metricUnit = metricUnit;
        this.timeSeries = timeSeries;

        for (Source source : sources) {
            addSource(source);
        }
    }

    public void addInfo(String metricUnit, TimeSeries timeSeries, Source source) {
        this.metricUnit = metricUnit;
        this.timeSeries = timeSeries;

        addSource(source);
    }

    public TimeSeries getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public void setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Info name=\"%s\" value=\"%s\" type=\"%s\" uuid=\"%s\">", name, value, type, uuid));

        stringBuilder.append(String.format("<Description>%s</Description>", description));

        stringBuilder.append("<Sources>");
        for (Source source : sources) {
            source.export();
        }
        stringBuilder.append("</Sources>");

        stringBuilder.append(String.format("<MetricUnit>%s</MetricUnit>", metricUnit));
        for (Datapoint datapoint : timeSeries.getDatapoints()) {
            stringBuilder.append(String.format("<Data t=\"%s\" v=\"%f\" />", datapoint.getTimestamp(), datapoint.getValue()));
        }

        stringBuilder.append("</Info>");
        return stringBuilder.toString();
    }
}
