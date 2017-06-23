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

import java.util.Map;

/**
 * Created by wlngai on 6/19/16.
 */
public class Overview {

    long startTime;
    long endTime;
    long nodeSize;
    long threadSize;
    String algorithm;
    String dataset;
    String name;
    String description;
    Map<String, Long> breakDown;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Long> getBreakDown() {
        return breakDown;
    }

    public void setBreakDown(Map<String, Long> breakDown) {
        this.breakDown = breakDown;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(long nodeSize) {
        this.nodeSize = nodeSize;
    }

    public long getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(long threadSize) {
        this.threadSize = threadSize;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
