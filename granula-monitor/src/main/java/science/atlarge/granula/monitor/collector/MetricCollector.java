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

import science.atlarge.granula.monitor.metric.Metric;
import science.atlarge.granula.monitor.metric.MetricStore;
import science.atlarge.granula.monitor.util.ExecutorUtil;
import science.atlarge.granula.monitor.util.TimeUtil;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wlngai on 21-4-16.
 */
public abstract class MetricCollector extends Collector {

    BlockingQueue queue;
    ExecutorUtil executorUtil;
    String outputPath;
    Map<String, MetricStore> metricStores;
    BufferedWriter bufferedWriter;
    long timestamp;
    long preTimestamp;

    long storeInterval = 1000;

    long interval;

    public abstract boolean init();
    public abstract boolean kill();
    public abstract void collect() throws Exception;

    public void flush() {
        for (MetricStore metricStore : metricStores.values()) {
            metricStore.flush();
        }
    }

    int error = 0;

    public MetricCollector() {
        this.executorUtil = new ExecutorUtil(4);
        queue = new ArrayBlockingQueue(1024);
        metricStores = new HashMap<>();
    }

    public boolean run() {
        System.out.println(String.format("Collector %s starting at %s.",
                this.getClass().getSimpleName(), System.currentTimeMillis()));
        if(status == Status.PREP) {
            //startRoutine();
            status = Status.STARTED;
            executorUtil.execute(() -> {
                while (status == Status.STARTED || status == Status.STANDBY) {
                    preTimestamp = System.currentTimeMillis();
                    timestamp = preTimestamp + interval - preTimestamp % interval;
                    TimeUtil.sleep((int) (timestamp - System.currentTimeMillis()));
                    execute();
                    preTimestamp = timestamp;
                }
            });

            executorUtil.execute(() -> {
                while (status != Status.KILLED) {
                    try {
                        store();
                        TimeUtil.sleep(storeInterval);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return true;
        } else if(status == Status.STANDBY) {
            status = Status.STARTED;
            return true;
        } else {
                return false;
        }
    }

    private void execute() {
        try {
            if (status == Status.STARTED) {
                preprocess();
                collect();
                postprocess();
            }

        } catch (Exception e) {
            System.out.println(String.format("%s cannot execute its collection routine due to %s",
                    getName(), e.getClass().getSimpleName()));
            error++;
            if(error >= 3) {
                pause();
            }

        }
    }

    protected void store() throws Exception {
        for (MetricStore metricStore : metricStores.values()) {
            metricStore.run();
        }
    };

    public boolean pause() {
        if(status == Status.STARTED) {
            status = Status.STANDBY;
            return true;
        }else {
            return false;
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
        for (MetricStore metricStore : metricStores.values()) {
            metricStore.setInterval(interval);
        }
    }

    protected abstract void preprocess() throws Exception;
    protected abstract void postprocess() throws Exception;


    public void addStore(String metric, MetricStore metricStore) {
        metricStores.put(metric, metricStore);
    }

    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        for (MetricStore metricStore : metricStores.values()) {
            metrics.addAll(metricStore.getMetrics());
        }
        return metrics;
    }

}
