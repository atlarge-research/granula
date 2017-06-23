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

import science.atlarge.granula.monitor.metric.Metric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wlngai on 20-4-16.
 */
public class JobMonitor {
    String jobId;
    Set<NodeMonitor> nodeMonitors;
    Set<ProcessMonitor> processMonitors;

    public JobMonitor(String jobId) {
        this.jobId = jobId;
        processMonitors = new HashSet<>();
        nodeMonitors = new HashSet<>();
    }

    public void run() {

    }

    public void linkProcess(ProcessMonitor processMonitor) {
        processMonitors.add(processMonitor);
    }

    public void linkMachine(NodeMonitor nodeMonitor) {
        nodeMonitors.add(nodeMonitor);
    }

    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();

        for (NodeMonitor nodeMonitor : nodeMonitors) {
            metrics.addAll(nodeMonitor.getMetrics());
            nodeMonitor.flush();
        }

        for (ProcessMonitor processMonitor : processMonitors) {
            metrics.addAll(processMonitor.getMetrics());
            processMonitor.flush();
        }

        return metrics;
    }

    public Set<NodeMonitor> getNodeMonitors() {
        return nodeMonitors;
    }

    public Set<ProcessMonitor> getProcessMonitors() {
        return processMonitors;
    }

    public String getJobId() {
        return jobId;
    }



}
