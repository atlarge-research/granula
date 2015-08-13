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

package nl.tudelft.pds.granula.modeller.rule.visual;

import nl.tudelft.pds.granula.archiver.entity.info.TimeSeriesInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.entity.visual.TimeSeriesVisual;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 9-4-15.
 */
public class TimeSeriesVisualization extends VisualizationRule {

    TimeSeriesVisual timeSeriesVisual;
    List<String>  y1InfoNames = new ArrayList<>();
    List<String>  y2InfoNames = new ArrayList<>();
    String y1Title;
    String y2Title;

    public TimeSeriesVisualization(int level, String visalName, String visualTitle, String y1Title, String y2Title) {
        super(level);

        timeSeriesVisual = new TimeSeriesVisual(visalName);
        timeSeriesVisual.setTitle(visualTitle);;
        this.y1Title = y1Title;
        this.y2Title = y2Title;
    }

    public void addY1Info(String infoName) {
        y1InfoNames.add(infoName);
    }

    public void addY2Info(String infoName) {
        y2InfoNames.add(infoName);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        long startTime = Long.parseLong(operation.getInfo("StartTime").getValue());
        long endTime = Long.parseLong(operation.getInfo("EndTime").getValue());
        timeSeriesVisual.setXAxis("ExecutionTime", "s", String.valueOf(startTime), String.valueOf(endTime));


        List<TimeSeriesInfo>  y1Infos = new ArrayList<>();
        for (String infoName : y1InfoNames) {
            TimeSeriesInfo tsInfo = (TimeSeriesInfo) operation.getInfo(infoName);
            y1Infos.add(tsInfo);
        }
            double y1Min = getMin(y1Infos);
            double y1Max = getMax(y1Infos);
            double y1Padding = (y1Max - y1Min) / 10.0;
            timeSeriesVisual.setY1Axis(y1Title, "", String.valueOf(y1Min - y1Padding), String.valueOf(y1Max + y1Padding));
            if((y1Max + y1Padding) == (y1Min - y1Padding)) {
                System.out.println(y1Min + " " + y1Max + " " + String.valueOf(y1Min - y1Padding) + " " + String.valueOf(y1Max + y1Padding) + " " + y1Padding + " " + y1Infos.size());
            }

            for (TimeSeriesInfo y1Info : y1Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY1(y1Info);
            }

        List<TimeSeriesInfo>  y2Infos = new ArrayList<>();
        for (String infoName : y2InfoNames) {
            TimeSeriesInfo tsInfo = (TimeSeriesInfo) operation.getInfo(infoName);
            y2Infos.add(tsInfo);
        }
        if( y2Infos.size() > 0) {
            double y2Min = getMin(y2Infos);
            double y2Max = getMax(y2Infos);
            double y2Padding = (y2Max - y2Min) / 10.0;
            timeSeriesVisual.setY2Axis(y2Title, "", String.valueOf(y2Min - y2Padding), String.valueOf(y2Max + y2Padding));
            for (TimeSeriesInfo y2Info : y2Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY2(y2Info);
            }
        }




        operation.addVisual(timeSeriesVisual);

        return true;
    }

    public double getMax(List<TimeSeriesInfo> timeSeriesInfos) {
        List<Double> values = new ArrayList<>();
        double finalMaxValue = Double.MIN_VALUE;
        double finalMinValue = Double.MAX_VALUE;

        for (TimeSeriesInfo timeSeriesInfo : timeSeriesInfos) {
            if(!timeSeriesInfo.getTimeSeries().empty()) {
                values.add(timeSeriesInfo.getTimeSeries().maxValue());
            }
        }

        if(values.size() >= 1) {

            for (Double value : values) {
                finalMaxValue = Math.max(value, finalMaxValue);
                finalMinValue = Math.min(value, finalMinValue);
            }
            return (finalMaxValue - finalMinValue) == 0 ? values.get(0) + 5 : finalMaxValue;
        } else {
            return 10;
        }
    }

    public double getMin(List<TimeSeriesInfo> timeSeriesInfos) {
        List<Double> values = new ArrayList<>();
        double finalMinValue = Double.MAX_VALUE;
        double finalMaxValue = Double.MIN_VALUE;

        for (TimeSeriesInfo timeSeriesInfo : timeSeriesInfos) {
            if(!timeSeriesInfo.getTimeSeries().empty()) {
                values.add(timeSeriesInfo.getTimeSeries().minValue());
            }
        }

        if(values.size() >= 1) {

            for (Double value : values) {
                finalMaxValue = Math.max(value, finalMaxValue);
                finalMinValue = Math.min(value, finalMinValue);
            }
            return (finalMaxValue - finalMinValue) == 0 ? values.get(0) - 5 : finalMinValue;
        } else {
            return -10;
        }
    }
}
