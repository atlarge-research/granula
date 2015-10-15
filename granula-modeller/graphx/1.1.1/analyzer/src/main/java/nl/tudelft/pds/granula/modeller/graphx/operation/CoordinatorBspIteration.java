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
import nl.tudelft.pds.granula.modeller.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.FilialLongAggregationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.FilialTimeSeriesDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.RemoveIfEmptyLinking;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TimeSeriesVisualization;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;

import java.util.ArrayList;

public class CoordinatorBspIteration extends AbstractOperationModel {

    public CoordinatorBspIteration() {
        super(GraphXType.Coordinator, GraphXType.BspIteration);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GraphXType.SparkApplication, GraphXType.Execution));
        addLinkingRule(new RemoveIfEmptyLinking(2));

        addInfoDerivation(new FilialLongAggregationDerivation(4, GraphXType.Superstep, "MessageCount", "MessageCount"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-MessageCount", GraphXType.Superstep, "MessageCount"));
        addInfoDerivation(new FilialStartTimeDerivation(5));
        addInfoDerivation(new FilialEndTimeDerivation(5));
        addInfoDerivation(new DurationDerivation(6));
        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("MessageCount");
                }}));

        TimeSeriesVisualization activenessTSVisualization =
                new TimeSeriesVisualization(1, "ActivenessVisual", "Activeness", "No. of vertices", "No. of messages");
        activenessTSVisualization.addY2Info("TS-MessageCount");
        addVisualDerivation(activenessTSVisualization);
    }


    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation consists of a list of supersteps, " +
                        "which is the main operation in the Pregel engine of GraphX. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }

}
