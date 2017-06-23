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
package science.atlarge.granula.monitor.backup;

/**
 * Created by wlngai on 20-4-16.
 */
public class Executors {
//
//    public static void main(String[] args) {
//        String nodeName = SystemUtil.getComputerName();
//
//        Executors jobMonitor = new Executors();
//        jobMonitor.monitorOs(nodeName);
//        jobMonitor.monitorProcess(nodeName, 3113);
//        jobMonitor.monitorProcess(nodeName, 3388);
//    }
//
//    public void monitorOs(String nodeName) {
//
//        NodeMonitor OsMonitor = new NodeMonitor();
//        OsMonitor.setNodeName(nodeName);
//
//        NetworkMetricCollector networkMetricCollector = new NetworkMetricCollector();
//        networkMetricCollector.setInterval(500);
//        OsMonitor.addCollector("network", networkMetricCollector);
//        OsMonitor.run();
//    }
//
//    public void monitorProcess(String nodeName, int processId) {
//
//        ProcessMonitor processMonitor = new ProcessMonitor(String.valueOf(processId));
//        processMonitor.setProcessId(processId);
//        processMonitor.setNodeName(nodeName);
//
//        CpuMetricCollector cpuMetricCollector = new CpuMetricCollector();
//        cpuMetricCollector.setInterval(500);
//        processMonitor.addCollector("cpu", cpuMetricCollector);
//
//        MemoryMetricCollector memoryMetricCollector = new MemoryMetricCollector();
//        memoryMetricCollector.setInterval(500);
//        processMonitor.addCollector("memory", memoryMetricCollector);
//        processMonitor.run();
//    }
//
//
//
//
//    public static void doSomething() {
//
//        int processId = 5144;
//        ExecutorUtil executorUtil = new ExecutorUtil(10);
//        List<MetricCollector> metricCollectors = new ArrayList<>();
//
//        CpuMetricCollector cpuMetricCollector = new CpuMetricCollector();
//        cpuMetricCollector.setInterval(3000);
//        cpuMetricCollector.setProcessId(processId);
//        cpuMetricCollector.init();
//        metricCollectors.add(cpuMetricCollector);
//
//
//        NetworkMetricCollector networkMetricCollector = new NetworkMetricCollector();
//        networkMetricCollector.setInterval(3000);
//        networkMetricCollector.init();
//        metricCollectors.add(networkMetricCollector);
//
//        for (MetricCollector metricCollector : metricCollectors) {
//            executorUtil.execute(() -> {
//                metricCollector.run();
//            });
//        }
//
//        TimeUtil.waitFor(7);
//        cpuMetricCollector.pause();
//
//        TimeUtil.waitFor(20);
//        cpuMetricCollector.run();
//        TimeUtil.waitFor(7);
//        cpuMetricCollector.kill();
//        networkMetricCollector.kill();
//        executorUtil.setBarrier();
//    }
}
