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

package science.atlarge.granula.modeller.rule.visual;

import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.platform.visual.TimeSeriesVisual;
import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 9-4-15.
 */
public class EnvTimeSeriesVisualization extends TimeSeriesVisualization {



    TimeSeriesVisual timeSeriesVisual;
    List<String>  y1InfoNames = new ArrayList<>();
    List<String>  y2InfoNames = new ArrayList<>();
    String y1Title;
    String y1Unit;
    String y2Title;
    String y2Unit;

    public EnvTimeSeriesVisualization(int level, String visalName, String visualTitle, String y1Title, String y1Unit, String y2Title, String y2Unit) {
        super(level, visalName, visualTitle, y1Title, y2Title);

        timeSeriesVisual = new TimeSeriesVisual(visalName);
        timeSeriesVisual.setTitle(visualTitle);;
        this.y1Title = y1Title;
        this.y1Unit = y1Unit;
        this.y2Title = y2Title;
        this.y2Unit = y2Unit;
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

        String computeNodeName = operation.getInfo(BasicType.ComputeNode).getValue();


        List<TimeSeriesInfo>  y1Infos = new ArrayList<>();
        for (String infoName : y1InfoNames) {
            try {
                TimeSeriesInfo tsInfo = (TimeSeriesInfo) operation.getJob().getEnvironment().getNode(computeNodeName).getInfo(infoName);
                y1Infos.add(tsInfo);
            } catch (Exception e) {
                System.out.println(String.format("Cannot find %s at %s.", infoName, computeNodeName));
            }
        }
            double y1Min = 0; //getMin(y1Infos);
            double y1Max = getMax(y1Infos);
            double y1Padding = (y1Max - y1Min) / 10.0;
            timeSeriesVisual.setY1Axis(y1Title, y1Unit, String.valueOf(y1Min), String.valueOf(y1Max + y1Padding));
            if((y1Max + y1Padding) == (y1Min - y1Padding)) {
                System.out.println(y1Min + " " + y1Max + " " + String.valueOf(y1Min) + " " + String.valueOf(y1Max + y1Padding) + " " + y1Padding + " " + y1Infos.size());
            }

            for (TimeSeriesInfo y1Info : y1Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY1(y1Info);
            }

        List<TimeSeriesInfo>  y2Infos = new ArrayList<>();
        for (String infoName : y2InfoNames) {
            try {
                TimeSeriesInfo tsInfo = (TimeSeriesInfo) operation.getJob().getEnvironment().getNode(computeNodeName).getInfo(infoName);
                y2Infos.add(tsInfo);
            } catch (Exception e) {
                System.out.println(String.format("Cannot find %s at %s.", infoName, computeNodeName));
            }
        }
        if( y2Infos.size() > 0) {
            double y2Min = 0; //getMin(y2Infos);
            double y2Max = getMax(y2Infos);
            double y2Padding = (y2Max - y2Min) / 10.0;
            timeSeriesVisual.setY2Axis(y2Title, y2Unit, String.valueOf(y2Min), String.valueOf(y2Max + y2Padding));
            for (TimeSeriesInfo y2Info : y2Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY2(y2Info);
            }
        }

        operation.addVisual(timeSeriesVisual);

        return true;
    }

}
