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

package science.atlarge.granula.modeller.platform.operation;

import science.atlarge.granula.modeller.entity.Entity;
import science.atlarge.granula.modeller.entity.Model;
import science.atlarge.granula.modeller.rule.assembling.AssemblingRule;
import science.atlarge.granula.modeller.rule.assembling.BasicAssembling;
import science.atlarge.granula.modeller.rule.filling.FillingRule;
import science.atlarge.granula.modeller.rule.linking.LinkingRule;

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
//        addInfoDerivation(new BasicSummaryDerivation(10));
//        addInfoDerivation(new UnusedLogInfoDerivation(2));
//        addVisualDerivation(new SummaryVisualization(1));
        //addVisualDerivation(new AllBasicInfoTableVisualOperationDerivation(1));
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