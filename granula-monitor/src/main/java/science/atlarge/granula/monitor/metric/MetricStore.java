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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wlngai on 5/27/16.
 */
public abstract class MetricStore {

    BlockingQueue queue;
    String metric;
    Path basePath;
    long interval;
    List<Metric> metrics;

    public boolean init() {
        queue = new ArrayBlockingQueue(1024);
        metrics = new ArrayList<>();
        return true;
    }

    public abstract void run() throws IOException;

    public abstract boolean kill();

    public abstract boolean flush();

    public Path getBasePath() {
        return basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void addMeasurement(Measurment measurment) throws InterruptedException {
        queue.put(measurment);
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

}
