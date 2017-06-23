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
import java.util.List;
import java.util.Map;

public class CombinedNetworkMetricCollector extends ProcFSCollector {

    Map<String, List<Long>> networkTraffics;
    String REMOTE = "remote";
    String LOCAL = "local";
    String LO = "lo";
    String IB = "ib0";

    MetricStore netEthRecStore;
    MetricStore netEthSndStore;


    boolean check = false;


    public CombinedNetworkMetricCollector() {
        this.procFilePath = "/proc/net/dev";
    }

    public boolean init() {
        if(!super.init()) {
            return false;
        }
        setMetricStores();
        return true;
    }

    private void setMetricStores() {
        netEthSndStore = setMetricStore("net-snd");
        netEthRecStore = setMetricStore("net-rec");




    }

    private MetricStore setMetricStore(String metric) {
        MetricStore metricStore = new StandardMetricStore();
        Path basePath = Paths.get(Conf.monDataPath).resolve(MonitorService.nodeName)
                .resolve(String.valueOf(0));
        System.out.println(basePath);
        metricStore.setBasePath(basePath);
        metricStore.setMetric(metric);
        metricStore.setInterval(interval);
        metricStore.init();
        addStore(metric, metricStore);
        return metricStore;
    }



    public void collect() throws Exception {

        reader.readLine();
        reader.readLine();
        long remoteReceiveVolume = 0, remoteTransmitVolume = 0;
        long ipoibRecVolume = 0, ipoibSndVolume = 0;
        long localReceiveVolume = 0, localTransmitVolume = 0;

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] netIntMetrics = line.trim().split("\\s+");
            String netInterface = netIntMetrics[0].replace(":", "");
            long receiveVolume = Long.parseLong(netIntMetrics[1]);
            long transmitVolume = Long.parseLong(netIntMetrics[9]);
//            networkTraffics.get(netInterface).add(receiveVolume);

            if(!netInterface.equals(IB) && !netInterface.equals(LO)) {
                remoteReceiveVolume += receiveVolume;
                remoteTransmitVolume += transmitVolume;
            } if(netInterface.startsWith(IB)) {
                ipoibRecVolume += receiveVolume;
                ipoibSndVolume += transmitVolume;
            } else {
                localReceiveVolume += receiveVolume;
                localTransmitVolume += transmitVolume;
            }
        }

//
//        netIpoibRecStore.addMeasurement(new Measurment(timestamp, String.valueOf(ipoibRecVolume)));
//        netIpoibSndStore.addMeasurement(new Measurment(timestamp, String.valueOf(ipoibSndVolume)));
//
//
//

        String perfquery = collectFromExecution("perfquery", "-x");

        if(!check) {
            System.out.println(perfquery);
            check = true;
        }

        long ibRecVolume = 0;
        long ibSndVolume = 0;

        for (String line : perfquery.split("\n")) {
            if(line.contains("PortRcvData")) {
                String[] parts = line.trim().split("\\.");
                ibRecVolume = Long.parseLong(parts[parts.length - 1]) * 4;
            }
            if(line.contains("PortXmitData")) {
                String[] parts = line.trim().split("\\.");
                ibSndVolume = Long.parseLong(parts[parts.length - 1]) * 4;
            }
        }

        netEthRecStore.addMeasurement(new Measurment(timestamp, String.valueOf(remoteReceiveVolume + ibRecVolume)));
        netEthSndStore.addMeasurement(new Measurment(timestamp, String.valueOf(remoteTransmitVolume + ibSndVolume)));
//
//

//
//        netIbRecStore.addMeasurement(new Measurment(timestamp, String.valueOf(ibRecVolume)));
//        netIbSndStore.addMeasurement(new Measurment(timestamp, String.valueOf(ibSndVolume)));
    }



}
