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

import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.*;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.*;
import nl.tudelft.pds.granula.modeller.rule.linking.IdentifierParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;

public class BspWorkerSuperstep extends AbstractOperationModel {

    public BspWorkerSuperstep() {
        super(GiraphType.BspWorker, GiraphType.Superstep);
    }

    public void loadRules() {
        super.loadRules();
        //addInfoDerivation(new NetworkUtilOperationDerivation(1));
        //addInfoDerivation(new CpuUtilOperationDerivation(1));
        //addInfoDerivation(new MemoryUtilOperationDerivation(1));
        //addVisualDerivation(new NetworkUtilVisualOperationDerivation(1));

        addLinkingRule(new IdentifierParentLinking(GiraphType.BspWorker, GiraphType.Equal, GiraphType.WorkerTask, GiraphType.Unique));
        addInfoDerivation(new FilialStartTimeDerivation(2));
        addInfoDerivation(new FilialEndTimeDerivation(2));
        addInfoDerivation(new DurationDerivation(3));

        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "SentMsgs", "SentMsgs"));
        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "ActiveVertices", "ActiveVertices"));

        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "SentMsgVolume", "SentMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "ReceivedMsgVolume", "ReceivedMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "LocalMsgVolume", "LocalMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(3, GiraphType.PostSuperstep, "RemoteMsgVolume", "RemoteMsgVolume"));
        addInfoDerivation(new FilialLongMaxDerivation(3, GiraphType.PostSuperstep, "ReceivedMsgVolume", "MaxReceivedMsgVolume"));



        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new TableVisualization(1, "ActivenessTable",
                new ArrayList<String>() {{
                    add("ActiveVertices");
                    add("SentMsgs");
                }}));
        addVisualDerivation(new TableVisualization(1, "NetworkTrafficTable",
                new ArrayList<String>() {{
                    add("SentMsgVolume");
                    add("ReceivedMsgVolume");
                    add("LocalMsgVolume");
                    add("RemoteMsgVolume");
                }}));
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation executes a BSP superstep. " +
                    "Each BspWorker prepares for the superstep, " +
                    "computes partitions and receives and sends messages, " +
                    "and waits for other BspWorker to complete.",
                    operation.getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }

}
