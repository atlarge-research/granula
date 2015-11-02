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
import nl.tudelft.pds.granula.modeller.rule.derivation.*;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.linking.IdentifierParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;
import java.util.List;

import java.util.ArrayList;

public class GlobalCoordinatorGlobalSuperstep extends ConcreteOperationModel {

    public GlobalCoordinatorGlobalSuperstep() {
        super(GiraphType.GlobalCoordinator, GiraphType.GlobalSuperstep);
    }

    public void loadRules() {
        super.loadRules();

//        addLinkingRule(new UniqueParentLinking(GiraphType.BspMaster, GiraphType.BspIteration));
        addLinkingRule(new IdentifierParentLinking(GiraphType.BspWorker, "M", GiraphType.MasterTask, GiraphType.Unique));

        RecordInfoDerivation totalNumVertices = new RecordInfoDerivation(1, "TotalNumVertices", "Vertices");
        addInfoDerivation(totalNumVertices);
        totalNumVertices.setDescription("[TotalNumVertices] is the global vertex count. This metric is already included in the standard implementation of Giraph.");

        RecordInfoDerivation totalNumEdges = new RecordInfoDerivation(1, "TotalNumEdges", "Edges");
        addInfoDerivation(totalNumEdges);
        totalNumEdges.setDescription("[TotalNumEdges] is the global vertex count. This metric is already included in the standard implementation of Giraph.");

        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "SentMsgs", "SentMsgs"));
        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "ActiveVertices", "ActiveVertices"));

        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "SentMsgVolume", "SentMsgVolume"));
        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "ReceivedMsgVolume", "ReceivedMsgVolume"));
        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "LocalMsgVolume", "LocalMsgVolume"));
        addInfoDerivation(new SuperstepLongAggregationDerivation(4, "RemoteMsgVolume", "RemoteMsgVolume"));
//        addInfoDerivation(new FilialLongMaxDerivation(4, GiraphType.Superstep, "ReceivedMsgVolume", "MaxReceivedMsgVolume"));

        addInfoDerivation(new SyncOverheadDerivation(4));

        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("SyncOverhead");

                }}));
        addVisualDerivation(new TableVisualization(1, "ActivenessTable",
                new ArrayList<String>() {{
                    add("Vertices");
                    add("ActiveVertices");
                    add("Edges");
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

    protected class SyncOverheadDerivation extends DerivationRule {

        public SyncOverheadDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            List<Info> computationDurationInfos = new ArrayList<>();


            for (Operation sibOperation : operation.getParent().findSiblingOperations(GiraphType.WorkerTask)) {
                for (Operation subOperation : sibOperation.getChildren()) {
                    if(subOperation.getMission().hasType(GiraphType.Superstep)) {
                        if(subOperation.getMission().getId().equals(operation.getMission().getId())) {
                            for (Operation computationOperation : subOperation.findSuboperations(GiraphType.Computation)) {
                                computationDurationInfos.add(computationOperation.getInfo("Duration"));
                            }
                        }
                    }
                }

            }

            List<Source> sources = new ArrayList<>();
            sources.add(new InfoSource("ComputationDuration", computationDurationInfos));

            BasicInfo syncOverheadInfo = new BasicInfo("SyncOverhead");

            long max = Long.MIN_VALUE;
            long total = 0;
            long numWorkers = computationDurationInfos.size();
            for (Info compDurationInfo : computationDurationInfos) {
                long duration = Long.parseLong(compDurationInfo.getValue());
                max = Math.max(duration, max);
                total += duration;
            }
            long avg = total / numWorkers;

            long syncOverhead = max - avg;

            syncOverheadInfo.addInfo(String.valueOf(syncOverhead), sources);
            syncOverheadInfo.setDescription(String.format("[%s] is the difference between the maximum and the average [Duration] of " +
                    "all Computation sub-operations. The intuition here is that the average [Duration] represents the optimal scenario, " +
                    "in which partitions are distributed in perfect balance. " +
                    "The extra time needed til the last Computation is done are therefore synchronization overhead. ", syncOverheadInfo.getName()));
            operation.addInfo(syncOverheadInfo);
            return  true;
        }
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation coordinates each Bsp superstep among BspWorkers. " +
                            "This operation starts after the BspMaster starts superstep %s, " +
                            "and ends when the BspMaster ends superstep %s. ",
                    operation.getName(), operation.getMission().getId(), operation.getMission().getId());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }

}
