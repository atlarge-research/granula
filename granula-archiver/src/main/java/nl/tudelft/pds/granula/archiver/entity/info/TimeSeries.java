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

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeSeries {
    List<Datapoint> datapoints;



    public TimeSeries() {
        this.datapoints = new ArrayList<>();
    }

    public TimeSeries filterDatapoints(long starTtime, long endTime) {
        List<Datapoint> filteredDps = new ArrayList<>();

        Datapoint firstBefore = null;
        Datapoint firstAfter = null;

        for (Datapoint datapoint : datapoints) {
            if(datapoint.getTimestamp() <= starTtime) {
                if(firstBefore != null) {
                    if(datapoint.getTimestamp() > firstBefore.getTimestamp()) {
                        firstBefore = datapoint;
                    }
                } else {
                    firstBefore = datapoint;
                }
            }
            if(datapoint.getTimestamp() >= endTime) {
                if(firstAfter != null) {
                    if(datapoint.getTimestamp() < firstAfter.getTimestamp()) {
                        firstAfter = datapoint;
                    }
                } else {
                    firstAfter = datapoint;
                }
            }
        }

        if(firstBefore != null) {
            filteredDps.add(firstBefore);
        }

        if(firstAfter != null) {
            filteredDps.add(firstAfter);
        }

        for (Datapoint datapoint : datapoints) {
            if(datapoint.getTimestamp() > starTtime && datapoint.getTimestamp() < endTime) {
                filteredDps.add(datapoint);
            }
        }

        TimeSeries filteredTimeSeries = new TimeSeries();
        for (Datapoint filteredDp : filteredDps) {
            filteredTimeSeries.addDatapoint(filteredDp.timestamp, filteredDp.value);
        }
        return filteredTimeSeries;
    }


    public List<Datapoint> getDatapoints() {
        return datapoints;
    }

    @XmlElement(name="Data")
    public String getData() {
        StringBuilder str = new StringBuilder();
        for (Datapoint datapoint : datapoints) {
            str.append(datapoint.getTimestamp() + "@" + datapoint .getValue() + "#");
        }
        return str.toString();
    }

    public void addDatapoint(long timestamp, double value) {
        Datapoint datapoint = new Datapoint();
        datapoint.setTimestamp(timestamp);
        datapoint.setValue(value);
        datapoints.add(datapoint);
    }

    public double maxValue() {
        double maxValue = Double.MIN_VALUE;
        for (Datapoint datapoint : datapoints) {
            if(datapoint.getValue() > maxValue) {
                maxValue = datapoint.getValue();
            }
        }
        return maxValue;
    }

    public double minValue() {
        double minValue = Double.MAX_VALUE;
        for (Datapoint datapoint : datapoints) {
            if(datapoint.getValue() < minValue) {
                minValue = datapoint.getValue();
            }
        }
        return minValue;
    }

    public boolean empty() {
        return datapoints.size() == 0;
    }

    public int size() {
        return datapoints.size();
    }

    public void sort() {
        Collections.sort(datapoints, new Comparator<Datapoint>() {
            @Override
            public int compare(Datapoint datapoint1, Datapoint datapoint2) {
                if (datapoint1.getTimestamp() > datapoint2.getTimestamp()) {
                    return 1;
                } else if (datapoint2.getTimestamp() > datapoint1.getTimestamp()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
}
