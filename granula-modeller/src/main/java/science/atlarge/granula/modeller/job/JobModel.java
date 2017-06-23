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

package science.atlarge.granula.modeller.job;

import science.atlarge.granula.modeller.entity.Model;
import science.atlarge.granula.modeller.platform.PlatformModel;

public class JobModel extends Model {
    PlatformModel platformModel;

    public JobModel(PlatformModel platformModel) {
        this.platformModel = platformModel;
    }

    public PlatformModel getPlatformModel() {
        return platformModel;
    }

    public void setPlatformModel(PlatformModel platformModel) {
        this.platformModel = platformModel;
    }

    @Override
    public void loadRules() {

    }
}
