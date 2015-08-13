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
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.RecordInfoDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.RecordTimeSeriesDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.IdentifierParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TimeSeriesVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;

public class BspWorkerComputation extends ConcreteOperationModel {

    public BspWorkerComputation() {
        super(GiraphType.BspWorker, GiraphType.Computation);
    }

    public void loadRules() {
        super.loadRules();
        addLinkingRule(new IdentifierParentLinking(GiraphType.GlobalCoordinator, GiraphType.Unique, GiraphType.GlobalSuperstep, GiraphType.Equal));
//         RecordInfoDerivation receivedMsgReqsVolume = new RecordInfoDerivation(1, "ReceivedMsgReqsVolume");
//        addInfoDerivation(receivedMsgReqsVolume);
//        receivedMsgReqsVolume.setDescription("[ReceivedMsgReqsVolume] is the volume of all worker message requests received by a BspWorker in a superstep. " +
//                "This value is aggregated from WorkerRequestServerHandler.processRequest() -> (WritableRequest) request).getType() == RequestType.SEND_WORKER_MESSAGES_REQUEST -> .getSerializedSize() ");

        addInfoDerivation(new SummaryDerivation(10));

        addInfoDerivation(new RecordInfoDerivation(1, "ThreadNum"));
        addInfoDerivation(new RecordInfoDerivation(1, "PartitionNum"));
        addInfoDerivation(new RecordTimeSeriesDerivation(1, "TS-Partition"));
        addInfoDerivation(new RecordTimeSeriesDerivation(1, "TS-SentMsgVolume"));
        addInfoDerivation(new RecordTimeSeriesDerivation(1, "TS-ReceivedMsgVolume"));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("ThreadNum");
                    add("PartitionNum");
                }}));

        TimeSeriesVisualization computeTSVisualization = new TimeSeriesVisualization(1, "ComputationVisual", "Computation", "No. of Partition", "No. of Messages");
        computeTSVisualization.addY1Info("TS-Partition");
        computeTSVisualization.addY2Info("TS-SentMsgVolume");
        computeTSVisualization.addY2Info("TS-ReceivedMsgVolume");
        addVisualDerivation(computeTSVisualization);
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation process graph partitions, " +
                    "thus doing the computation for each vertex in the partitions. ", operation.getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }


}
