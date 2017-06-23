/*
 * Copyright 2015 - 2017 Atlarge Research Team,
 * operating at Technische Universiteit Delft
 * and Vrije Universiteit Amsterdam, the Netherlands.
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

package science.atlarge.granula.modeller.rule.derivation;

import science.atlarge.granula.modeller.platform.info.SummaryInfo;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.platform.info.Source;

import java.util.ArrayList;

public class EmptySummaryDerivation extends DerivationRule {

    public EmptySummaryDerivation(int level) { super(level); }

    @Override
    public boolean execute() {

        Operation operation = (Operation) entity;
        String summary = "";

        SummaryInfo summaryInfo = new SummaryInfo("Summary");
        summaryInfo.setValue("A basic summary");
        summaryInfo.addSummary(summary, new ArrayList<Source>());
        operation.addInfo(summaryInfo);
        return  true;
    }
}