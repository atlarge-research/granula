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

package science.atlarge.granula.modeller.platform;
import science.atlarge.granula.modeller.entity.Description;
import science.atlarge.granula.modeller.entity.Entity;
import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.platform.visual.Visual;
import science.atlarge.granula.modeller.source.log.Log;
import science.atlarge.granula.util.UuidGenerator;
import science.atlarge.granula.util.json.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends Entity {

    @Exclude private Map<String, Actor> actorMap;
    @Exclude private Map<String, Mission> missionMap;

    @Exclude Map<String, Log> logs;
    Map<String, Info> infos;
    Map<String, Visual> visuals;
    List<String> rootIds;
    Map<String, Operation> operations;
    Map<String, Actor> actors;
    Map<String, Mission> missions;
    Map<String, Description> descriptions;


    public Platform() {
        super();

        this.logs = new HashMap<>();
        this.operations = new HashMap<>();
        this.actors = new HashMap<>();
        this.missions = new HashMap<>();
        this.rootIds = new ArrayList<>();
        this.infos = new HashMap<>();
        this.visuals = new HashMap<>();
        this.descriptions = new HashMap<>();

        actorMap = new HashMap<>();
        missionMap = new HashMap<>();
    }


    public void addActor(Actor actor) {
        actors.put(actor.getUuid(), actor);
        actorMap.put(actor.getName(), actor);;
    }

    public void addMission(Mission mission) {
        missions.put(mission.getUuid(), mission);
        missionMap.put(mission.getName(), mission);
    }

    public void addOperation(Operation operation) {
        operations.put(operation.getUuid(), operation);
//        actors.put(operation.getActor().getUuid(), operation.getActor());
//        missions.put(operation.getMission().getUuid(), operation.getMission());
    }

    public void addDescription(Description description) {
        this.descriptions.put(String.valueOf(description.hashCode()), description);
    }

    public void addDescription(String descriptionText) {
        Description description = new Description();
        description.setUuid(UuidGenerator.getTextUUID(descriptionText));
        description.setText(descriptionText);
        this.descriptions.put(description.getUuid(), description);
    }

    public void addLog(Log log) {
        this.logs.put(log.getUuid(), log);
    }

    public void addInfo(Info info) {
        addDescription(info.getDescription());
        info.setDescription(UuidGenerator.getTextUUID(info.getDescription()));
        this.infos.put(info.getUuid(), info);
    }


    public void addVisual(Visual visual) {
        this.visuals.put(visual.getUuid(), visual);
    }

    public void addRoot(String operationId) {
        this.rootIds.add(operationId);
    }


    public List<Log> getLogs() {
        return new ArrayList(logs.values());
    }

    public List<Operation> getOperations() {
        return new ArrayList(operations.values());
    }

    public Actor findActor(String name) {
        return actorMap.get(name);
    }

    public Mission findMission(String name) {
        return missionMap.get(name);
    }

    public Map<String, Mission> getMissions() {
        return missions;
    }

    public List<Description> getDescriptions() {
        return new ArrayList<>(descriptions.values());
    }

    public Operation findOperation(String actorType, String missionType) {
        Operation operation = null;
        for (Operation memberOperation : operations.values()) {
            if(memberOperation.hasType(actorType, missionType)) {
                if(operation == null) {
                    operation = memberOperation;
                } else {
                    throw new IllegalStateException(actorType + " " + missionType);
                }
            }
        }
        return operation;
    }

    public List<Operation> findOperations(String actorType, String missionType) {
        List<Operation> foundOperations = new ArrayList<>();
        for (Operation memberOperation : operations.values()) {
            if(memberOperation.hasType(actorType, missionType)) {
                foundOperations.add(memberOperation);
            }
        }
        return foundOperations;
    }

}
