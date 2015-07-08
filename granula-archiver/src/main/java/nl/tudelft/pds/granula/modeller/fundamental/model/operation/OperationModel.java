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

package nl.tudelft.pds.granula.modeller.fundamental.model.operation;

import nl.tudelft.pds.granula.ArchiverConfiguration;
import nl.tudelft.pds.granula.archiver.entity.Entity;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.fundamental.model.Model;
import nl.tudelft.pds.granula.modeller.fundamental.rule.assembling.AssemblingRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.assembling.BasicAssembling;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.derivation.EmptySummaryDerivation;
import nl.tudelft.pds.granula.modeller.fundamental.rule.filling.FillingRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.linking.LinkingRule;
import nl.tudelft.pds.granula.modeller.fundamental.rule.visual.SummaryVisualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 5-2-15.
 */
public abstract class OperationModel extends Model {

    String actorType;
    String missionType;
    String identifier;

    protected List<AssemblingRule> assemblingRules;
    protected List<LinkingRule> linkingRules;
    protected List<FillingRule> fillingRules;


    public OperationModel(String actorType, String missionType) {
        super();
        this.actorType = actorType;
        this.missionType = missionType;
        setIdentifier(String.format("%s-%s", actorType, missionType));
        assemblingRules = new ArrayList<>();
        fillingRules = new ArrayList<>();
        linkingRules = new ArrayList<>();
    }


    public void loadRules() {
        addAssemblingRule(new BasicAssembling());
//        addLinkingRule(new BasicLinkingRule());
        addInfoDerivation(new BasicSummaryDerivation(10));
//        addInfoDerivation(new UnusedRecordInfoDerivation(2));
        addVisualDerivation(new SummaryVisualization(1));
        //addVisualDerivation(new AllBasicInfoTableVisualOperationDerivation(1));
        if(!ArchiverConfiguration.ExportDescription) {
            addInfoDerivation(new EmptySummaryDerivation(11));
        }
    }


    public void setEntity(Entity entity) {
        if (!(entity instanceof Operation)) {
            throw new IllegalStateException();
        }
        this.entity = entity;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public void addAssemblingRule(AssemblingRule assemblingRule) {
        assemblingRules.add(assemblingRule);
        assemblingRule.setEntity(entity);
    }

    public List<AssemblingRule> getAssemblingRules() {
        return assemblingRules;
    }


    public void addFillingRule(FillingRule fillingRule) {
        fillingRules.add(fillingRule);
        fillingRule.setEntity(entity);
    }

    public List<FillingRule> getFillingRules() {
        return fillingRules;
    }

    public void addLinkingRule(LinkingRule linkingRule) {
        linkingRules.add(linkingRule);
        linkingRule.setEntity(entity);
    }

    public List<LinkingRule> getLinkingRules() {
        return linkingRules;
    }

}