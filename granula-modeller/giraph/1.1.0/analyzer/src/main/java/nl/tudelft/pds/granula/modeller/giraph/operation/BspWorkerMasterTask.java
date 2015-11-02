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
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.*;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TimeSeriesVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.VisualizationRule;

import java.util.ArrayList;
import java.util.List;

public class BspWorkerMasterTask extends ConcreteOperationModel {

    public BspWorkerMasterTask() {
        super(GiraphType.BspWorker, GiraphType.MasterTask);
    }

    public void loadRules() {
        super.loadRules();
        addLinkingRule(new UniqueParentLinking(GiraphType.BspMaster, GiraphType.BspIteration));

        addInfoDerivation(new SuperstepStatsDerivationRule(3));

        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-Vertices", GiraphType.GlobalSuperstep, "Vertices"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-ActiveVertices", GiraphType.GlobalSuperstep, "ActiveVertices"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-Edges", GiraphType.GlobalSuperstep, "Edges"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-SentMsgs", GiraphType.GlobalSuperstep, "SentMsgs"));

        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-SentMsgVolume", GiraphType.GlobalSuperstep, "SentMsgVolume"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-LocalMsgVolume", GiraphType.GlobalSuperstep, "LocalMsgVolume"));
        addInfoDerivation(new FilialTimeSeriesDerivation(5, "TS-RemoteMsgVolume", GiraphType.GlobalSuperstep, "RemoteMsgVolume"));

        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "Vertices", "Vertices"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "Edges", "Edges"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "ActiveVertices", "ActiveVertices"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "SentMsgs", "SentMsgs"));

        addInfoDerivation(new FilialLongAverageDerivation(5, GiraphType.GlobalSuperstep, "Vertices", "AvgVertices"));
        addInfoDerivation(new FilialLongAverageDerivation(5, GiraphType.GlobalSuperstep, "Edges", "AvgEdges"));
        addInfoDerivation(new FilialLongAverageDerivation(5, GiraphType.GlobalSuperstep, "ActiveVertices", "AvgActiveVertices"));
        addInfoDerivation(new FilialLongAverageDerivation(5, GiraphType.GlobalSuperstep, "SentMsgs", "AvgSentMsgs"));


        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "SentMsgVolume", "SentMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "ReceivedMsgVolume", "ReceivedMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "LocalMsgVolume", "LocalMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "RemoteMsgVolume", "RemoteMsgVolume"));
        addInfoDerivation(new FilialLongAggregationDerivation(5, GiraphType.GlobalSuperstep, "SyncOverhead", "SyncOverhead"));
//        addInfoDerivation(new FilialLongMaxDerivation(4, GiraphType.GlobalSuperstep, "MaxReceivedMsgVolume", "MaxReceivedMsgVolume"));


        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
//                    add("ComputationClass");
//                    add("DataInputPath");
                    add("NumSuperstep");
                    add("SyncOverhead");
                }}));

        addVisualDerivation(new TableVisualization(1, "ActivenessTable",
                new ArrayList<String>() {{
                    add("Vertices");
                    add("ActiveVertices");
                    add("Edges");
                    add("SentMsgs");

                    add("AvgVertices");
                    add("AvgActiveVertices");
                    add("AvgEdges");
                    add("AvgSentMsgs");
                }}));

        addVisualDerivation(new TableVisualization(1, "NetworkTrafficTable",
                new ArrayList<String>() {{
                    add("SentMsgVolume");
                    add("ReceivedMsgVolume");
                    add("LocalMsgVolume");
                    add("RemoteMsgVolume");
//                    add("MaxReceivedMsgVolume");
                }}));


        TimeSeriesVisualization trafficTSVisualization = new TimeSeriesVisualization(1, "NetworkTrafficVisual", "Network Traffic", "Message Volume", "");
        trafficTSVisualization.addY1Info("TS-SentMsgVolume");
        trafficTSVisualization.addY1Info("TS-LocalMsgVolume");
        trafficTSVisualization.addY1Info("TS-RemoteMsgVolume");
        addVisualDerivation(trafficTSVisualization);

        TimeSeriesVisualization activenessTSVisualization = new TimeSeriesVisualization(1, "ActivenessVisual", "Activeness", "No. of vertices", "No. of messages");

        activenessTSVisualization.addY1Info("TS-Vertices");
        activenessTSVisualization.addY1Info("TS-ActiveVertices");
        activenessTSVisualization.addY2Info("TS-Edges");
        activenessTSVisualization.addY2Info("TS-SentMsgs");

        addVisualDerivation(activenessTSVisualization);
    }


    protected class SuperstepStatsDerivationRule extends DerivationRule {

        public SuperstepStatsDerivationRule(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            int superstepNum = 0;

            List<Info> superstepStartTimes = new ArrayList<>();
            for (Operation suboperation : operation.getChildren()) {
                if(suboperation.getMission().getType().equals(GiraphType.GlobalSuperstep)) {
                    superstepNum++;

                }
            }
            InfoSource superstepNumSource = new InfoSource("Superstep", superstepStartTimes);
            List<Source> superstepNumSources = new ArrayList<>();
            superstepNumSources.add(superstepNumSource);


            BasicInfo superstepNumInfo = new BasicInfo("NumSuperstep");
            superstepNumInfo.setDescription("The [NumSuperstep] is the total number of all actual supersteps (non-dataloading).");
            superstepNumInfo.addInfo(String.valueOf(superstepNum), superstepNumSources);
            operation.addInfo(superstepNumInfo);

            return true;
        }
    }

    protected class SuperstepStatsVisualization extends VisualizationRule {

        public SuperstepStatsVisualization(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            boolean succeed = true;

            TimeSeriesVisual timeSeriesVisual = new TimeSeriesVisual("NetworkUtilVisual");

            timeSeriesVisual.setTitle("Network Utilization");

            long startTime = Long.parseLong(operation.getInfo("StartTime").getValue());
            long endTime = Long.parseLong(operation.getInfo("EndTime").getValue());
            timeSeriesVisual.setXAxis("ExecutionTime", "s", String.valueOf(startTime), String.valueOf(endTime));

            List<TimeSeriesInfo> y1Infos = new ArrayList<>();
            TimeSeriesInfo pktsIn = (TimeSeriesInfo) operation.getInfo("pkts_in");
            TimeSeriesInfo pktsOut = (TimeSeriesInfo) operation.getInfo("pkts_out");
            y1Infos.add(pktsIn);
            y1Infos.add(pktsOut);
            double y1Min = getMin(y1Infos);
            double y1Max = getMax(y1Infos);
            double y1Padding = (y1Max - y1Min) / 10.0;
            timeSeriesVisual.setY1Axis("Num. Packages", "", String.valueOf(y1Min - y1Padding), String.valueOf(y1Max + y1Padding));
            for (TimeSeriesInfo y1Info : y1Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY1(y1Info);
            }

            List<TimeSeriesInfo> y2Infos = new ArrayList<>();
            TimeSeriesInfo bytesIn = (TimeSeriesInfo) operation.getInfo("bytes_in");
            TimeSeriesInfo bytesOut = (TimeSeriesInfo) operation.getInfo("bytes_out");
            y2Infos.add(bytesIn);
            y2Infos.add(bytesOut);
            double y2Min = getMin(y2Infos);
            double y2Max = getMax(y2Infos);
            double y2Padding = (y2Max - y2Min) / 10.0;
            timeSeriesVisual.setY2Axis("Package Volume", "", String.valueOf(y2Min - y2Padding), String.valueOf(y2Max + y2Padding));
            for (TimeSeriesInfo y2Info : y2Infos) {
                timeSeriesVisual.addTimeSeriesInfoToY2(y2Info);
            }

            operation.addVisual(timeSeriesVisual);

            return succeed;
        }

        public double getMax(List<TimeSeriesInfo> timeSeriesInfos) {
            List<Double> pMaxValues = new ArrayList<>();
            double finalMaxValue = Double.MIN_VALUE;

            for (TimeSeriesInfo timeSeriesInfo : timeSeriesInfos) {
                if(!timeSeriesInfo.getTimeSeries().empty()) {
                    pMaxValues.add(timeSeriesInfo.getTimeSeries().maxValue());
                }
            }

            if(pMaxValues.size() != 0) {
                for (Double pMaxValue : pMaxValues) {
                    finalMaxValue = Math.max(pMaxValue, finalMaxValue);
                }
                return finalMaxValue;
            } else {
                return 10;
            }
        }

        public double getMin(List<TimeSeriesInfo> timeSeriesInfos) {
            List<Double> pMinValues = new ArrayList<>();
            double finalMinValue = Double.MAX_VALUE;

            for (TimeSeriesInfo timeSeriesInfo : timeSeriesInfos) {
                if(!timeSeriesInfo.getTimeSeries().empty()) {
                    pMinValues.add(timeSeriesInfo.getTimeSeries().minValue());
                }
            }

            if(pMinValues.size() != 0) {
                for (Double pMinValue : pMinValues) {
                    finalMinValue = Math.min(pMinValue, finalMinValue);
                }
                return finalMinValue;
            } else {
                return -10;
            }
        }
    }

}
