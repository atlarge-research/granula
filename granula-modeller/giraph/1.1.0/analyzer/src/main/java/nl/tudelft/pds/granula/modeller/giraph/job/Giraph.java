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

package nl.tudelft.pds.granula.modeller.giraph.job;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.job.JobModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.giraph.operation.*;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wing on 12-3-15.
 */
public class Giraph extends JobModel {

    public Giraph() {
        super();
        addOperationModel(new TopActorTopMission());

        addOperationModel(new AppMasterDeployment());
            addOperationModel(new AppMasterAppStartup());
            addOperationModel(new AppMasterContainerAssignment());
            addOperationModel(new AppMasterContainerLoad());
        addOperationModel(new AppMasterBspExecution());
            addOperationModel(new BspMasterBspSetup());
                addOperationModel(new GlobalCoordinatorGlobalStartup());
                    addOperationModel(new BspWorkerSetup());
                    addOperationModel(new BspWorkerZookeerpSetup());
                addOperationModel(new GlobalCoordintorGlobalDataload());
                    addOperationModel(new BspWorkerDataload());
                    addOperationModel(new BspWorkerPostDataload());
            addOperationModel(new BspMasterBspIteration());
                addOperationModel(new GlobalCoordinatorGlobalSuperstep());
                    addOperationModel(new BspWorkerPrepSuperstep());
                    addOperationModel(new BspWorkerComputation());
                    addOperationModel(new BspWorkerMsgSend());
                    addOperationModel(new BspWorkerPostSuperstep());
            addOperationModel(new BspMasterBspCleanup());
                addOperationModel(new BspWorkerZookeeperCleanup());
                addOperationModel(new BspWorkerClientCleanup());
                addOperationModel(new BspWorkerServerCleanup());
                addOperationModel(new BspWorkerUnknown3Cleanup());
                addOperationModel(new BspWorkerUnknown4Cleanup());
                addOperationModel(new BspWorkerDataOffload());
                addOperationModel(new BspWorkerCleanup());
                addOperationModel(new BspWorkerFinalCleanup());
                    addOperationModel(new BspWorkerZookeeperOfflining());
                    addOperationModel(new BspWorkerOutputMerge());
        addOperationModel(new AppMasterUndeployment());
            addOperationModel(new AppMasterContainerOffload());
                addOperationModel(new AppMasterAppTermination());
    }

    public void loadRules() {
        super.loadRules();
        addInfoDerivation(new JobNameDerivationRule(2));
    }


    protected class JobNameDerivationRule extends DerivationRule {

        public JobNameDerivationRule(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

                Job job = (Job) entity;

                Operation bspIteration = null;
                Operation containerAssignment = null;
                for(Operation operation: job.getTopOperation().getChildren()) {
                    if(operation.hasType(GiraphType.AppMaster, GiraphType.BspExecution)) {
                        for (Operation suboperation : operation.getChildren()) {
                            if (suboperation.hasType(GiraphType.BspMaster, GiraphType.BspIteration)) {
                                bspIteration = suboperation;
                            }
                        }
                    }
                }
                for(Operation operation: job.getTopOperation().getChildren()) {
                    if(operation.hasType(GiraphType.AppMaster, GiraphType.Deployment)) {
                        for (Operation suboperation : operation.getChildren()) {
                            if(suboperation.hasType(GiraphType.AppMaster, GiraphType.ContainerAssignment)) {
                                containerAssignment = suboperation;
                            }
                        }
                    }
                }

                Info numContainers = containerAssignment.getInfo("NumContainers");
                Info containerHeapSize = containerAssignment.getInfo("ContainerHeapSize");

                Info computeClass = bspIteration.getInfo("ComputationClass");
                Info dataInputPath = bspIteration.getInfo("DataInputPath");

                String fileName = new File(dataInputPath.getValue()).getName();

                String jobName = String.format("Job[%s-%s, %sx%sMB]",
                        computeClass.getValue().replace("Computation", ""), fileName,
                        numContainers.getValue(), containerHeapSize.getValue());

                Info jobNameInfo = new Info("JobName");
                jobNameInfo.addInfo(jobName, new ArrayList<Source>());
                job.addInfo(jobNameInfo);
                return  true;

        }
    }
}
