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

package science.atlarge.granula.modeller.rule.derivation;

import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.platform.info.LogSource;
import science.atlarge.granula.modeller.platform.info.TimeSeries;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

/**
 * Created by wing on 18-6-15.
 */
public class LogTimeSeriesDerivation extends DerivationRule {

    String logName;
    String infoName;

    public LogTimeSeriesDerivation(int level, String logName) {
        super(level);
        this.logName = logName;
        this.infoName = logName;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        String x = operation.getLog(logName).getAttr("InfoValue");

        TimeSeriesInfo timeSeriesInfo = new TimeSeriesInfo(infoName);


        TimeSeries timeSeries = new TimeSeries();
        for (String timeValueStr : x.split("#")) {
                long time = Long.parseLong(timeValueStr.split("-")[0]);
                long value = Long.parseLong(timeValueStr.split("-")[1]);
                timeSeries.addDatapoint(time, value);
        };

        timeSeriesInfo.addInfo("", timeSeries, new LogSource(logName, operation.getLog(logName)));

        operation.addInfo(timeSeriesInfo);
        return  true;
    }
}
