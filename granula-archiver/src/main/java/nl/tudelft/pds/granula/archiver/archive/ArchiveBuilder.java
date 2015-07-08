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

package nl.tudelft.pds.granula.archiver.archive;

import nl.tudelft.pds.granula.archiver.entity.Entity;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.VisualizationRule;

import java.util.*;

public class ArchiveBuilder {

    public void build(Job job) {


        List<Operation> operationList = job.getMemberOperations();

        List<DerivationRule> infoDerivationRules = new ArrayList<>();
        List<VisualizationRule> visualDerivationRules = new ArrayList<>();

        List<Entity> entities = new ArrayList<>();
        entities.add(job);
        for (Operation operation : operationList) {
            entities.add(operation);
        }

        for (Entity entity : entities) {
            for (DerivationRule derivationRule : entity.getModel().getInfoDerivationRules()) {
                infoDerivationRules.add(derivationRule);
            }
            for (VisualizationRule visualizationRule : entity.getModel().getVisualDerivationRules()) {
                visualDerivationRules.add(visualizationRule);
            }
        }

        Collections.sort(infoDerivationRules);
        Collections.sort(visualDerivationRules);

        for (DerivationRule infoDerivationRule : infoDerivationRules) {
            infoDerivationRule.execute();
        }

        for (VisualizationRule visualDerivationRule : visualDerivationRules) {
            visualDerivationRule.execute();
        }

    }

}
