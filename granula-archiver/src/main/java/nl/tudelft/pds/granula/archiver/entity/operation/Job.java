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
import nl.tudelft.pds.granula.archiver.source.record.JobRecord;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 12-3-15.
 */
@XmlRootElement(name="Job")
@XmlSeeAlso({Operation.class})
public class Job extends Entity {

    JobRecord jobRecord;
    Operation topOperation;
    Actor topActor;
    Mission topMission;
    Environment environment;
    List<Operation> memberOperations;
    List<Operation> operations;

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

    public void fillOperation() {
        operations = new ArrayList<>();
        operations.add(topOperation);
    }


    @XmlElementWrapper(name="Operations")
    @XmlElementRef
    public List<Operation> getOperations() {
        return operations;
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

    public List<Operation> findOperations(String actorType, String missionType) {
        List<Operation> operations = new ArrayList<>();
        for (Operation memberOperation : memberOperations) {
            if(memberOperation.hasType(actorType, missionType)) {
                operations.add(memberOperation);
            }
        }
        return operations;
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

    @XmlElementRef
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
