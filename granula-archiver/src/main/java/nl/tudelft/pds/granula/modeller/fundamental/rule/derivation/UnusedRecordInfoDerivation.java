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

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.RecordSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.record.Record;
import nl.tudelft.pds.granula.archiver.record.RecordInfo;

import java.util.ArrayList;
import java.util.List;

public class UnusedRecordInfoDerivation extends DerivationRule {

    public UnusedRecordInfoDerivation(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        for (Record record : operation.getRecords()) {
            String infoName = record.getAttr(RecordInfo.InfoName);
            if(!operation.hasInfo(infoName)) {
                Info info = new Info(infoName);
                List<Source> sources = new ArrayList<>();
                sources.add(new RecordSource(infoName, record));
                info.addInfo(record.getAttr(RecordInfo.InfoValue), sources);
                info.setDescription("The information contained in the sourcce record is never used, to preserve its value, we convert it into a Info.");
                operation.addInfo(info);
//                System.out.println(infoName + " not already being used.");
            }
        }
        return  true;
    }
}
