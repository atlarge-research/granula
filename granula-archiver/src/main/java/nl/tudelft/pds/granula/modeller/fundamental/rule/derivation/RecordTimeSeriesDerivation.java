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

package nl.tudelft.pds.granula.modeller.fundamental.rule.derivation;

import nl.tudelft.pds.granula.archiver.entity.info.RecordSource;
import nl.tudelft.pds.granula.archiver.entity.info.TimeSeries;
import nl.tudelft.pds.granula.archiver.entity.info.TimeSeriesInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;

/**
 * Created by wing on 18-6-15.
 */
public class RecordTimeSeriesDerivation extends DerivationRule {

    String recordName;
    String infoName;

    public RecordTimeSeriesDerivation(int level, String recordName) {
        super(level);
        this.recordName = recordName;
        this.infoName = recordName;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        String x = operation.getRecord(recordName).getAttr("InfoValue");

        TimeSeriesInfo timeSeriesInfo = new TimeSeriesInfo(infoName);


        TimeSeries timeSeries = new TimeSeries();
        for (String timeValueStr : x.split("#")) {
                long time = Long.parseLong(timeValueStr.split("-")[0]);
                long value = Long.parseLong(timeValueStr.split("-")[1]);
                timeSeries.addDatapoint(time, value);
        };

        timeSeriesInfo.addInfo("", timeSeries, new RecordSource(recordName, operation.getRecord(recordName)));

        operation.addInfo(timeSeriesInfo);
        return  true;
    }
}
