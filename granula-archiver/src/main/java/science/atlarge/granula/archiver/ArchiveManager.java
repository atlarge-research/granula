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

package science.atlarge.granula.archiver;

import science.atlarge.granula.modeller.entity.Entity;
import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.modeller.rule.derivation.DerivationRule;
import science.atlarge.granula.modeller.rule.filling.BasicOperationFilling;
import science.atlarge.granula.modeller.rule.visual.VisualizationRule;
import science.atlarge.granula.modeller.source.log.Log;
import science.atlarge.granula.modeller.platform.Mission;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.source.log.LogInfo;
import science.atlarge.granula.modeller.platform.operation.OperationModel;
import science.atlarge.granula.modeller.rule.assembling.AssemblingRule;
import science.atlarge.granula.modeller.rule.filling.FillingRule;
import science.atlarge.granula.modeller.platform.Actor;
import science.atlarge.granula.modeller.rule.linking.LinkingRule;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchiveManager {

    Job job;

    public ArchiveManager(Job job) {
        this.job = job;
    }

    public void build() {
//        buildEnvironment();
        buildSystem();

    }


    public void buildSystem() {
        fillOperations();
        assembleOperations();
        linkOperations();
        enrichOperations();
    }



    public void fillOperations() {
        for (Log log : job.getPlatform().getLogs()) {
            if(log.isOf(LogInfo.InfoName, "StartTime")) {
                job.getPlatform().getModel().addFillingRule(
                        new BasicOperationFilling(1,
                                log.getAttr(LogInfo.ActorType), log.getAttr(LogInfo.ActorId),
                                log.getAttr(LogInfo.MissionType), log.getAttr(LogInfo.MissionId)));
            }
        }

        for (FillingRule fillingRule : job.getPlatform().getModel().getFillingRules()) {
            fillingRule.execute();
        }
    }

    public void assembleOperations() {

        List<AssemblingRule> assemblingRules = new ArrayList<>();

        for (Operation operation : job.getPlatform().getOperations()) {
            for (AssemblingRule assemblingRule : ((OperationModel) operation.getModel()).getAssemblingRules()) {
                assemblingRules.add(assemblingRule);
            }
        }

        for (AssemblingRule assemblingRule : assemblingRules) {
            assemblingRule.execute();
        }
    }


    public void linkOperations() {
        List<LinkingRule> linkingRules = new ArrayList<>();

        for (Operation operation : job.getPlatform().getOperations()) {
            for (LinkingRule linkingRule : ((OperationModel) operation.getModel()).getLinkingRules()) {
                linkingRules.add(linkingRule);
            }
        }

        Collections.sort(linkingRules);
        for (LinkingRule linkingRule : linkingRules) {
            linkingRule.execute();
        }


    }


    public void enrichOperations() {

        List<Operation> operationList = job.getPlatform().getOperations();

        List<DerivationRule> infoDerivationRules = new ArrayList<>();
        List<VisualizationRule> visualDerivationRules = new ArrayList<>();

        List<Entity> entities = new ArrayList<>();
        entities.add(job.getPlatform());
        for (Operation operation : operationList) {
            entities.add(operation);
        }

        for (Entity entity : entities) {
            for (DerivationRule derivationRule : entity.getModel().getInfoDerivationRules()) {
                infoDerivationRules.add(derivationRule);
            }
            for (VisualizationRule visualizationRule : entity.getModel().getVisualDerivationRules()) {
                visualDerivationRules.add(visualizationRule);
            }
        }

        Collections.sort(infoDerivationRules);
        Collections.sort(visualDerivationRules);

        for (DerivationRule infoDerivationRule : infoDerivationRules) {
            try {
                infoDerivationRule.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (VisualizationRule visualDerivationRule : visualDerivationRules) {
            visualDerivationRule.execute();
        }



    }

    public void linkMission(Mission mission) {
//        List<Operation> operations = mission.getOperations();
//        Map<String, Mission> missionMap = new HashMap<>();
//        for (Operation operation : operations) {
//            for (Operation subOperation : operation.getChildren()) {
//                Mission subMission = subOperation.getMission();
//                missionMap.put(String.format("%s-%s", subMission.getType(), subMission.getId()), subMission);
//            }
//        }
//
//        for (Operation operation : operations) {
//            for (Operation subOperation : operation.getChildren()) {
//                Mission subMission = subOperation.getMission();
//                Mission uniqueMission = missionMap.get(String.format("%s-%s", subMission.getType(), subMission.getId()));
//                subOperation.setMission(uniqueMission);
//                uniqueMission.addOperation(subOperation);
//            }
//        }
//
//        for (Mission uniqueSubMission : missionMap.values()) {
//            operations.get(0).getMission().addChild(uniqueSubMission);
//            uniqueSubMission.setParent(operations.get(0).getMission());
//
//            linkMission(uniqueSubMission);
//        }
    }

    public void linkActor(Actor actor) {
//        List<Operation> operations = actor.getOperations();
//        Map<String, Actor> actorMap = new HashMap<>();
//        for (Operation operation : operations) {
//            for (Operation subOperation : operation.getChildren()) {
//                Actor subActor = subOperation.getActor();
//                actorMap.put(String.format("%s-%s", subActor.getType(), subActor.getId()), subActor);
//            }
//        }
//
//        for (Operation operation : operations) {
//            for (Operation subOperation : operation.getChildren()) {
//                Actor subActor = subOperation.getActor();
//                Actor uniqueActor = actorMap.get(String.format("%s-%s", subActor.getType(), subActor.getId()));
//                subOperation.setActor(uniqueActor);
//                uniqueActor.addOperation(subOperation);
//            }
//        }
//
//        for (Actor uniqueSubActor : actorMap.values()) {
//            operations.get(0).getActor().addChild(uniqueSubActor);
//            uniqueSubActor.setParent(operations.get(0).getActor());
//            linkActor(uniqueSubActor);
//        }
    }

    public void buildEnvironment() {
//        List<EnvLog> utilRecords = job.getJobRecord().getUtilRecords();
//
//        Set<String> envIds = new LinkedHashSet<>();
//        Set<String> nodeIds = new LinkedHashSet<>();
//
//        for (EnvLog utilRecord : utilRecords) {
//            String sourceFilePath = utilRecord.getLocation().getLocation();
//            String[] parentDirs = sourceFilePath.split("/");
//            String nodeId = parentDirs[parentDirs.length - 2];
//            String envId = parentDirs[parentDirs.length - 3];
//            envIds.add(envId);
//            nodeIds.add(nodeId);
//        }
//
//        if(envIds.size() != 1 || nodeIds.size() <= 0) {
//            return;
////            throw new IllegalStateException();
//        }
//
//        String envId = (String) envIds.toArray()[0];
//        Environment env = new Environment(envId);
//        for (String nodeId : nodeIds) {
//            ComputationNode computationNode = new ComputationNode(nodeId);
//            computationNode.addProcess(new Process("all"));
//            env.addNode(computationNode);
//
//        }
//
//        for (EnvLog utilRecord : utilRecords) {
//            String sourceFilePath = utilRecord.getLocation().getLocation();
//            String[] parentDirs = sourceFilePath.split("/");
//            String nodeId = parentDirs[parentDirs.length - 2];
//            String uInfoName = (new File(sourceFilePath)).getName().replace(".rrd", "");
//
//            //System.out.println(String.format("%s, %s, %s", nodeId, processId, rInfoName));
//
//            TimeSeriesInfo timeSeriesInfo = new TimeSeriesInfo(uInfoName);
//            Source source = new LogSource(uInfoName, utilRecord);
//            timeSeriesInfo.addInfo("Bytes", utilRecord.getTimeSeries(), source);
//            env.addResourceInfo(timeSeriesInfo, nodeId);
//
//        }
//        job.setEnvironment(env);
    }

}
