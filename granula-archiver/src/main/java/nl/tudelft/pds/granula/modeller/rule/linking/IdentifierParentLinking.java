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

package nl.tudelft.pds.granula.modeller.rule.linking;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class IdentifierParentLinking extends LinkingRule {

    String parentActorType;
    String parentActorId;
    String parentMissionType;
    String parentMissionId;

    public IdentifierParentLinking(String parentActorType, String parentActorId,
                                   String parentMissionType, String parentMissionId) {
        super();
        this.parentActorType = parentActorType;
        this.parentActorId = parentActorId;
        this.parentMissionType = parentMissionType;
        this.parentMissionId = parentMissionId;
    }

    @Override
    public boolean execute() {

        Operation operation = (Operation) entity;

        List<Operation> matchedParents = new ArrayList<>();

        for (Operation candidateOperation : operation.getJob().getMemberOperations()) {

            boolean actorMatched = candidateOperation.getActor().getType().equals(parentActorType);
            boolean missionMatched = candidateOperation.getMission().getType().equals(parentMissionType);

            boolean actorIdMatched = false;
            if(parentActorId.equals(Identifier.Unique)) {
                actorIdMatched = true;
            } else if(parentActorId.equals(Identifier.Equal)) {
                actorIdMatched = candidateOperation.getActor().getId().equals(operation.getActor().getId());
            } else if(parentActorId.equals(Identifier.Any)) {
                actorIdMatched = true;
            } else if(parentActorId.equals(candidateOperation.getActor().getId())) {
                actorIdMatched = true;
            } else {
                if(operation.hasRecord(parentActorId)) {
                    String matchedParentActorId = String.valueOf(operation.getRecord(parentActorId).getAttr("InfoValue"));
                    actorIdMatched = candidateOperation.getActor().getId().equals(matchedParentActorId);
                }
            }

            boolean missionIdMatched = false;
            if(parentMissionId.equals(Identifier.Unique)) {
                missionIdMatched = true;
            } else if(parentMissionId.equals(Identifier.Equal)) {
                missionIdMatched = candidateOperation.getMission().getId().equals(operation.getMission().getId());
            } else if(parentMissionId.equals(Identifier.Any)) {
                missionIdMatched = true;
            } else if(parentMissionId.equals(candidateOperation.getMission().getId())) {
                missionIdMatched = true;
            } else {
                if(operation.hasRecord(parentMissionId)) {
                    String matchedParentMissionId = String.valueOf(operation.getRecord(parentMissionId).getAttr("InfoValue"));
                    missionIdMatched = candidateOperation.getMission().getId().equals(matchedParentMissionId);
                }
            }

            if (actorMatched && actorIdMatched && missionMatched && missionIdMatched) {
                matchedParents.add(candidateOperation);
            }
        }

        if(matchedParents.size() != 1) {
            throw new IllegalStateException();
        }

        Operation parent = matchedParents.get(0);
        operation.setParent(parent);
        parent.addChild(operation);
        return  true;
    }

    @Override
    public String toString() {
        return "IdentifierParentLinking{" +
                "operation=" + ((Operation) entity).getName() +
                "parentActorType='" + parentActorType + '\'' +
                ", parentActorId='" + parentActorId + '\'' +
                ", parentMissionType='" + parentMissionType + '\'' +
                ", parentMissionId='" + parentMissionId + '\'' +
                '}';
    }
}
