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

import science.atlarge.granula.monitor.conf.Conf;
import science.atlarge.granula.monitor.collector.CpuMetricCollector;
import science.atlarge.granula.monitor.collector.DiskMetricCollector;
import science.atlarge.granula.monitor.collector.MemoryMetricCollector;
import science.atlarge.granula.monitor.collector.MetricCollector;

/**
 * Created by wlngai on 21-4-16.
 */
public class StandardProcessMonitor extends ProcessMonitor {

    public StandardProcessMonitor(String processId) {
        super(processId);
    }

    @Override
    public void addCollectors() {
        CpuMetricCollector cpuMetricCollector = new CpuMetricCollector();
        cpuMetricCollector.setInterval(Conf.stdInterval);
        addCollector("cpu", cpuMetricCollector);

        MemoryMetricCollector memoryMetricCollector = new MemoryMetricCollector();
        memoryMetricCollector.setInterval(Conf.stdInterval);
        addCollector("memory", memoryMetricCollector);

        DiskMetricCollector diskMetricCollector = new DiskMetricCollector();
        diskMetricCollector.setInterval(Conf.stdInterval);
        addCollector("disk", diskMetricCollector);
    }


    public void run() {
        for (MetricCollector metricCollector : metricCollectors.values()) {
            metricCollector.run();
        }
    }
}
