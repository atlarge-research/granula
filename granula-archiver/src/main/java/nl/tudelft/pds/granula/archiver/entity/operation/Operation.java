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

package nl.tudelft.pds.granula.archiver.entity.operation;

import nl.tudelft.pds.granula.archiver.entity.*;
import nl.tudelft.pds.granula.archiver.entity.environment.Environment;
import nl.tudelft.pds.granula.archiver.record.Record;
import nl.tudelft.pds.granula.archiver.record.RecordInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Operation extends Entity {

    public Job job;
    public Operation parent;
    public List<Operation> children;
    public Map<String, Record> records;

    protected Actor actor;
    protected Mission mission;
    protected Environment environment;

    public Operation() {
        super();
        infos = new LinkedHashMap<>();
        children = new ArrayList<>();
        records = new LinkedHashMap<>();
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
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
    }

    public void addChild(Operation operation) {
        children.add(operation);
    }

    public List<Operation> getChildren() {
        return children;
    }


    public boolean hasRecord(String recordType) {
        return records.containsKey(recordType);
    }


    public void addRecord(Record record) {
        records.put(record.getAttr(RecordInfo.InfoName), record);
    }

    public List<Record> getRecords() {
        return new ArrayList<>(records.values());
    }

    public Record getRecord(String recordType) {
        if(records.containsKey(recordType)) {
            return records.get(recordType);
        }
        else {
            throw new IllegalStateException();
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

    public String exportBasic() {
        return String.format("<Operation uuid=\"%s\"></Operation>", uuid);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Operation uuid=\"%s\" type=\"%s\" name=\"%s\">", uuid, getType(), getName()));


        stringBuilder.append(actor.exportBasic());
        stringBuilder.append(mission.exportBasic());

        stringBuilder.append(exportInfos());
        stringBuilder.append(exportVisuals());

        stringBuilder.append("<Children>");
        for (Operation child : children) {
            stringBuilder.append(child.export());
        }
        stringBuilder.append("</Children>");


        stringBuilder.append("</Operation>");
        return stringBuilder.toString();
    }

}
