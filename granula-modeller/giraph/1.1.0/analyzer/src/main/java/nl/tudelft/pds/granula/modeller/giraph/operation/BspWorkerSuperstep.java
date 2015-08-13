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
import nl.tudelft.pds.granula.archiver.entity.info.TimeSeriesInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.entity.visual.TimeSeriesVisual;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.ResourceUtilDerivation;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.VisualizationRule;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;
import java.util.List;

public class BspWorkerSuperstep extends ConcreteOperationModel {

    public BspWorkerSuperstep() {
        super(GiraphType.BspWorker, GiraphType.Superstep);
    }

    public void loadRules() {
        super.loadRules();
        //addInfoDerivation(new NetworkUtilOperationDerivation(1));
        //addInfoDerivation(new CpuUtilOperationDerivation(1));
        //addInfoDerivation(new MemoryUtilOperationDerivation(1));
        //addVisualDerivation(new NetworkUtilVisualOperationDerivation(1));


//        RecordInfoDerivation sentMsgs = new RecordInfoDerivation(1, "SentMsgs");
//        addInfoDerivation(sentMsgs);
//        sentMsgs.setDescription("[SentMsgs] is the amount of messages sent by a BspWorker in a superstep. " +
//                "This metric is already included in the standard implementation of Giraph. ");
//
//
//        RecordInfoDerivation alterSentMsgs = new RecordInfoDerivation(1, "AlterSentMsgs");
//        addInfoDerivation(alterSentMsgs);
//        alterSentMsgs.setDescription("[AlterSentMsgs]is the amount messages sent by a BspWorker in a superstep. " +
//                "This value is aggregated from AbstractComputation.sendMessage() and AbstractComputation.sendMessageToAllEdges. " +
//                "We need to double check if local messages are counted here or not. " +
//                "So far, we observe that [SentMsgs] and [AlterSentMsgs] are always equal. ");
//
//
//        RecordInfoDerivation sentMsgsVolume = new RecordInfoDerivation(1, "SentMsgsVolume");
//        addInfoDerivation(sentMsgsVolume);
//        sentMsgsVolume.setDescription("[SentMsgsVolume] is the amount of bytes of all sent messages by a BspWorker in a superstep. " +
//        "This value is calculated internally by Giraph. ");
//
//        RecordInfoDerivation receivedMsgs = new RecordInfoDerivation(1, "ReceivedMsgs");
//        addInfoDerivation(receivedMsgs);
//        receivedMsgs.setDescription("[ReceivedMsgs] is the amount messages received by a BspWorker in a superstep. " +
//                "This value is aggregated from SendWorkerMessagesRequest.doRequest() -> ((VertexIdMessages) iterator.getCurrentSecond()).getSize()" +
//                "We need to double check if local messages are going through here or not. ");
//
//        RecordInfoDerivation receivedMsgsVolume = new RecordInfoDerivation(1, "ReceivedMsgsVolume");
//        addInfoDerivation(receivedMsgsVolume);
//        receivedMsgsVolume.setDescription("[ReceivedMsgsVolume] is the amount messages received by a BspWorker in a superstep. " +
//                "This value is aggregated from SendWorkerMessagesRequest.doRequest() -> ((VertexIdMessages) iterator.getCurrentSecond()).getSerializedSize(). " +
//                "We need to double check if local messages are going through here or not. ");
//
//        RecordInfoDerivation allReceivedReqs = new RecordInfoDerivation(1, "AllReceivedReqs");
//        addInfoDerivation(allReceivedReqs);
//        allReceivedReqs.setDescription("[AllReceivedReqs] is all network requests received by a BspWorker in a superstep. " +
//                "This value is aggregated from WorkerRequestServerHandler.processRequest()");
//
//        RecordInfoDerivation receivedMsgReqs = new RecordInfoDerivation(1, "ReceivedMsgReqs");
//        addInfoDerivation(receivedMsgReqs);
//        receivedMsgReqs.setDescription("[ReceivedMsgReqs] is all worker message requests received by a BspWorker in a superstep. " +
//                "This value is aggregated from WorkerRequestServerHandler.processRequest() -> (WritableRequest) request).getType() == RequestType.SEND_WORKER_MESSAGES_REQUEST ");
//
//        RecordInfoDerivation receivedMsgReqsVolume = new RecordInfoDerivation(1, "ReceivedMsgReqsVolume");
//        addInfoDerivation(receivedMsgReqsVolume);
//        receivedMsgReqsVolume.setDescription("[ReceivedMsgReqsVolume] is the volume of all worker message requests received by a BspWorker in a superstep. " +
//                "This value is aggregated from WorkerRequestServerHandler.processRequest() -> (WritableRequest) request).getType() == RequestType.SEND_WORKER_MESSAGES_REQUEST -> .getSerializedSize() ");
//
//        RecordInfoDerivation computeNode = new RecordInfoDerivation(1, "ComputeNode");
//        addInfoDerivation(computeNode);

        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
//                    add("SentMsgs");
//                    add("AlterSentMsgs");
//                    add("ReceivedMsgs");
//
//                    add("SentMsgsVolume");
//                    add("ReceivedMsgsVolume");
//                    add("ReceivedMsgReqsVolume");
//
//                    add("AllReceivedReqs");
//                    add("ReceivedMsgReqs");
//
//                    add("ComputeNode");
                }}));
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation executes each BSP superstep. In each BspWorker, messages received at previous superstep are processed per vertex, and new messages are created in the process." +
                            "The exact functioning of this operation is eh.. still under investigation. (Superstep is too sync-ed to be accurate). " +
                            "This operation starts after %s starts %s, " +
                            "and ends when %s ends %s. " +
                            "This operation does not contain any child operations. ",
                    operation.getName(), operation.getActor().getName(), operation.getMission().getName(), operation.getActor().getName(), operation.getMission().getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }

    protected class MemoryUtilDerivation extends ResourceUtilDerivation {

        public MemoryUtilDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                boolean succeed = true;

                //GiraphType.mem_total

                String[] memUtilMetrics = {GiraphType.mem_buffers, GiraphType.mem_cached, GiraphType.mem_free,
                        GiraphType.mem_shared};

                for (String memUtilMtc : memUtilMetrics) {
                    boolean mapUtilSucceed = mapUtilMetricInfo(operation, memUtilMtc);
                    if(!mapUtilSucceed) { succeed = false; }
                }
                return  true;
        }
    }


    protected class CpuUtilDerivation extends ResourceUtilDerivation {

        public CpuUtilDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                boolean succeed = true;

                String[] cpuUtilMetrics = {GiraphType.cpu_user, GiraphType.cpu_aidle, GiraphType.cpu_idle,
                        GiraphType.cpu_steal, GiraphType.cpu_nice, GiraphType.cpu_wio};

                for (String cpuUtilMtc : cpuUtilMetrics) {
                    boolean mapUtilSucceed = mapUtilMetricInfo(operation, cpuUtilMtc);
                    if(!mapUtilSucceed) { succeed = false; }
                }
                return succeed;
        }
    }


    protected class NetworkUtilDerivation extends ResourceUtilDerivation {

        public NetworkUtilDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                boolean succeed = true;

                String[] networkUtilMetrics = {GiraphType.pkts_in, GiraphType.pkts_out, GiraphType.bytes_in, GiraphType.bytes_out};

                for (String ntwkUtilMtc : networkUtilMetrics) {
                    boolean mapUtilSucceed = mapUtilMetricInfo(operation, ntwkUtilMtc);
                    if(!mapUtilSucceed) { succeed = false; }
                }
                return succeed;
        }
    }


    protected class NetworkUtilVisualOperationDerivationRule extends VisualizationRule {

        public NetworkUtilVisualOperationDerivationRule(int level) {
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
