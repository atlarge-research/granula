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

package nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.time;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;

import java.util.ArrayList;
import java.util.List;

public class FilialStartTimeDerivation extends DerivationRule {

    public FilialStartTimeDerivation(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        long startTime = Long.MAX_VALUE;
        List<Info> usedInfos = new ArrayList<>();

        for (Operation child : operation.getChildren()) {
            Info startTimeInfo = child.getInfo("StartTime");
            startTime = Math.min(startTime, Long.parseLong(startTimeInfo.getValue()));
            usedInfos.add(startTimeInfo);
        }
        Info info = new Info("StartTime");
        List<Source> sources = new ArrayList<>();
        sources.add(new InfoSource("FilialEndTimes", usedInfos));
        info.setDescription("The [StartTime] of an (abstract) operation is derived from the smallest value of [FilialStartTimes], which are [StartTime]s of all child operations.");
        info.addInfo(String.valueOf(startTime), sources);
        operation.addInfo(info);
        return  true;
    }
}
