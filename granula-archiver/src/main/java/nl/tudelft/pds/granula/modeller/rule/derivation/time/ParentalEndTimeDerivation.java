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

package nl.tudelft.pds.granula.modeller.rule.derivation.time;

import nl.tudelft.pds.granula.archiver.entity.info.BasicInfo;
import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;

import java.util.ArrayList;
import java.util.List;

public class ParentalEndTimeDerivation extends DerivationRule {

    public ParentalEndTimeDerivation(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        Operation parent = operation.getParent();
        Info sourceInfo = parent.getInfo("EndTime");
        long endTime = Long.parseLong(sourceInfo.getValue());

        BasicInfo info = new BasicInfo("EndTime");
        List<Source> sources = new ArrayList<>();
        sources.add(new InfoSource("ParentalEndTime", sourceInfo));
        info.setDescription("The [EndTime] of an (abstract) operation is derived from the largest value of [FilialEndTimes], which are [EndTime]s of all child operations.");
        info.addInfo(String.valueOf(endTime), sources);
        operation.addInfo(info);
        return  true;
    }
}
