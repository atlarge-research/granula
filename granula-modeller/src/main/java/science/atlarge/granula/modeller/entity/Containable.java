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

import science.atlarge.granula.modeller.platform.info.Info;
import science.atlarge.granula.modeller.platform.visual.Visual;
import science.atlarge.granula.util.json.Exclude;

import java.util.*;

/**
 * Created by wing on 5-2-15.
 */
public abstract class Containable extends Entity {


    public Map<String, String> infoIds;
    public Map<String, String> visualIds;
    @Exclude protected Map<String, Info> infos;
    @Exclude protected Map<String, Visual> visuals;

    public Containable() {
        super();
        this.infos = new LinkedHashMap<>();
        this.visuals = new LinkedHashMap<>();
        this.infoIds = new HashMap<>();
        this.visualIds = new HashMap<>();
    }

    public void addInfo(Info info) {
        infos.put(info.getName(), info);
        infoIds.put(info.getName(), info.getUuid());

        getJob().getPlatform().addInfo(info);
    }

    public List<Info> getInfos() {
        return new ArrayList<>(infos.values());
    }

    public Info getInfo(String name) {
        if(infos.containsKey(name)) {
            return infos.get(name);
        }
        else {
            throw new IllegalStateException(String.format("%s does not contain info %s.", toString(), name));
        }
    }

    public boolean hasInfo(String name) {
        return infos.containsKey(name);
    }

    public void addVisual(Visual visual) {
        visuals.put(visual.getName(), visual);
        visualIds.put(visual.getName(), visual.getUuid());

        getJob().getPlatform().addVisual(visual);
    }

    public List<Visual> getVisuals() { return new ArrayList<>(visuals.values()); }

    public void loadRules() {
        model.loadRules();
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ' ' +getName();
    }

}
