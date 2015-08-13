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

package nl.tudelft.pds.granula.modeller.mapreducev2.operation.old;

import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.FilialStringDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.mapreducev2.MapReduceV2Type;

import java.util.ArrayList;

public class SparkAppDeployment extends AbstractOperationModel {

    public SparkAppDeployment() {
        super(MapReduceV2Type.SparkApplication, MapReduceV2Type.Deployment);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new UniqueParentLinking(MapReduceV2Type.TopActor, MapReduceV2Type.TopMission));

        addInfoDerivation(new FilialStringDerivation(2, MapReduceV2Type.AppStartup, "ApplicationName"));
        addInfoDerivation(new FilialStringDerivation(2, MapReduceV2Type.AppStartup, "ExecutorMemory"));
        addInfoDerivation(new FilialStringDerivation(2, MapReduceV2Type.AppStartup, "DataInputPath"));
        addInfoDerivation(new FilialStringDerivation(2, MapReduceV2Type.ExecutorAssignment, "ExecutorSize"));

        addInfoDerivation(new FilialStartTimeDerivation(2));
        addInfoDerivation(new FilialEndTimeDerivation(2));
        addInfoDerivation(new DurationDerivation(3));

        addInfoDerivation(new ColorDerivation(1, MapReduceV2Type.ColorGrey));
        addInfoDerivation(new SummaryDerivation(10));

        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add("ApplicationName");
                    add("DataInputPath");
                    add("ExecutorMemory");
                    add("ExecutorSize");
                }}));
    }


    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation consists of Setup, BspIteration and Cleanup. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }

}
