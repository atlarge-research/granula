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

import science.atlarge.granula.modeller.rule.derivation.CompletenessDerivation;
import science.atlarge.granula.modeller.rule.derivation.time.DurationDerivation;
import science.atlarge.granula.modeller.rule.derivation.time.UnfoundEndTimeDerivation;
import science.atlarge.granula.modeller.rule.derivation.time.StartTimeDerivation;

public class RealtimeOperationModel extends OperationModel {

    public RealtimeOperationModel(String actorType, String missionType) {
        super(actorType, missionType);
    }


    public void loadRules() {
        super.loadRules();
        addInfoDerivation(new CompletenessDerivation(0));
        addInfoDerivation(new StartTimeDerivation(1));
        addInfoDerivation(new UnfoundEndTimeDerivation(1));
        addInfoDerivation(new DurationDerivation(2));
    }



}
