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

package science.atlarge.granula.modeller.rule.assembling;

import science.atlarge.granula.modeller.source.log.Log;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.source.log.LogInfo;

import java.util.ArrayList;
import java.util.List;

public class BasicAssembling extends AssemblingRule {

    public BasicAssembling() {
        super();
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        List<Log> candidateLogs = new ArrayList<>();

        for (Log log : operation.getJob().getPlatform().getLogs()) {
            candidateLogs.add(log);
        }

        for (Log candidateLog : candidateLogs) {
            boolean actorMatched = candidateLog.getAttr(LogInfo.ActorType).equals(operation.getActor().getType());
            boolean actorIdMatched = candidateLog.getAttr(LogInfo.ActorId).equals(operation.getActor().getId());
            boolean missionMatched = candidateLog.getAttr(LogInfo.MissionType).equals(operation.getMission().getType());
            boolean missionIdMatched = candidateLog.getAttr(LogInfo.MissionId).equals(operation.getMission().getId());

            if (actorMatched && actorIdMatched && missionMatched && missionIdMatched) {
                operation.addLog(candidateLog);
            }
        }
        return true;
    }
}
