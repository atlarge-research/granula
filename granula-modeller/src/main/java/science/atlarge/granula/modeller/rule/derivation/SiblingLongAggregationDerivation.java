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

import science.atlarge.granula.modeller.platform.info.InfoSource;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.platform.info.BasicInfo;
import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.info.Source;

import java.util.ArrayList;
import java.util.List;

public class SiblingLongAggregationDerivation extends DerivationRule {

    String infoName;
    String aggInfoName;
    String missionType;
    String missionId;

    public SiblingLongAggregationDerivation(int level, String missionType, String missionId, String infoName, String aggInfoName) {
        super(level);
        this.missionType = missionType;
        this.infoName = infoName;
        this.aggInfoName = aggInfoName;
        this.missionId = missionId;
    }

    public SiblingLongAggregationDerivation(int level, String missionType, String missionId, String infoName) {
        super(level);
        this.missionType = missionType;
        this.infoName = infoName;
        this.aggInfoName = infoName;
        this.missionId = missionId;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        long total = 0;
        List<Source> sources = new ArrayList<>();
        List<Info> usedInfos = new ArrayList<>();
        for (Operation sibOperation : operation.findSiblingOperations(missionType)) {

            boolean selected = false;
            if(missionId.equals(BasicType.Equal)) {
                if(operation.getMission().getId().equals(sibOperation.getMission().getId())) {
                    selected = true;
                }
            } else if (missionId.equals(sibOperation.getMission().getId())) {
                selected = true;
            }
            if(selected) {
                Info info = sibOperation.getInfo(infoName);
                total += Long.parseLong(info.getValue());;
                usedInfos.add(info);
            }
        }
        sources.add(new InfoSource(infoName, usedInfos));
        BasicInfo aggInfo = new BasicInfo(aggInfoName);
        aggInfo.setDescription(String.format("[%s] is aggregated from [%s]s of all children operation with mission type %s. ", aggInfoName, infoName, missionType));
        aggInfo.addInfo(String.valueOf(total), sources);
        operation.addInfo(aggInfo);

        return true;
    }
}
