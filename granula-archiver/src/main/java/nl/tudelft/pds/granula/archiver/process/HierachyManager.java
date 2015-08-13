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

package nl.tudelft.pds.granula.archiver.process;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.operation.Actor;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.entity.operation.Mission;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordInfo;
import nl.tudelft.pds.granula.modeller.model.job.JobModel;
import nl.tudelft.pds.granula.modeller.model.operation.OperationModel;
import nl.tudelft.pds.granula.modeller.rule.assembling.AssemblingRule;
import nl.tudelft.pds.granula.modeller.rule.filling.FillingRule;
import nl.tudelft.pds.granula.modeller.rule.linking.LinkingRule;
import nl.tudelft.pds.granula.util.UuidGenerator;

import java.util.*;

public class HierachyManager {

    Job job;

    public HierachyManager(Job job) {
        this.job = job;
    }

    public void build() {
        buildEnvironment();

        initOperations();
        assembleOperations();
        fillOperations();
        linkOperations();

        job.fillOperation();
    }

    public void initOperations() {

        for (Record record : job.getJobRecord().getRecords()) {
            if(record.isOf(RecordInfo.InfoName, "StartTime")) {
                Operation operation = new Operation();
                operation.setUuid(UuidGenerator.getRandomUUID());
                operation.setJob(job);
                operation.setEnvironment(job.getEnvironment());
                operation.setActor(new Actor(record.getAttr(RecordInfo.ActorType), record.getAttr(RecordInfo.ActorId)));
                operation.setMission(new Mission(record.getAttr(RecordInfo.MissionType), record.getAttr(RecordInfo.MissionId)));
                operation.setModel(((JobModel) job.getModel()).getOperationModel(operation.getType()));
                job.addMemberOperations(operation);
            }
        }
    }

    public void assembleOperations() {
        List<AssemblingRule> assemblingRules = new ArrayList<>();

        for (Operation operation : job.getMemberOperations()) {
            for (AssemblingRule assemblingRule : ((OperationModel) operation.getModel()).getAssemblingRules()) {
                assemblingRules.add(assemblingRule);
            }
        }

        Collections.sort(assemblingRules);
        for (AssemblingRule assemblingRule : assemblingRules) {
            assemblingRule.execute();
        }
    }


    public void fillOperations() {

        boolean done = false;
        int level = Integer.MIN_VALUE;

        List<FillingRule> fillingRules = new ArrayList<>();

        while(!done) {

            fillingRules.clear();

            for (FillingRule fillingRule : job.getModel().getFillingRules()) {
                fillingRules.add(fillingRule);
            }
            for (Operation operation : job.getMemberOperations()) {
                for (FillingRule fillingRule : ((OperationModel) operation.getModel()).getFillingRules()) {
                    fillingRules.add(fillingRule);
                }
            }

            Collections.sort(fillingRules);

            if(fillingRules.size() != 0) {

                if(level > fillingRules.get(fillingRules.size() - 1).getLevel()) {
                    done = true;
                }


                level = Math.max(fillingRules.get(0).getLevel(), level);


                for (FillingRule fillingRule : fillingRules) {
                    if(fillingRule.getLevel() == level) {
                        fillingRule.execute();
                    }
                }
                level = level + 1;
            } else {
                done = true;
            }
        }
    }

    public void linkOperations() {
        List<LinkingRule> linkingRules = new ArrayList<>();

        for (Operation operation : job.getMemberOperations()) {
            for (LinkingRule linkingRule : ((OperationModel) operation.getModel()).getLinkingRules()) {
                linkingRules.add(linkingRule);
            }
        }

        Collections.sort(linkingRules);
        for (LinkingRule linkingRule : linkingRules) {
            linkingRule.execute();
        }


        linkTopOperation();
        linkActor(job.getTopActor());
        linkMission(job.getTopMission());


    }

    public void linkTopOperation() {
        for (Operation operation : job.getMemberOperations()) {
            if(operation.getActor().getType().equals(Identifier.TopActor) &&
                    operation.getMission().getType().equals(Identifier.TopMission)) {
                job.setTopOperation(operation);
                job.setTopActor(operation.getActor());
                job.setTopMission(operation.getMission());
            }
        }
    }

    public void linkMission(Mission mission) {
        List<Operation> operations = mission.getOperations();
        Map<String, Mission> missionMap = new HashMap<>();
        for (Operation operation : operations) {
            for (Operation subOperation : operation.getChildren()) {
                Mission subMission = subOperation.getMission();
                missionMap.put(String.format("%s-%s", subMission.getType(), subMission.getId()), subMission);
            }
        }

        for (Operation operation : operations) {
            for (Operation subOperation : operation.getChildren()) {
                Mission subMission = subOperation.getMission();
                Mission uniqueMission = missionMap.get(String.format("%s-%s", subMission.getType(), subMission.getId()));
                subOperation.setMission(uniqueMission);
                uniqueMission.addOperation(subOperation);
            }
        }

        for (Mission uniqueSubMission : missionMap.values()) {
            operations.get(0).getMission().addChild(uniqueSubMission);
            uniqueSubMission.setParent(operations.get(0).getMission());

            linkMission(uniqueSubMission);
        }
    }

    public void linkActor(Actor actor) {
        List<Operation> operations = actor.getOperations();
        Map<String, Actor> actorMap = new HashMap<>();
        for (Operation operation : operations) {
            for (Operation subOperation : operation.getChildren()) {
                Actor subActor = subOperation.getActor();
                actorMap.put(String.format("%s-%s", subActor.getType(), subActor.getId()), subActor);
            }
        }

        for (Operation operation : operations) {
            for (Operation subOperation : operation.getChildren()) {
                Actor subActor = subOperation.getActor();
                Actor uniqueActor = actorMap.get(String.format("%s-%s", subActor.getType(), subActor.getId()));
                subOperation.setActor(uniqueActor);
                uniqueActor.addOperation(subOperation);
            }
        }

        for (Actor uniqueSubActor : actorMap.values()) {
            operations.get(0).getActor().addChild(uniqueSubActor);
            uniqueSubActor.setParent(operations.get(0).getActor());
            linkActor(uniqueSubActor);
        }
    }


    public void buildEnvironment() {
//        List<ResourceRecord> resourceRecords = jobRecord.getResourceRecords();
//
//        Set<String> envIds = new LinkedHashSet<>();
//        Set<String> nodeIds = new LinkedHashSet<>();

//        for (ResourceRecord resourceRecord : resourceRecords) {
//            String sourceFilePath = resourceRecord.getRecordLocation().getLocation();
//            String[] parentDirs = sourceFilePath.split("/");
//            envIds.add(parentDirs[2]);
//            nodeIds.add(parentDirs[3]);
//        }

//        if(envIds.size() != 1 || nodeIds.size() <= 0) {
//            throw new IllegalStateException();
//        }

//        String envId = (String) envIds.toArray()[0];
//        Environment env = new Environment(envId);
//        for (String nodeId : nodeIds) {
//            ComputationNode computationNode = new ComputationNode(nodeId);
//            computationNode.addProcess(new Process("all"));
//            env.addNode(computationNode);
//
//        }

//        for (ResourceRecord resourceRecord : resourceRecords) {
//            String sourceFilePath = resourceRecord.getRecordLocation().getLocation();
//            String[] parentDirs = sourceFilePath.split("/");
//            String nodeId = parentDirs[3];
//            String[] fileNameParts = parentDirs[4].split("-");
//            String processId = (fileNameParts.length > 1) ? fileNameParts[0] : "all";
//            String rInfoName = ((fileNameParts.length > 1) ? fileNameParts[1] : fileNameParts[0]).replace(".rrd", "");
//
//            //System.out.println(String.format("%s, %s, %s", nodeId, processId, rInfoName));
//
//            TimeSeriesInfo timeSeriesInfo = new TimeSeriesInfo(rInfoName);
//            Source source = new RecordSource(rInfoName, resourceRecord);
//            timeSeriesInfo.addInfo("Bytes", resourceRecord.getTimeSeries(), source);
//            env.addResourceInfo(timeSeriesInfo, nodeId);
//
//        }

//        job.setEnvironment(env);

    }

}
