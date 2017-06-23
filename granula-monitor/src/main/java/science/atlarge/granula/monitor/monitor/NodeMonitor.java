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
package science.atlarge.granula.monitor.monitor;

import science.atlarge.granula.monitor.collector.MetricCollector;
import science.atlarge.granula.monitor.metric.Metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wlngai on 21-4-16.
 */
public abstract class NodeMonitor extends Monitor {

    Map<String, MetricCollector> metricCollectors;

    String nodeName;

    public NodeMonitor() {
        metricCollectors = new HashMap<>();
        addCollectors();
    }

    public void addCollector(String collectorType, MetricCollector metricCollector) {
        metricCollectors.put(collectorType, metricCollector);
        metricCollector.init();
    }


    public abstract void addCollectors();

    public void run() {
        for (MetricCollector metricCollector : metricCollectors.values()) {
                metricCollector.run();
        }

    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        for (MetricCollector metricCollector : metricCollectors.values()) {
            metrics.addAll(metricCollector.getMetrics());
        }
        return metrics;
    }

    public void flush() {
        for (MetricCollector metricCollector : metricCollectors.values()) {
            metricCollector.flush();
        }
    }
}
