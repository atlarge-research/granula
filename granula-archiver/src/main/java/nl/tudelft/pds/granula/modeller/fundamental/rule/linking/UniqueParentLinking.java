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

package nl.tudelft.pds.granula.modeller.fundamental.rule.linking;

import nl.tudelft.pds.granula.archiver.entity.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class UniqueParentLinking extends LinkingRule {

    String parentActorType;
    String parentMissionType;

    public UniqueParentLinking(String parentActorType, String parentMissionType) {
        super();
        this.parentActorType = parentActorType;
        this.parentMissionType = parentMissionType;
    }

    @Override
    public boolean execute() {

        Operation operation = (Operation) entity;

        List<Operation> matchedParents = new ArrayList<>();

        for (Operation candidateOperation : operation.getJob().getMemberOperations()) {

            boolean actorMatched = candidateOperation.getActor().getType().equals(parentActorType);
            boolean missionMatched = candidateOperation.getMission().getType().equals(parentMissionType);

            if (actorMatched && missionMatched) {
                matchedParents.add(candidateOperation);
            }
        }

        Operation parent = matchedParents.get(0);
        operation.setParent(parent);
        parent.addChild(operation);
        return  true;
    }
}