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
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.RecordInfoDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.filling.UniqueIdOperationFilling;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.graphx.GraphXType;

import java.util.ArrayList;

public class CoordinatorSuperstep extends ConcreteOperationModel {

    public CoordinatorSuperstep() {
        super(GraphXType.Coordinator, GraphXType.Superstep);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GraphXType.Coordinator, GraphXType.BspIteration));
        addFillingRule(new UniqueIdOperationFilling(1, GraphXType.MsgReceiver, GraphXType.MsgReceive));
        addFillingRule(new UniqueIdOperationFilling(1, GraphXType.GraphConstructor, GraphXType.GraphConstruct));
        addFillingRule(new UniqueIdOperationFilling(1, GraphXType.GraphUpdater, GraphXType.GraphUpdate));
        addFillingRule(new UniqueIdOperationFilling(1, GraphXType.MsgSender, GraphXType.MsgSend));
        addFillingRule(new UniqueIdOperationFilling(1, GraphXType.MsgCounter, GraphXType.MsgCount));

        addInfoDerivation(new FilialStartTimeDerivation(4));
        addInfoDerivation(new FilialEndTimeDerivation(4));
        addInfoDerivation(new DurationDerivation(5));

        addInfoDerivation(new RecordInfoDerivation(1, "MessageCount"));
        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("MessageCount");
                }}));
    }


    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation consists of a SparkJob, " +
                        "which starts with procssing the messages (modify the vertex data in first stage), " +
                        "and then send messages (modifying the edge data in the third stage), " +
                        "and finally count the messages (in the fourth stage, needed to trigger the previous process). ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }

}
