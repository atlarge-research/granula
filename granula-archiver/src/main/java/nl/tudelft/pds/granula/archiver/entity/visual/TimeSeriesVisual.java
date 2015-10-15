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

import nl.tudelft.pds.granula.archiver.entity.Archivable;
import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.info.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 16-3-15.
 */
@XmlRootElement(name="Visual")
@XmlSeeAlso({Source.class})
public class TimeSeriesVisual extends Visual {

    String title;
    Axis xAxis;
    Axis y1Axis;
    Axis y2Axis;

    private TimeSeriesVisual() {
        super("unspecified", Identifier.TimeSeriesVisual);
    }


    public TimeSeriesVisual(String name) {
        super(name, Identifier.TimeSeriesVisual);
        title = "Unspecified Title";
    }

    @XmlElement(name="Title")
    public String getTitle() {
        return title;
    }

    @XmlElement(name="Axis")
    public Axis getXAxis() {
        return xAxis;
    }

    @XmlElement(name="Axis")
    public Axis getY1Axis() {
        return y1Axis;
    }

    @XmlElement(name="Axis")
    public Axis getY2Axis() {
        return y2Axis;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setY1Axis(String title, String unit) {
        y1Axis = new Axis("y1", title, unit);
    }

    public void setY2Axis(String title, String unit) {
        y2Axis = new Axis("y2", title, unit);
    }

    public void setXAxis(String title, String unit, String startValue, String endValue) {
        xAxis = new Axis("x", title, unit, startValue, endValue);
    }

    public void setY1Axis(String title, String unit, String startValue, String endValue) {
        y1Axis = new Axis("y1", title, unit, startValue, endValue);
    }

    public void setY2Axis(String title, String unit, String startTime, String endTime) {
        y2Axis = new Axis("y2", title, unit, startTime, endTime);
    }

    public void addTimeSeriesInfoToY1(TimeSeriesInfo timeSeriesInfo) {
        if(y1Axis == null) {
            System.out.println("Y1 Axis not initialized.");
            throw new IllegalStateException();
        }
        this.y1Axis.addTimeSeriesInfo(timeSeriesInfo);
    }

    public void addTimeSeriesInfoToY2(TimeSeriesInfo timeSeriesInfo) {
        if(y2Axis == null) {
            System.out.println("Y1 Axis not initialized.");
            throw new IllegalStateException();
        }
        this.y2Axis.addTimeSeriesInfo(timeSeriesInfo);
    }

    @XmlRootElement(name="Axis")
    private static class Axis extends Archivable {

        String title;
        String unit;
        String startValue;
        String endValue;
        boolean isDynamic;
        List<Source> tsSources;

        private Axis() {
            this.type = "unspecified";
            this.title = "unspecified";
            this.unit = "unspecified";
            isDynamic = true;
            tsSources = new ArrayList<>();
        }

        public Axis(String type, String title, String unit) {
            this.type = type;
            this.title = title;
            this.unit = unit;
            isDynamic = true;
            tsSources = new ArrayList<>();
        }

        public Axis(String type, String title, String unit, String startValue, String endValue) {
            this.type = type;
            this.title = title;
            this.unit = unit;
            this.startValue = startValue;
            this.endValue = endValue;
            isDynamic = false;
            tsSources = new ArrayList<>();
        }

        @XmlElement(name="Title")
        public String getTitle() {
            return title;
        }

        @XmlElement(name="Unit")
        public String getUnit() {
            return unit;
        }

        @XmlElement(name="StartValue")
        public String getStartValue() {
            return startValue;
        }

        @XmlElement(name="EndValue")
        public String getEndValue() {
            return endValue;
        }

        @XmlElementWrapper(name="TimeSeriesSources")
        @XmlElementRef
        public List<Source> getTsSources() {
            return tsSources;
        }

        public void addTimeSeriesInfo(TimeSeriesInfo timeSeriesInfo) {
            List<Info> sourceInfos = new ArrayList<>();
            sourceInfos.add(timeSeriesInfo);
            this.tsSources.add(new InfoSource("TimeSeries Info", timeSeriesInfo));
        }
    }
}
