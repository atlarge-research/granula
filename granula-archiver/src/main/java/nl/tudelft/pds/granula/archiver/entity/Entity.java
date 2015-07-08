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

package nl.tudelft.pds.granula.archiver.entity;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.visual.Visual;
import nl.tudelft.pds.granula.modeller.fundamental.model.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wing on 5-2-15.
 */
public abstract class Entity extends Archivable {

    protected Map<String, Info> infos;
    protected Map<String, Visual> visuals;
    public Model model;

    public Entity() {
        super();
        this.infos = new LinkedHashMap<>();
        this.visuals = new LinkedHashMap<>();
    }

    public void addInfo(Info info) {
        infos.put(info.getName(), info);
    }

    public List<Info> getInfos() {
        return new ArrayList<>(infos.values());
    }

    public Info getInfo(String name) {
        if(infos.containsKey(name)) {
            return infos.get(name);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public boolean hasInfo(String name) {
        return infos.containsKey(name);
    }

    public void addVisual(Visual visual) {
        visuals.put(visual.getName(), visual);
    }

    public List<Visual> getVisuals() { return new ArrayList<>(visuals.values()); }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        model.setEntity(this);
        model.loadRules();
    }

    public void loadRules() {
        model.loadRules();
    }

    protected String exportInfos() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<Infos>");
        for (Info info : getInfos()) {
            stringBuilder.append(info.export());
        }
        stringBuilder.append("</Infos>");

        return stringBuilder.toString();
    }

    protected String exportVisuals() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<Visuals>");
        for (Visual visual : getVisuals()) {
            stringBuilder.append(visual.export());
        }
        stringBuilder.append("</Visuals>");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ' ' +getName();
    }

}
