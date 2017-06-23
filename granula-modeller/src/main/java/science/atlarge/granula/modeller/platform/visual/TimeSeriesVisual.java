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

package science.atlarge.granula.modeller.platform.visual;

import science.atlarge.granula.modeller.entity.Archivable;
import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.info.InfoSource;
import science.atlarge.granula.modeller.platform.info.Source;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 16-3-15.
 */
public class TimeSeriesVisual extends Visual {

    String title;
    Axis xAxis;
    Axis y1Axis;
    Axis y2Axis;

    private TimeSeriesVisual() {
        super("unspecified", BasicType.TimeSeriesVisual);
    }


    public TimeSeriesVisual(String name) {
        super(name, BasicType.TimeSeriesVisual);
        title = "Unspecified Title";
    }

    public String getTitle() {
        return title;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getY1Axis() {
        return y1Axis;
    }

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

        public String getTitle() {
            return title;
        }

        public String getUnit() {
            return unit;
        }

        public String getStartValue() {
            return startValue;
        }

        public String getEndValue() {
            return endValue;
        }

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
