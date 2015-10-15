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

package nl.tudelft.pds.granula.modeller.rule.derivation;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Actor;
import nl.tudelft.pds.granula.archiver.entity.operation.Mission;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BasicSummaryDerivation extends DerivationRule {

    public BasicSummaryDerivation(int level) { super(level); }

    @Override
    public boolean execute() {

        Operation operation = (Operation) entity;
        String summary = "";
        String tauntMessage = "Unfortunately the fellow who created this operation model, was too lazy to make a more decriptive summary.";

        summary += getBasicSummary(operation);
        summary += tauntMessage;

        SummaryInfo summaryInfo = new SummaryInfo("Summary");
        summaryInfo.setValue("A basic summary");
        summaryInfo.addSummary(summary, new ArrayList<Source>());
        operation.addInfo(summaryInfo);
        return  true;
    }

    public String getBasicSummary(Operation operation) {

        Info startTimeInfo = operation.getInfo("StartTime");
        long startTime = Long.parseLong(startTimeInfo.getValue());
        Info endTimeInfo = operation.getInfo("EndTime");
        long endTime = Long.parseLong(endTimeInfo.getValue());
        Info durationInfo = operation.getInfo("Duration");
        long duration = Long.parseLong(durationInfo.getValue());

        String summary = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        summary += String.format("This operation starts at %s (epoch=%s%s) and ends at %s (epoch=%s%s)",
                dateFormat.format(new Date(startTime)), startTime, getSourceString(startTimeInfo),
                dateFormat.format(new Date(endTime)), endTime, getSourceString(endTimeInfo));
        summary += String.format(", takes in total %.2f sec (%s ms %s). ", duration / 1000.0f, duration, getSourceString(durationInfo));
        return summary;
    }

    public String getSourceString(Info info) {
        return "[{" + info.getUuid() + "}]";
    }

    public String getOperationTitle(Operation operation) {
        Actor actor = operation.getActor();
        Mission mission = operation.getMission();

        String actorText = (actor.getId().startsWith("Id.")) ? actor.getType() : actor.getType() + "-" + actor.getId();
        String missionText = (mission.getId().startsWith("Id.")) ? mission.getType() : mission.getType() + "-" + mission.getId();

        return String.format("Operation [%s, %s]", actorText, missionText);
    }
}