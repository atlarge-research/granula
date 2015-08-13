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
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.rule.derivation.RecordInfoDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.IdentifierParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;
import java.util.List;

public class BspWorkerPostSuperstep extends ConcreteOperationModel {

    public BspWorkerPostSuperstep() {
        super(GiraphType.BspWorker, GiraphType.PostSuperstep);
    }

    public void loadRules() {
        super.loadRules();
        addLinkingRule(new IdentifierParentLinking(GiraphType.GlobalCoordinator, GiraphType.Unique, GiraphType.GlobalSuperstep, GiraphType.Equal));
        addInfoDerivation(new ColorDerivation(1, GiraphType.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));
        addRecordInfos();
        addInfoDerivation(new LocalMsgDerivation(2));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("SentMsgs");
                    add("SentMsgVolume");
                    add("ReceivedMsgVolume");
                    add("LocalMsgVolume");
                    add("RemoteMsgVolume");

//                    add("ComputeNode");
                }}));
        addVisualDerivation(new TableVisualization(1, "ActivenessTable",
                new ArrayList<String>() {{
                    add("ActiveVertices");
                }}));
    }


    public void addRecordInfos() {

        RecordInfoDerivation sentMsgs = new RecordInfoDerivation(1, "SentMsgs", "SentMsgs");
        addInfoDerivation(sentMsgs);
        sentMsgs.setDescription("[SentMsgs] is the amount messages sent by a BspWorker in a superstep. ");

        RecordInfoDerivation allSentMsgReqsVolume = new RecordInfoDerivation(1, "SentMsgVolume", "SentMsgVolume");
        addInfoDerivation(allSentMsgReqsVolume);
        allSentMsgReqsVolume.setDescription("[RemoteMsgReqsVolume] is the volume of all worker message requests sent remotely by a BspWorker in a superstep. ");

        RecordInfoDerivation receivedMsgReqsVolume = new RecordInfoDerivation(1, "ReceivedMsgVolume", "ReceivedMsgVolume");
        addInfoDerivation(receivedMsgReqsVolume);
        receivedMsgReqsVolume.setDescription("[ReceivedMsgReqsVolume] is the volume of all worker message requests received by a BspWorker in a superstep.");

        RecordInfoDerivation remoteMsgReqsVolume = new RecordInfoDerivation(1, "RemoteMsgVolume", "RemoteMsgVolume");
        addInfoDerivation(remoteMsgReqsVolume);
        receivedMsgReqsVolume.setDescription("[RemoteMsgReqsVolume] is the volume of all worker message requests sent remotely by a BspWorker in a superstep. ");

        RecordInfoDerivation activeVertices = new RecordInfoDerivation(1, "ActiveVertices", "ActiveVertices");
        addInfoDerivation(activeVertices);
        receivedMsgReqsVolume.setDescription("[ActiveVertices] is the number of vertices that are executed during the superstep. ");

//        RecordInfoDerivation computeNode = new RecordInfoDerivation(1, "ComputeNode");
//        addInfoDerivation(computeNode);
    }



    protected class LocalMsgDerivation extends DerivationRule {

        public LocalMsgDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            Info sentMsgVolInfo = operation.getInfo("SentMsgVolume");
            Info remoteMsgVolInfo = operation.getInfo("RemoteMsgVolume");

            List<Source> sources = new ArrayList<>();
            sources.add(new InfoSource("SentMsgVolume", sentMsgVolInfo));
            sources.add(new InfoSource("RemoteMsgVolume", remoteMsgVolInfo));

            long localMsgVol = Long.parseLong(sentMsgVolInfo.getValue()) - Long.parseLong(remoteMsgVolInfo.getValue());

            BasicInfo localMsgInfo = new BasicInfo("LocalMsgVolume");
            localMsgInfo.setDescription(String.format("[%s] is derived from [%s] - [%s]", "LocalMsgVolume", "SentMsgVolume", "RemoteMsgVolume"));
            localMsgInfo.addInfo(String.valueOf(localMsgVol), sources);
            operation.addInfo(localMsgInfo);
            return  true;
        }
    }


    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation is executed after the Computation and the MessageSend operations in each superstep, " +
                    "which involves writing finished superstep info into Zookeeper, and " +
                    "waiting for other workers to finish. ", operation.getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }

}
