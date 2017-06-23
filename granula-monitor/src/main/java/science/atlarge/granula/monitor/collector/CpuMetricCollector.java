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
package science.atlarge.granula.monitor.collector;


import science.atlarge.granula.monitor.conf.Conf;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.metric.MetricStore;
import science.atlarge.granula.monitor.metric.StandardMetricStore;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CpuMetricCollector extends ProcFSCollector implements ProcessMetricCollector {

    long kernelClockTick;
    MetricStore cpuSecStore;


    public CpuMetricCollector() {
    }

    public boolean init() {
        if(!super.init()) {
            return false;
        }
        setConstantValues();
        setMetricStore();

        return true;
    }

    private void setConstantValues() {
        kernelClockTick = Long.parseLong(collectFromExecution("getconf", "CLK_TCK"));
    }

    private void setMetricStore() {
        cpuSecStore = new StandardMetricStore();
        String meric = "cpu-sec";
        Path basePath = Paths.get(Conf.monDataPath).resolve(MonitorService.nodeName)
                .resolve(String.valueOf(processId));
        cpuSecStore.setBasePath(basePath);
        cpuSecStore.setMetric(meric);
        cpuSecStore.setInterval(interval);
        cpuSecStore.init();
        addStore(meric, cpuSecStore);
    }



    public void collect() throws Exception {
        String[] procMetrics = reader.readLine().split("\\s+");
        double cpuTime = calculateCpuTime(procMetrics);
        cpuSecStore.addMeasurement(new Measurment(timestamp, String.valueOf(cpuTime)));
    }


    private double calculateCpuTime(String[] procMetrics) {

        long uTime = Long.parseLong(procMetrics[13]);
        long sTime = Long.parseLong(procMetrics[14]);
        long cuTime = Long.parseLong(procMetrics[15]);
        long csTime = Long.parseLong(procMetrics[16]);
        return ((double) uTime + sTime + cuTime + csTime) / kernelClockTick;
    }

    @Override
    public void setProcessId(int processId) {
        this.processId = processId;
        this.procFilePath = String.format("/proc/%s/stat", processId);
    }

    @Override
    public int getProcessId() {
        return processId;
    }
}
