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

public class MemoryMetricCollector extends ProcFSCollector implements ProcessMetricCollector {

    long kernelClockTick;


    MetricStore memRssStore;
    MetricStore memSwapStore;

    public MemoryMetricCollector() {

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

        Path basePath = Paths.get(Conf.monDataPath).resolve(MonitorService.nodeName)
                .resolve(String.valueOf(processId));

        memRssStore = new StandardMetricStore();
        String rssMetric = "mem-rss";

        memRssStore.setBasePath(basePath);
        memRssStore.setMetric(rssMetric);
        memRssStore.setInterval(interval);
        memRssStore.init();
        addStore(rssMetric, memRssStore);



        memSwapStore = new StandardMetricStore();
        String swapMetric = "mem-swap";
        memSwapStore.setBasePath(basePath);
        memSwapStore.setMetric(swapMetric);
        memSwapStore.setInterval(interval);
        memSwapStore.init();
        addStore(swapMetric, memSwapStore);
    }



    public void collect() throws Exception {
        for (int i = 0; i < 16; i++) {
            reader.readLine();
        }
        long memoryRSS = Long.parseLong(reader.readLine().split("\\s+")[1]);
        memRssStore.addMeasurement(new Measurment(timestamp, String.valueOf(memoryRSS)));
        for (int i = 0; i < 5; i++) {
            reader.readLine();
        }
        long memorySwap = Long.parseLong(reader.readLine().split("\\s+")[1]);
        memSwapStore.addMeasurement(new Measurment(timestamp, String.valueOf(memorySwap)));
    }

    @Override
    public void setProcessId(int processId) {
        this.processId = processId;
        this.procFilePath = String.format("/proc/%s/status", processId);
    }

    @Override
    public int getProcessId() {
        return processId;
    }
}
