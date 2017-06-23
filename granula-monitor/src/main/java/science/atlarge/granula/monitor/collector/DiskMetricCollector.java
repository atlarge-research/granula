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

public class DiskMetricCollector extends ProcFSCollector implements ProcessMetricCollector {

    MetricStore dskRcharStore;
    MetricStore dskWcharStore;

    public DiskMetricCollector() {

    }

    public boolean init() {
        if(!super.init()) {
            return false;
        }
        setMetricStore();
        return true;
    }


    private void setMetricStore() {

        Path basePath = Paths.get(Conf.monDataPath).resolve(MonitorService.nodeName)
                .resolve(String.valueOf(processId));

        dskRcharStore = new StandardMetricStore();
        String dskRcharMetric = "dsk-rchar";

        dskRcharStore.setBasePath(basePath);
        dskRcharStore.setMetric(dskRcharMetric);
        dskRcharStore.setInterval(interval);
        dskRcharStore.init();
        addStore(dskRcharMetric, dskRcharStore);



        dskWcharStore = new StandardMetricStore();
        String dskWcharMetric = "dsk-wchar";
        dskWcharStore.setBasePath(basePath);
        dskWcharStore.setMetric(dskWcharMetric);
        dskWcharStore.setInterval(interval);
        dskWcharStore.init();
        addStore(dskWcharMetric, dskWcharStore);
    }



    public void collect() throws Exception {
        long diskRChar = Long.parseLong(reader.readLine().split("\\s+")[1]);
        dskRcharStore.addMeasurement(new Measurment(timestamp, String.valueOf(diskRChar)));
        long diskWChar = Long.parseLong(reader.readLine().split("\\s+")[1]);
        dskWcharStore.addMeasurement(new Measurment(timestamp, String.valueOf(diskWChar)));
    }

    @Override
    public void setProcessId(int processId) {
        this.processId = processId;
        this.procFilePath = String.format("/proc/%s/io", processId);
    }

    @Override
    public int getProcessId() {
        return processId;
    }
}
