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

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.time.ParentalStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;

import java.util.ArrayList;

public class CoordinatorSetup extends AbstractOperationModel {

    public CoordinatorSetup() {
        super(GraphXType.Coordinator, GraphXType.Setup);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GraphXType.SparkApplication, GraphXType.Execution));

        addInfoDerivation(new ColorDerivation(1, GraphXType.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));
        addInfoDerivation(new FilialStartTimeDerivation(4));
        addInfoDerivation(new FilialEndTimeDerivation(4));
        addInfoDerivation(new ParentalStartTimeDerivation(5));
        addInfoDerivation(new DurationDerivation(5));
        addInfoDerivation(new DataLoadTimeDerivation(5));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("DataLoadTime");
                }}));
    }



    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation consists of a SparkJob which loads the data from the DFS " +
                        "to the RDD (first stage), and process the intial message. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }



    protected class DataLoadTimeDerivation extends DerivationRule {

        public DataLoadTimeDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            long startTime = Long.MAX_VALUE;
            long endTime = Long.MIN_VALUE;

            for (Operation suboperation : operation.getChildren()) {
                long subStartTime = Long.parseLong(suboperation.getInfo("StartTime").getValue());
                long subEndTime = Long.parseLong(suboperation.getInfo("EndTime").getValue());
                if(subEndTime > endTime) {
                    endTime = subEndTime;
                }
                if(subStartTime < startTime) {
                    startTime = subStartTime;
                }
            }


            Info ioTime = new Info("DataLoadTime");
            ioTime.addInfo(String.valueOf(endTime - startTime), new ArrayList<Source>());

            operation.addInfo(ioTime);
            return  true;
        }
    }

}
