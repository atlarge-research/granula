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

package nl.tudelft.pds.granula.modeller.mapreducev2.operation;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.mapreducev2.MapReduceV2Type;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.*;
import nl.tudelft.pds.granula.modeller.rule.linking.IdentifierParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;

import java.util.ArrayList;

public class AppMasterExecution extends ConcreteOperationModel {

    public AppMasterExecution() {
        super(MapReduceV2Type.AppMaster, MapReduceV2Type.Execution);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new IdentifierParentLinking(MapReduceV2Type.MRApp, Identifier.Equal, MapReduceV2Type.MRJob, Identifier.Unique));

//        addInfoDerivation(new RecordInfoDerivation(1, "StackTrace"));
//        addInfoDerivation(new DurationDerivation(3));
        addInfoDerivation(new SummaryDerivation(10));
        addInfoDerivation(new ActorIdShortenerDerivation(4));

        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
//                    add("InputMethod");
//                    add("RunTime");
//                    add("InputSize");
//                    add("InputMethod");
//                    add("ShuffleRead");
//                    add("ShuffleWrite");
                }}));
    }


    protected class ActorIdShortenerDerivation extends DerivationRule {

        public ActorIdShortenerDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String actorId = operation.getActor().getId();

            String shortenedId = actorId.substring(0, 2) + ".." + actorId.substring(actorId.length() - 8, actorId.length());
            operation.getActor().setId(Identifier.Unique);

            return  true;
        }
    }
    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation contains a set of independent tasks all computing the same " +
                        "function that need to run as part of a Spark job, where all the tasks have the same shuffle dependencies. " +
                        "Each DAG of tasks run by the scheduler is split up into stages at the boundaries where shuffle occurs, " +
                        "and then the DAGScheduler runs these stages in topological order. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }

}
