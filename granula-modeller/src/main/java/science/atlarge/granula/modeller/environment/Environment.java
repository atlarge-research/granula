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

package science.atlarge.granula.modeller.environment;

import science.atlarge.granula.modeller.entity.Containable;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wing on 26-2-15.
 */
public class Environment extends Containable {

    Map<String, ComputationNode> nodes;

    public Environment(String name) {
        super();
        this.name = name;
        nodes = new LinkedHashMap<>();
    }

    public Environment() {
        super();
        this.name = "unspecified";
        nodes = new LinkedHashMap<>();
    }

    public List<ComputationNode> getNodes() {
        return new ArrayList<ComputationNode>(nodes.values());
    }

    public ComputationNode getNode(String id) {
        if(nodes.containsKey(id)) {
            return nodes.get(id);
        } else {
            throw new IllegalStateException();
        }

    }

    public void addNode(ComputationNode node) {
        nodes.put(node.getName(), node);
    }

    public void addResourceInfo(TimeSeriesInfo timeSeriesInfo, String processId, String nodeName) {
        if(nodes.containsKey(nodeName)) {
            ComputationNode node = getNode(nodeName);
            node.addResourceInfo(timeSeriesInfo, processId);
        } else {
            addNode(new ComputationNode(nodeName));
            System.out.println(String.format("Warning: creating node %s at environment %s", nodeName, name));
        }
    }

    public void addResourceInfo(TimeSeriesInfo timeSeriesInfo, String nodeName) {
        if(nodes.containsKey(nodeName)) {
            ComputationNode node = getNode(nodeName);
            node.addResourceInfo(timeSeriesInfo);
        } else {
            addNode(new ComputationNode(nodeName));
            System.out.println(String.format("Warning: creating node %s at environment %s", nodeName, name));
        }
    }


//    public String exportBasic() {
//        return String.format("<Environment name=\"%s\" uuid=\"%s\">", name, uuid);
//    }
//
//    public String export() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(String.format("<Environment name=\"%s\" uuid=\"%s\">", name, uuid));
//
//        stringBuilder.append("<ComputationNodes>");
//        for (ComputationNode computationNode : getNodes()) {
//            stringBuilder.append(computationNode.export());
//        }
//        stringBuilder.append("</ComputationNodes>");
//
//        stringBuilder.append(exportInfos());
//        stringBuilder.append(exportVisuals());
//
//        stringBuilder.append("</Environment>");
//        return stringBuilder.toString();
//    }
}
