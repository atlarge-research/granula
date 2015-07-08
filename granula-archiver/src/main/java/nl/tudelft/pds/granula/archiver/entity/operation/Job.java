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

import nl.tudelft.pds.granula.archiver.entity.Entity;
import nl.tudelft.pds.granula.archiver.entity.environment.Environment;
import nl.tudelft.pds.granula.archiver.record.JobRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 12-3-15.
 */
public class Job extends Entity {

    JobRecord jobRecord;
    Operation topOperation;
    Actor topActor;
    Mission topMission;
    Environment environment;
    List<Operation> memberOperations;

    public Job() {
        super();
        this.memberOperations = new ArrayList<>();
    }

    public Job(Operation topOperation, Actor topActor, Mission topMission, Environment environment) {
        super();
        setTopOperation(topOperation);
        setTopActor(topActor);
        setTopMission(topMission);
        setEnvironment(environment);
    }

    public JobRecord getJobRecord() {
        return jobRecord;
    }

    public void setJobRecord(JobRecord jobRecord) {
        this.jobRecord = jobRecord;
    }

    public List<Operation> getMemberOperations() {
        return memberOperations;
    }

    public void addMemberOperations(Operation memberOperation) {
        memberOperations.add(memberOperation);
    }

    public Operation findOperation(String actorType, String missionType) {
        Operation operation = null;
        for (Operation memberOperation : memberOperations) {
            if(memberOperation.hasType(actorType, missionType)) {
                if(operation == null) {
                    operation = memberOperation;
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        return operation;
    }

    public Operation getTopOperation() {
        return topOperation;
    }

    public void setTopOperation(Operation topOperation) {
        this.topOperation = topOperation;
    }

    public Actor getTopActor() {
        return topActor;
    }

    public void setTopActor(Actor topActor) {
        this.topActor = topActor;
    }

    public Mission getTopMission() {
        return topMission;
    }

    public void setTopMission(Mission topMission) {
        this.topMission = topMission;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String exportBasic() {
        return String.format("<Job uuid=\"%s\"></Job>", uuid);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Job uuid=\"%s\">", uuid));

        stringBuilder.append("<Operations>");
        stringBuilder.append(topOperation.export());
        stringBuilder.append("</Operations>");

        stringBuilder.append("<Actors>");
        stringBuilder.append(topActor.export());
        stringBuilder.append("</Actors>");

        stringBuilder.append("<Missions>");
        stringBuilder.append(topMission.export());
        stringBuilder.append("</Missions>");

//        stringBuilder.append(environment.export());

        stringBuilder.append(exportInfos());
        stringBuilder.append(exportVisuals());


        stringBuilder.append("</Job>");
        return stringBuilder.toString();
    }

}
