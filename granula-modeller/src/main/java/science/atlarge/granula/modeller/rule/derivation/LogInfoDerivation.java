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
import science.atlarge.granula.modeller.platform.info.LogSource;
import science.atlarge.granula.modeller.platform.info.Source;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.source.log.Log;
import science.atlarge.granula.modeller.source.log.LogInfo;

import java.util.ArrayList;
import java.util.List;

public class LogInfoDerivation extends DerivationRule {

    String infoName;
    String description;
    String logInfoName;

    public LogInfoDerivation(int level, String infoName) {
        super(level);
        this.infoName = infoName;
        this.logInfoName = infoName;
    }

    public LogInfoDerivation(int level, String logInfoName, String infoName) {
        super(level);
        this.infoName = infoName;
        this.logInfoName = logInfoName;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;
        Log log = operation.getLog(logInfoName);
        BasicInfo info = new BasicInfo(infoName);
        List<Source> sources = new ArrayList<>();
        sources.add(new LogSource(log.getAttr(LogInfo.InfoName), log));
        info.addInfo(log.getAttr(LogInfo.InfoValue), sources);
        info.setDescription("The information is extracted directed from its source log.");
        if(description != null) {
            info.setDescription(description);
        }
        operation.addInfo(info);
        return  true;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}