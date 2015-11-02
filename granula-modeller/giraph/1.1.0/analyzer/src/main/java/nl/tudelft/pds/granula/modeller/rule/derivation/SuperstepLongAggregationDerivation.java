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

import nl.tudelft.pds.granula.archiver.entity.info.BasicInfo;
import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;
import java.util.List;

public class SuperstepLongAggregationDerivation extends DerivationRule {

    String infoName;
    String aggInfoName;

    public SuperstepLongAggregationDerivation(int level, String infoName, String aggInfoName) {
        super(level);
        this.infoName = infoName;
        this.aggInfoName = aggInfoName;
    }

    public SuperstepLongAggregationDerivation(int level,  String infoName) {
        super(level);
        this.infoName = infoName;
        this.aggInfoName = infoName;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        long total = 0;
        List<Source> sources = new ArrayList<>();
        List<Info> usedInfos = new ArrayList<>();

        for (Operation bspTaskOp : operation.getParent().getParent().findSuboperations(GiraphType.WorkerTask)) {
            for (Operation superstepOp : bspTaskOp.findSuboperations(GiraphType.Superstep)) {
                if (operation.getMission().getId().equals(superstepOp.getMission().getId())) {
                    Info info = superstepOp.getInfo(infoName);
                    total += Long.parseLong(info.getValue());;
                    usedInfos.add(info);
                }
            }
        }

        sources.add(new InfoSource(infoName, usedInfos));
        BasicInfo aggInfo = new BasicInfo(aggInfoName);
        aggInfo.setDescription(String.format("[%s] is aggregated from [%s]s of all superstep operation.", aggInfoName, infoName));
        aggInfo.addInfo(String.valueOf(total), sources);
        operation.addInfo(aggInfo);

        return true;
    }
}
