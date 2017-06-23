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

package science.atlarge.granula.modeller.entity;

import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.util.json.Exclude;

public abstract class Entity extends Archivable {

    @Exclude
    protected Model model;
    @Exclude protected Job job;

    public Entity() {
        super();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        model.setEntity(this);
        model.loadRules();
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void loadRules() {
        model.loadRules();
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ' ' +getName();
    }

}
