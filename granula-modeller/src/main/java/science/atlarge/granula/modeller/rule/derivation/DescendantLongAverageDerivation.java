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

import science.atlarge.granula.modeller.platform.info.BasicInfo;
import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.info.InfoSource;
import science.atlarge.granula.modeller.platform.info.Source;
import science.atlarge.granula.modeller.platform.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class DescendantLongAverageDerivation extends DerivationRule {

    String infoName;
    String aggInfoName;
    String missionType;
    int searchLevel;

    public DescendantLongAverageDerivation(int level, String missionType, int searchLevel, String infoName, String aggInfoName) {
        super(level);
        this.missionType = missionType;
        this.infoName = infoName;
        this.aggInfoName = aggInfoName;
        this.searchLevel = searchLevel;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        long total = 0;
        long count = 0;
        List<Source> sources = new ArrayList<>();
        List<Info> usedInfos = new ArrayList<>();
        for (Operation suboperation : operation.findDescendantOperations(missionType, searchLevel)) {
                Info info = suboperation.getInfo(infoName);
                total += Long.parseLong(info.getValue());;
                count++;
                usedInfos.add(info);
        }
        sources.add(new InfoSource(infoName, usedInfos));
        BasicInfo aggInfo = new BasicInfo(aggInfoName);

        long value = (count > 0) ? total / count : 0;
        aggInfo.setDescription(String.format("[%s] is averaged from [%s]s of all children operation with mission type %s. ", aggInfoName, infoName, missionType));
        aggInfo.addInfo(String.valueOf(value), sources);
        operation.addInfo(aggInfo);

        return true;
    }
}
