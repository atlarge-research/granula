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

import science.atlarge.granula.modeller.entity.BasicType;

import java.util.List;

/**
 * Created by wing on 26-2-15.
 */
public class TimeSeriesInfo extends Info {

    TimeSeries timeSeries;
    String metricUnit;

    public TimeSeriesInfo() {
        this("unspecified");
    }

    public TimeSeriesInfo(String name) {
        super(name, BasicType.TimeSeriesInfo);
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


}
