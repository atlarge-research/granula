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

package nl.tudelft.pds.granula.modeller.model.operation;

import nl.tudelft.pds.granula.archiver.entity.Identifier;
import nl.tudelft.pds.granula.modeller.rule.derivation.ColorDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.UuidOperationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.EndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.IncompleteEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.StartTimeDerivation;

public class IncompleteConcreteOperationModel extends OperationModel {

    public IncompleteConcreteOperationModel(String actorType, String missionType) {
        super(actorType, missionType);
    }


    public void loadRules() {
        super.loadRules();
        addInfoDerivation(new StartTimeDerivation(1));
        addInfoDerivation(new IncompleteEndTimeDerivation(1));
        addInfoDerivation(new DurationDerivation(1));
        addInfoDerivation(new ColorDerivation(1, Identifier.ColorBlue));
        addInfoDerivation(new UuidOperationDerivation(1));
    }



}
