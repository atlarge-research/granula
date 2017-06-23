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

import science.atlarge.granula.modeller.platform.info.BasicInfo;
import science.atlarge.granula.modeller.platform.info.Source;
import science.atlarge.granula.modeller.platform.operation.Operation;

import java.util.ArrayList;

public class FilialCompletenessDerivation extends DerivationRule {

    public FilialCompletenessDerivation(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        BasicInfo info = new BasicInfo("Completeness");
        boolean isCompleted = true;

        for (Operation subOper : operation.getChildren()) {
            String completed = subOper.getInfo("Completeness").getValue();
            if(!Boolean.parseBoolean(completed)) {
                isCompleted = false;
            }
        }

        info.addInfo(String.valueOf(isCompleted), new ArrayList<Source>());
        operation.addInfo(info);

        return  true;
    }
}
