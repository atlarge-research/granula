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

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;
import java.util.List;

public class BspMasterBspCleanup extends ConcreteOperationModel {

    public BspMasterBspCleanup() {
        super(GiraphType.BspMaster, GiraphType.BspCleanup);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(GiraphType.AppMaster, GiraphType.BspExecution));
        addInfoDerivation(new ColorDerivation(1, GiraphType.ColorGrey));
        addInfoDerivation(new DataOffloadTimeDerivation(2));
        addInfoDerivation(new SummaryDerivation(10));

        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("DataOffloadTime");
                }}));

    }


    protected class DataOffloadTimeDerivation extends DerivationRule {

        public DataOffloadTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            List<Source> sources = new ArrayList<>();
            List<Info> usedInfos = new ArrayList<>();

            long maxDuration = Long.MIN_VALUE;

            for(Operation suboperation: operation.getChildren()) {
                if(suboperation.hasType(GiraphType.BspWorker, GiraphType.DataOffload)) {
                    Info dataOffloadDuration = suboperation.getInfo("Duration");
                    usedInfos.add(dataOffloadDuration);
                    maxDuration = Math.max(maxDuration, Long.parseLong(dataOffloadDuration.getValue()));
                }
            }
            sources.add(new InfoSource("DataOffloadDuration", usedInfos));

            Info dataOffloadTime = new Info("DataOffloadTime");
            dataOffloadTime.setDescription(
                    String.format("The [%s] is the maximum of duration all DataOffload operations. ",
                            dataOffloadTime.getName()));

            dataOffloadTime.addInfo(String.valueOf(maxDuration), sources);
            operation.addInfo(dataOffloadTime);

            return  true;

        }
    }



    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation cleans up the BspExecution operation after all supersteps are done, " +
                    "which involves ServerCleanup, ClientCleanup, DataOffload, FinalCleanup, ZookeeperOfflining etc. " +
                    "The exact sychronization barrier of this operation is yet unclear", operation.getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }

}
