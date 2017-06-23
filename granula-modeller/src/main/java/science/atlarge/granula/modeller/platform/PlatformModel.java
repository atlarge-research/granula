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

package science.atlarge.granula.modeller.platform;

import science.atlarge.granula.modeller.entity.Model;
import science.atlarge.granula.modeller.platform.operation.OperationModel;
import science.atlarge.granula.modeller.rule.extraction.ExtractionRule;

import java.lang.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class PlatformModel extends Model {
    Map<String, OperationModel> operationModels;
    protected List<ExtractionRule> extractionRules;

    public PlatformModel() {
        this.operationModels = new LinkedHashMap<>();
        infoDerivationRules = new ArrayList<>();
        extractionRules = new ArrayList<>();
    }

    public void addExtraction(ExtractionRule extractionRule) {
        extractionRules.add(extractionRule);
        extractionRule.setEntity(entity);
    }

    public List<ExtractionRule> getExtractionRules() {
        return extractionRules;
    }

    public abstract void loadRules();

    public void addOperationModel(OperationModel operationModel) {
        this.operationModels.put(operationModel.getIdentifier(), operationModel);
    }

    public OperationModel getOperationModel(String operationType) {
        try {
            return operationModels.get(operationType).getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            System.out.println(String.format("Cannot find operation type: %s", operationType));
            throw new IllegalStateException();
        }
    }

    public void unloadRules() {
        infoDerivationRules = new ArrayList<>();
        extractionRules = new ArrayList<>();
        fillingRules = new ArrayList<>();
    }

}
