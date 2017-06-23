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
package science.atlarge.granula.monitor.metric;

import science.atlarge.granula.monitor.collector.Measurment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by wlngai on 5/27/16.
 */
public class StandardMetricStore extends MetricStore {

    BufferedWriter _100msWriter;
    BufferedWriter _1000msWriter;
    BufferedWriter _10000msWriter;


    public boolean init() {
        super.init();

        queue = new ArrayBlockingQueue(1024);

        String nodeId = basePath.getParent().getFileName().toString();
        String processId = basePath.getFileName().toString();


        addMetric(nodeId, processId, metric, 100);
        _100msWriter = initWriter(metric + "_" + "100ms");

        addMetric(nodeId, processId, metric, 1000);
        _1000msWriter = initWriter(metric + "_" + "1000ms");

        addMetric(nodeId, processId, metric, 10000);
        _10000msWriter = initWriter(metric + "_" + "10000ms");

        return true;
    }

    public void run() throws IOException {

        Measurment measurment = (Measurment) queue.poll();
        while(measurment != null) {
            String results = String.format("%s %s\n", measurment.getTimestamp(), measurment.getValue());
            if(measurment.getTimestamp() % 10000 == 0) {
                _10000msWriter.write(results);
            }
            if(measurment.getTimestamp() % 1000 == 0) {
                _1000msWriter.write(results);
            }
            if(measurment.getTimestamp() % 100 == 0) {
                _100msWriter.write(results);
            }
            measurment = (Measurment) queue.poll();
        }
        _100msWriter.flush();
        _1000msWriter.flush();
        _10000msWriter.flush();
    }


    public boolean flush() {
        try {
            _100msWriter.flush();
            _1000msWriter.flush();
            _10000msWriter.flush();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return true;
    }

    public boolean kill() {
        flush();
        try {
            _100msWriter.close();
            _1000msWriter.close();
            _10000msWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addMetric(String nodeId, String processId, String type, long interval) {
        TimeSeriesMetric metric = new TimeSeriesMetric();
        metric.setNodeId(nodeId);
        metric.setProcessId(processId);
        metric.setType(type);
        metric.setInterval(interval);
        metric.setFilePath(basePath.resolve(type + "_" + interval + "ms").toString());
        metrics.add(metric);
    }

    private BufferedWriter initWriter(String metricKey) {
        BufferedWriter writer = null;
        try {
            File file = basePath.resolve(metricKey).toFile();
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

}
