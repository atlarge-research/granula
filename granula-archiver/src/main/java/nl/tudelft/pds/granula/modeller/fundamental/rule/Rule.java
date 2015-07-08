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

package nl.tudelft.pds.granula.modeller.fundamental.rule;

import nl.tudelft.pds.granula.archiver.entity.Entity;

public abstract class Rule implements Comparable<Rule> {
    int level;
    protected Entity entity;

    public Rule(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public abstract boolean execute();

    @Override
    public int compareTo(Rule other){
        if (this.getLevel() > other.getLevel()) {
            return 1;
        } else if (other.getLevel() > this.getLevel()) {
            return -1;
        } else {
            return 0;
        }
    }


    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
