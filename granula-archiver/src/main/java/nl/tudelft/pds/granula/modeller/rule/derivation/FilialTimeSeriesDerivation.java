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

package nl.tudelft.pds.granula.modeller.rule.derivation;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.info.*;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 13-3-15.
 */
public class FilialTimeSeriesDerivation extends DerivationRule {

    String outputInfoName;
    String missionType;
    String valueInfoName;

    public FilialTimeSeriesDerivation(int level, String outputInfoName, String missionType, String valueInfoName) {
        super(level);
        this.outputInfoName = outputInfoName;
        this.missionType = missionType;
        this.valueInfoName = valueInfoName;
    }

    public boolean execute() {

        Operation operation = (Operation) entity;

        List<Info> startTimeInfos = new ArrayList<>();
        List<Info> endTimeInfos = new ArrayList<>();
        List<Info> valueInfos = new ArrayList<>();

        List<Source> sources = new ArrayList<>();
        sources.add(new InfoSource("StartTimes", startTimeInfos));
        sources.add(new InfoSource("EndTimes", endTimeInfos));
        sources.add(new InfoSource(valueInfoName, valueInfos));

        for (Operation suboperation : operation.getChildren()) {
            if(suboperation.getMission().getType().equals(missionType)) {
                startTimeInfos.add(suboperation.getInfo(Identifier.StartTime));
                endTimeInfos.add(suboperation.getInfo(Identifier.EndTime));
                valueInfos.add(suboperation.getInfo(valueInfoName));
            }
        }

        TimeSeries timeSeries = new TimeSeries();
        for (int i = 0; i < startTimeInfos.size(); i++) {
            long time = Long.parseLong(startTimeInfos.get(i).getValue()) + (Long.parseLong(endTimeInfos.get(i).getValue()) - Long.parseLong(startTimeInfos.get(i).getValue())) / 2 ;
            timeSeries.addDatapoint(time, Double.parseDouble(valueInfos.get(i).getValue()));
        }

        TimeSeriesInfo timeSeriesInfo = new TimeSeriesInfo(outputInfoName);
        timeSeriesInfo.addInfo("", timeSeries, sources);

        operation.addInfo(timeSeriesInfo);

        return true;
    }
}
