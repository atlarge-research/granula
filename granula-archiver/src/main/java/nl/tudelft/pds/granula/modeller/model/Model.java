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

package nl.tudelft.pds.granula.modeller.model;

import nl.tudelft.pds.granula.archiver.entity.Entity;
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.rule.filling.FillingRule;
import nl.tudelft.pds.granula.modeller.rule.visual.VisualizationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 5-2-15.
 */
public abstract class Model {
    protected Entity entity;
    protected List<DerivationRule> infoDerivationRules;
    protected List<VisualizationRule> visualDerivationRules;
    protected List<FillingRule> fillingRules;

    public Model() {
        infoDerivationRules = new ArrayList<>();
        visualDerivationRules = new ArrayList<>();
        fillingRules = new ArrayList<>();
    }

    public void addInfoDerivation(DerivationRule derivationRule) {
        infoDerivationRules.add(derivationRule);
        derivationRule.setEntity(entity);
    }

    public void addVisualDerivation(VisualizationRule visualizationRule) {
        visualDerivationRules.add(visualizationRule);
        visualizationRule.setEntity(entity);
    }


    public void addFillingRule(FillingRule fillingRule) {
        fillingRules.add(fillingRule);
        fillingRule.setEntity(entity);
    }

    public List<FillingRule> getFillingRules() {
        return fillingRules;
    }

    public List<DerivationRule> getInfoDerivationRules() {
        return infoDerivationRules;
    }

    public abstract void loadRules();

    public List<VisualizationRule> getVisualDerivationRules() {
        return visualDerivationRules;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
