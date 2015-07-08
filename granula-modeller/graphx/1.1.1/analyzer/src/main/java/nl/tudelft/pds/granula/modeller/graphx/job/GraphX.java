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

package nl.tudelft.pds.granula.modeller.graphx.job;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.job.JobModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.filling.UniqueOperationFilling;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;
import nl.tudelft.pds.granula.modeller.graphx.operation.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wing on 12-3-15.
 */
public class GraphX extends JobModel {

    public GraphX() {
        super();
        addOperationModel(new TopActorTopMission());
        addOperationModel(new SparkAppDeployment());
        addOperationModel(new SparkAppDecommission());
        addOperationModel(new SparkAppAppStartup());
        addOperationModel(new SparkAppExecutorAssignment());
        addOperationModel(new SparkAppBspExecution());
        addOperationModel(new SparkAppAppTermination());

        addOperationModel(new CoordinatorSetup());
        addOperationModel(new CoordinatorBspIteration());
        addOperationModel(new CoordinatorProcessing());
        addOperationModel(new CoordinatorCleanup());
        addOperationModel(new CoordinatorSuperstep());

        addOperationModel(new GraphUpdaterGraphUpdate());
        addOperationModel(new MsgSenderMsgSend());
        addOperationModel(new MsgCounterMsgCount());
        addOperationModel(new MsgReceiverMsgReceive());
        addOperationModel(new GraphConstructorGraphConstruct());

        addOperationModel(new SparkJobSparkJob());
        addOperationModel(new DagSchedulerStage());
        addOperationModel(new ExecutorTask());
    }

    public void loadRules() {
        super.loadRules();

        addFillingRule(new UniqueOperationFilling(2, GraphXType.TopActor, GraphXType.TopMission));
        addFillingRule(new UniqueOperationFilling(2, GraphXType.Coordinator, GraphXType.Setup));
        addFillingRule(new UniqueOperationFilling(2, GraphXType.SparkApplication, GraphXType.Deployment));
        addFillingRule(new UniqueOperationFilling(2, GraphXType.SparkApplication, GraphXType.Decommission));
        addFillingRule(new UniqueOperationFilling(2, GraphXType.Coordinator, GraphXType.Cleanup));
        addFillingRule(new UniqueOperationFilling(2, GraphXType.SparkApplication, GraphXType.Execution));

        addInfoDerivation(new JobNameDerivationRule(4));
    }


    protected class JobNameDerivationRule extends DerivationRule {

        public JobNameDerivationRule(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

                Job job = (Job) entity;

                Operation topOperation = null;
                for(Operation operation: job.getMemberOperations()) {
                    if(operation.hasType(GraphXType.TopActor, GraphXType.TopMission)) {
                        topOperation = operation;
                    }
                }

                Info executorSize = topOperation.getInfo("ExecutorSize");
                Info executorMemory = topOperation.getInfo("ExecutorMemory");

                Info computeClass = topOperation.getInfo("ApplicationName");
                Info dataInputPath = topOperation.getInfo("DataInputPath");

                String fileName = new File(dataInputPath.getValue()).getName().replace("_FCF", "");

                String jobName = String.format("Job[%s-%s, %sx%sMB]",
                        computeClass.getValue().replace("Computation", ""), fileName,
                        executorSize.getValue(), executorMemory.getValue());


                Info jobNameInfo = new Info("JobName");
                jobNameInfo.addInfo(jobName, new ArrayList<Source>());
                job.addInfo(jobNameInfo);
                return  true;

        }
    }
}
