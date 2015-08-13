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

package nl.tudelft.pds.granula.modeller.rule.assembling;

import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordInfo;

import java.util.ArrayList;
import java.util.List;

public class BasicAssembling extends AssemblingRule {

    public BasicAssembling() {
        super();
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        List<Record> candidateRecords = new ArrayList<>();

        for (Record record : operation.getJob().getJobRecord().getRecords()) {
            candidateRecords.add(record);
        }

        for (Record candidateRecord : candidateRecords) {
            boolean actorMatched = candidateRecord.getAttr(RecordInfo.ActorType).equals(operation.getActor().getType());
            boolean actorIdMatched = candidateRecord.getAttr(RecordInfo.ActorId).equals(operation.getActor().getId());
            boolean missionMatched = candidateRecord.getAttr(RecordInfo.MissionType).equals(operation.getMission().getType());
            boolean missionIdMatched = candidateRecord.getAttr(RecordInfo.MissionId).equals(operation.getMission().getId());

            if (actorMatched && actorIdMatched && missionMatched && missionIdMatched) {
                operation.addRecord(candidateRecord);
            }
        }
        return true;
    }
}
