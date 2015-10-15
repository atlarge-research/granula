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

package nl.tudelft.pds.granula.modeller.model.job;

import nl.tudelft.pds.granula.modeller.model.Model;
import nl.tudelft.pds.granula.modeller.model.operation.OperationModel;
import nl.tudelft.pds.granula.modeller.rule.extraction.ExtractionRule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wing on 12-3-15.
 */
public abstract class JobModel extends Model {
    Map<String, OperationModel> operationModelMap;
    protected List<ExtractionRule> extractionRules;

    public JobModel() {
        this.operationModelMap = new LinkedHashMap<String, OperationModel>();
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

//    public boolean deriveInfos(Job job, int derivationLevel) {
//        boolean allDerivationDone = true;
//        for (Derivation derivation : infoDerivations) {
//            allDerivationDone = derivation.execute(job, derivationLevel) ? allDerivationDone : false;
//        }
//        return allDerivationDone;
//    }

    public Map<String, OperationModel> getOperationModelMap() {
        return operationModelMap;
    }

    public void addOperationModel(OperationModel operationModel) {
        this.operationModelMap.put(operationModel.getIdentifier(), operationModel);
    }

    public OperationModel getOperationModel(String operationType) {
        try {
            return operationModelMap.get(operationType).getClass().newInstance();
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
