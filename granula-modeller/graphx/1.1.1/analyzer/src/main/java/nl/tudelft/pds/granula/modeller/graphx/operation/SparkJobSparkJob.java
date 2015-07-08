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

package nl.tudelft.pds.granula.modeller.graphx.operation;

import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.linking.LinkingRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;

import java.util.ArrayList;
import java.util.List;

public class SparkJobSparkJob extends ConcreteOperationModel {

    public SparkJobSparkJob() {
        super(GraphXType.SparkJob, GraphXType.SparkJob);
    }

    public void loadRules() {
        super.loadRules();

//        addLinkingRule(new SparkJobParentLinking());

        addInfoDerivation(new ColorDerivation(1, GraphXType.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                }}));
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operations are submitted and executed one by one. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }



    public class SparkJobParentLinking extends LinkingRule {


        public SparkJobParentLinking() {
            super();
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            Operation parent = null;
            Operation sparkContext = null;
            Operation cleanup = null;
            List<Operation> matchedApps = new ArrayList<>();

            for (Operation candidateOperation : operation.getJob().getMemberOperations()) {


                if(candidateOperation.hasType(GraphXType.Coordinator, GraphXType.Setup)) {
                    matchedApps.add(candidateOperation);
                } else if(candidateOperation.hasType(GraphXType.Coordinator, GraphXType.Superstep)) {
                    matchedApps.add(candidateOperation);
                } else if(candidateOperation.hasType(GraphXType.Coordinator, GraphXType.Cleanup)) {
                    cleanup = candidateOperation;
                } else if(candidateOperation.hasType(GraphXType.SparkContext, GraphXType.SparkContext)) {
                    sparkContext = candidateOperation;
                }

            }

            if(matchedApps.size() > 0) {

                long myLogLine = Long.parseLong(operation.getRecord("StartTime").getRecordLocation().getLineNumber());
                long cloestDistance = Long.MAX_VALUE;
                Operation closestParent = null;

                for (Operation matchedApp : matchedApps) {
                    long parentStartTimeLogLine = Long.parseLong(matchedApp.getRecord("StartTime").getRecordLocation().getLineNumber());
                    long parentEndTimeLogLine = Long.parseLong(matchedApp.getRecord("EndTime").getRecordLocation().getLineNumber());
                    if(parentStartTimeLogLine <= myLogLine && parentEndTimeLogLine >= myLogLine) {
                        parent = matchedApp;
                    }
                }
            }

            if(parent == null) {
                System.out.print("SparkJob not matched");
//                parent = sparkContext;

                parent = cleanup;
            }

            operation.setParent(parent);
            parent.addChild(operation);
            return  true;
        }
    }


}
