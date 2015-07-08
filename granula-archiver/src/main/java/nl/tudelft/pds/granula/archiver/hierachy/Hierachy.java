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

package nl.tudelft.pds.granula.archiver.hierachy;

import nl.tudelft.pds.granula.archiver.entity.environment.Environment;
import nl.tudelft.pds.granula.archiver.entity.operation.Actor;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.entity.operation.Mission;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 11-2-15.
 */
public class Hierachy {

    Job job;
    Operation topOperation;
    List<Operation> memberOperations;

    Actor topActor;
    Mission topMission;
    Environment environment;

    public Hierachy() {
        this.memberOperations = new ArrayList<>();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public List<Operation> getMemberOperations() {
        return memberOperations;
    }

    public void addMemberOperations(Operation memberOperation) {
        memberOperations.add(memberOperation);
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

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
