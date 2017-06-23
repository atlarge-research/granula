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

package science.atlarge.granula.modeller.rule.linking;

import science.atlarge.granula.modeller.platform.operation.Operation;

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

        for (Operation candidateOperation : operation.getPlatform().getOperations()) {

            boolean actorMatched = candidateOperation.getActor().getType().equals(parentActorType);
            boolean missionMatched = candidateOperation.getMission().getType().equals(parentMissionType);

            if (actorMatched && missionMatched) {
                matchedParents.add(candidateOperation);
            }
        }

        if(matchedParents.size() != 1) {
            throw new IllegalStateException(String.format("Operation %s cannot find parent Operation [%s, %s]",
                    operation.getName(), parentActorType, parentMissionType));
        }

        Operation parent = matchedParents.get(0);
        operation.setParent(parent);
        parent.addChild(operation);
        return  true;
    }
}