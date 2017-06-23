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

package science.atlarge.granula.modeller.rule.filling;

import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.platform.PlatformModel;
import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.platform.Actor;
import science.atlarge.granula.modeller.platform.Mission;

public class UniqueOperationFilling extends FillingRule {

    String actorType;
    String missionType;

    public UniqueOperationFilling(int level, String actorType, String missionType) {
        super(level);
        this.actorType = actorType;
        this.missionType = missionType;

    }

    @Override
    public boolean execute() {
        Job job = entity.getJob();


        Operation operation = new Operation();

        Actor actor = new Actor(actorType, BasicType.Unique);
        if(job.getPlatform().findActor(actor.getName()) != null) {
            actor = job.getPlatform().findActor(actor.getName());
        } else {
            job.getPlatform().addActor(actor);
        }
        operation.setActor(actor);
        actor.addOperation(operation);

        Mission mission = new Mission(missionType,  BasicType.Unique);
        if(job.getPlatform().findMission(mission.getName()) != null) {
            mission = job.getPlatform().findMission(mission.getName());
        } else {
            job.getPlatform().addMission(mission);

        }
        operation.setMission(mission);
        mission.addOperation(operation);

        operation.setMission(mission);
        operation.setActor(actor);
        operation.setPlatform(job.getPlatform());
        operation.setJob(job);
        operation.setModel(((PlatformModel) job.getPlatform().getModel()).getOperationModel(operation.getType()));
        job.getPlatform().addOperation(operation);

        return  true;
    }
}
