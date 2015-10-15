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

package nl.tudelft.pds.granula.modeller.rule.linking;

import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.mapreducev2.MapReduceV2Type;

import java.util.ArrayList;
import java.util.List;

public class StageParentLinking extends LinkingRule {

    String parentActorType;
    String parentMissionType;
    String subActorType;
    String subMissionType;

    public StageParentLinking(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        String stackTrace = operation.getRecord("StackTrace").getAttr("InfoValue");

        String computeMethod = "compute(";
        String graphLoaderMethod = "nl.tudelft.graphalytics.graphx.GraphLoader";
        String preProcessMethod = "preprocess(";

        String pregelMethod = "org.apache.spark.graphx.GraphOps.pregel";
        String pregelMathod2 = "org.apache.spark.graphx.Pregel";

        Operation superSuperoperation = null;
        Operation superSuboperation = null;

        if(stackTrace.contains(computeMethod)) {

            if(stackTrace.contains(pregelMethod) | stackTrace.contains(pregelMathod2)) {

                parentActorType = MapReduceV2Type.Coordinator;
                parentMissionType = MapReduceV2Type.Superstep;

                superSuperoperation = getSuperSuperOperation(operation, parentActorType, parentMissionType);
                if(superSuperoperation == null) {
                    return false;
                }

                String graphUpdateMethod = "org.apache.spark.graphx.impl.GraphImpl.outerJoinVertices";
                String graphConstructorMethod = "org.apache.spark.graphx.impl.GraphImpl.mapVertices";
                String msgReceiveMethod = "asdfcvas";
                String msgSendMethod = "org.apache.spark.graphx.impl.GraphImpl.mapReduceTriplets";
                String msgCountMethod = "org.apache.spark.graphx.VertexRDD.count";


                if(stackTrace.contains(graphUpdateMethod)) {
                    superSuboperation = getSubOperation(superSuperoperation, MapReduceV2Type.GraphUpdater, MapReduceV2Type.GraphUpdate);
                } else if (stackTrace.contains(graphConstructorMethod)) {
                    superSuboperation = getSubOperation(superSuperoperation, MapReduceV2Type.GraphConstructor, MapReduceV2Type.GraphConstruct);
                } else if (stackTrace.contains(msgReceiveMethod)) {
                    superSuboperation = getSubOperation(superSuperoperation, MapReduceV2Type.MsgReceiver, MapReduceV2Type.MsgReceive);
                } else if (stackTrace.contains(msgSendMethod)) {
                    superSuboperation = getSubOperation(superSuperoperation, MapReduceV2Type.MsgSender, MapReduceV2Type.MsgSend);
                } else if (stackTrace.contains(msgCountMethod)) {
                    superSuboperation = getSubOperation(superSuperoperation, MapReduceV2Type.MsgCounter, MapReduceV2Type.MsgCount);
                } else {
                    System.out.println("Exception? in Stage ParentLinking");
                }
            } else if(stackTrace.contains(preProcessMethod)) {
                superSuboperation = operation.getJob().findOperation(MapReduceV2Type.Coordinator, MapReduceV2Type.Setup);
            } else {
                superSuboperation = operation.getJob().findOperation(MapReduceV2Type.SparkApplication, MapReduceV2Type.Execution);
            }
        } else if(stackTrace.contains(graphLoaderMethod)) {
            for (Operation candidate : operation.getJob().getMemberOperations()) {
                if (candidate.hasType(MapReduceV2Type.Coordinator, MapReduceV2Type.Setup)) {
                    superSuboperation = candidate;
                }
            }
        } else {
            for (Operation candidate : operation.getJob().getMemberOperations()) {
                if (candidate.hasType(MapReduceV2Type.Coordinator, MapReduceV2Type.Cleanup)) {
                    superSuboperation = candidate;
                }
            }

        }






        Operation parent = superSuboperation;
        operation.setParent(parent);
        parent.addChild(operation);
        return  true;
    }


    private Operation getSubOperation(Operation operation, String subActorType, String subMissionType) {
        List<Operation> matchedParents = new ArrayList<>();

        for (Operation candidateOperation : operation.getChildren()) {
            boolean actorMatched = candidateOperation.getActor().getType().equals(subActorType);
            boolean missionMatched = candidateOperation.getMission().getType().equals(subMissionType);

            if (actorMatched && missionMatched) {
                matchedParents.add(candidateOperation);
            }
        }

        if(matchedParents.size() != 1) {
            System.out.print("");
            //throw new IllegalStateException();
        }

        return matchedParents.get(0);
    }

    private Operation getSuperSuperOperation(Operation operation, String parentActorType, String parentMissionType) {
        List<Operation> matchedParents = new ArrayList<>();

        for (Operation candidateOperation : operation.getJob().getMemberOperations()) {

            boolean actorMatched = candidateOperation.getActor().getType().equals(parentActorType);
            boolean missionMatched = candidateOperation.getMission().getType().equals(parentMissionType);

            if (actorMatched && missionMatched) {
                matchedParents.add(candidateOperation);
            }
        }

        long myLogLine = Long.parseLong(operation.getRecord("StartTime").getRecordLocation().getLineNumber());
        long cloestDistance = Long.MAX_VALUE;
        Operation containingParent = null;

        for (Operation matchedParent : matchedParents) {
            long parentStartTimeLogLine = Long.parseLong(matchedParent.getRecord("StartTime").getRecordLocation().getLineNumber());
            long parentEndTimeLogLine = Long.parseLong(matchedParent.getRecord("EndTime").getRecordLocation().getLineNumber());
            if(parentStartTimeLogLine <= myLogLine && parentEndTimeLogLine >= myLogLine) {
                    containingParent = matchedParent;
            }
        }

        if(matchedParents.size() == 0) {
            System.out.print("");
            //throw new IllegalStateException();
        }

        return containingParent;
    }
}
