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

package nl.tudelft.pds.granula.modeller.giraph.operation;

import nl.tudelft.pds.granula.archiver.entity.info.*;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.entity.visual.TimeSeriesVisual;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.*;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.VisualizationRule;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.TimeSeriesVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;
import java.util.List;

public class BspMasterBspIteration extends ConcreteOperationModel {

    public BspMasterBspIteration() {
        super(GiraphType.BspMaster, GiraphType.BspIteration);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GiraphType.AppMaster, GiraphType.BspExecution));

        addInfoDerivation(new RecordInfoDerivation(1, "ComputationClass"));
        addInfoDerivation(new RecordInfoDerivation(1, "DataInputPath"));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("ComputationClass");
                    add("DataInputPath");
//                    add("NumSuperstep");
//                    add("SyncOverhead");
                }}));

    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation executes the acutal Bulk Synchronous Parallel programming model, " +
                    "which contains a set of %s operation. ", operation.getName(), "GlobalSuperstep");
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }




}
