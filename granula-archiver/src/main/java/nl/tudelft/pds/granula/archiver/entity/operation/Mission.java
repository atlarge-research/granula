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

package nl.tudelft.pds.granula.archiver.entity.operation;

import nl.tudelft.pds.granula.archiver.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Mission extends Entity {

    String id;

    Mission parent;
    List<Mission> children;
    List<Operation> operations;

    public Mission(String type, String id) {
        super();
        this.type = type;
        this.id = id;
        this.name = (id.startsWith("Id.")) ? type : type + '-' + id;
        children = new ArrayList<>();
        operations = new ArrayList<>();
    }

    public String exportBasic() {
        return String.format("<Mission name=\"%s\" type=\"%s\" id=\"%s\" uuid=\"%s\" />", name, type, id, uuid);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Mission name=\"%s\" type=\"%s\" id=\"%s\" uuid=\"%s\">", name, type, id, uuid));

        stringBuilder.append(exportInfos());
        stringBuilder.append(exportVisuals());

        stringBuilder.append("<Operations>");
        for (Operation operation : getOperations()) {
            stringBuilder.append(operation.exportBasic());
        }
        stringBuilder.append("</Operations>");

        stringBuilder.append("<Children>");
        for (Mission child : children) {
            stringBuilder.append(child.export());
        }
        stringBuilder.append("</Children>");


        stringBuilder.append("</Mission>");
        return stringBuilder.toString();
    }


    public List<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParent(Mission parent) {
        this.parent = parent;
    }

    public void addChild(Mission mission) {
        children.add(mission);
    }

    public Mission getParent() {
        return parent;
    }

    public List<Mission> getChildren() {
        return children;
    }

}
