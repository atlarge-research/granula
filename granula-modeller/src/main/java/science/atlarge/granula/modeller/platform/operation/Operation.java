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

package science.atlarge.granula.modeller.platform.operation;

import science.atlarge.granula.modeller.entity.Containable;
import science.atlarge.granula.modeller.environment.Environment;
import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.modeller.platform.Actor;
import science.atlarge.granula.modeller.source.log.Log;
import science.atlarge.granula.modeller.source.log.LogInfo;
import science.atlarge.granula.modeller.platform.Mission;
import science.atlarge.granula.modeller.platform.Platform;
import science.atlarge.granula.util.json.Exclude;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Operation extends Containable {

    public String parentId;
    public List<String> childIds;
    public String actorId;
    public String missionId;

    @Exclude public Job job;
    @Exclude public Platform platform;
    @Exclude public Operation parent;
    @Exclude public List<Operation> children;
    @Exclude public Map<String, Log> logs;

    @Exclude protected Actor actor;
    @Exclude protected Mission mission;
    @Exclude protected Environment environment;

    public Operation() {
        super();
        infos = new LinkedHashMap<>();
        children = new ArrayList<>();
        logs = new LinkedHashMap<>();
        childIds = new ArrayList<>();
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        this.actorId = actor.getUuid();
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
        this.missionId = mission.getUuid();
    }


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Operation getParent() {
        return parent;
    }

    public void setParent(Operation parent) {
        this.parent = parent;
        this.parentId = parent.getUuid();
    }

    public void addChild(Operation operation) {
        children.add(operation);
        childIds.add(operation.getUuid());
    }

    public List<Operation> getChildren() {
        return children;
    }


    public boolean containsLog(String logType) {
        return logs.containsKey(logType);
    }


    public void addLog(Log log) {
        logs.put(log.getAttr(LogInfo.InfoName), log);
    }

    public List<Log> getLogs() {
        return new ArrayList<>(logs.values());
    }

    public Log getLog(String logType) {
        if(logs.containsKey(logType)) {
            return logs.get(logType);
        }
        else {
            throw new IllegalStateException(String.format("Operation %s cannot find log %s", getName(), logType));
        }
    }

    public String getName() {
        if(actor == null || mission == null) {
            throw new IllegalStateException();
        }
        return String.format("%s @ %s", actor.getName(), mission.getName());
    }

    public String getType() {
        if(actor == null || mission == null) {
            throw new IllegalStateException();
        }
        return String.format("%s-%s", actor.getType(), mission.getType());
    }

    public boolean hasType(String actorType, String missionType) {
        return getType().equals(String.format("%s-%s", actorType, missionType));
    }

    public Operation findSuboperation(String missionType) {
        Operation operation = null;
        for (Operation suboperation : getChildren()) {
            if(suboperation.getMission().getType().equals(missionType)) {
                if(operation == null) {
                    operation = suboperation;
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        return operation;
    }

    public List<Operation> findSuboperations(String actorType, String missionType) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : getChildren()) {
            if(operation.hasType(actorType, missionType)) {
                operations.add(operation);
            }
        }
        return operations;
    }

    public List<Operation> findSuboperations(String missionType) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : getChildren()) {
            if(operation.getMission().hasType(missionType)) {
                operations.add(operation);
            }
        }
        return operations;
    }


    public List<Operation> findDescendantOperations(String missionType, int searchLevel) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : getChildren()) {
            if(operation.getMission().hasType(missionType)) {
                operations.add(operation);
            }
            if(searchLevel - 1 > 0) {
                operations.addAll(operation.findDescendantOperations(missionType, searchLevel - 1));
            }
        }
        return operations;
    }

    public List<Operation> findSiblingOperations(String actorType, String missionType) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : getParent().getChildren()) {
            if(operation.hasType(actorType, missionType)) {
                if(!operation.getUuid().equals(this)) {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

    public List<Operation> findSiblingOperations(String missionType) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : getParent().getChildren()) {
            if(operation.getMission().hasType(missionType)) {
                if(!operation.getUuid().equals(this)) {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }



}
