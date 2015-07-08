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
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.RecordInfoDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.fundamental.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.filling.UniqueOperationFilling;
import nl.tudelft.pds.granula.modeller.fundamental.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.util.ArrayList;

public class AppMasterAppStartup extends ConcreteOperationModel {

    public AppMasterAppStartup() {
        super(GiraphType.AppMaster, GiraphType.AppStartup);
    }

    public void loadRules() {
        super.loadRules();
        addFillingRule(new UniqueOperationFilling(1, GiraphType.TopActor, GiraphType.TopMission));
        addLinkingRule(new UniqueParentLinking(GiraphType.AppMaster, GiraphType.Deployment));

        addInfoDerivation(new RecordInfoDerivation(1, "ResponseTime"));
        addInfoDerivation(new ColorDerivation(1, GiraphType.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("ResponseTime");
                }}));
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation set up the AppMaster and starts to request Yarn Containers required for the Bsp Execution. " +
                    "This operation starts after AppMaster starts operating, " +
                    "and ends when AppMaster being notified that all containers requests are granted. ",
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
