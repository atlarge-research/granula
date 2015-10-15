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

import nl.tudelft.pds.granula.archiver.entity.info.BasicInfo;
import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.ParentalEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;

import java.util.ArrayList;

public class CoordinatorCleanup extends AbstractOperationModel {

    public CoordinatorCleanup() {
        super(GraphXType.Coordinator, GraphXType.Cleanup);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GraphXType.SparkApplication, GraphXType.Execution));

        addInfoDerivation(new ColorDerivation(1, GraphXType.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));
        addInfoDerivation(new FilialStartTimeDerivation(4));
        addInfoDerivation(new FilialEndTimeDerivation(4));
        addInfoDerivation(new ParentalEndTimeDerivation(5));
        addInfoDerivation(new DurationDerivation(5));
        addInfoDerivation(new DataOffloadTimeDerivation(5));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("DataOffloadTime");
                }}));
    }


    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation materializes the vertices and edges. (first two stages) " +
                        "and then write the data back to DFS (last stage). ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }



    protected class DataOffloadTimeDerivation extends DerivationRule {

        public DataOffloadTimeDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            long duration = 0;

            for (Operation suboperation : operation.getChildren()) {
                duration += Long.parseLong(suboperation.getInfo("Duration").getValue());

            }


            BasicInfo ioTime = new BasicInfo("DataOffloadTime");
            ioTime.addInfo(String.valueOf(duration), new ArrayList<Source>());

            operation.addInfo(ioTime);
            return  true;
        }
    }

}
