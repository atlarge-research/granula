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
import nl.tudelft.pds.granula.archiver.entity.info.RecordSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordInfo;
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;

import java.util.ArrayList;
import java.util.List;

public class IncompleteEndTimeDerivation extends DerivationRule {

    public IncompleteEndTimeDerivation(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        Record endEventRecord= null;
        long endtime = Long.MIN_VALUE;

        try {
            endEventRecord = operation.getRecord("EndTime");
            endtime = Long.parseLong(endEventRecord.getAttr(RecordInfo.InfoValue));
        } catch (Exception e) {
//            System.out.println("Invalid endtime");
        }

        if(endEventRecord == null) {
            endEventRecord = operation.getRecord("StartTime");
            endtime = Long.parseLong(endEventRecord.getAttr(RecordInfo.InfoValue)) + 1;
        }

        if(endtime == Long.MIN_VALUE) {
            throw new IllegalStateException();
        }

        List<Source> sources = new ArrayList<>();
        sources.add(new RecordSource("EndTime", endEventRecord));

        BasicInfo info = new BasicInfo("EndTime");
        info.setDescription("The [EndTime] of an operation is retrieved from the [EndTime] record in the log.");
        info.addInfo(String.valueOf(endtime), sources);
        operation.addInfo(info);
        return  true;
    }
}
