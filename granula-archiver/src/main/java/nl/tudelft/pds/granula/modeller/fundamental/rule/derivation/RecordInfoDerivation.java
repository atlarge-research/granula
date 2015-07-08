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

public class RecordInfoDerivation extends DerivationRule {

    String infoName;
    String description;
    String recordInfoName;

    public RecordInfoDerivation(int level, String infoName) {
        super(level);
        this.infoName = infoName;
        this.recordInfoName = infoName;
    }

    public RecordInfoDerivation(int level, String recordInfoName, String infoName) {
        super(level);
        this.infoName = infoName;
        this.recordInfoName = recordInfoName;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        Record record = operation.getRecord(recordInfoName);
        Info info = new Info(infoName);
        List<Source> sources = new ArrayList<>();
        sources.add(new RecordSource(record.getAttr(RecordInfo.InfoName), record));
        info.addInfo(record.getAttr(RecordInfo.InfoValue), sources);
        info.setDescription("The information is extracted directed from its source record.");
        if(description != null) {
            info.setDescription(description);
        }
        operation.addInfo(info);
        return  true;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}