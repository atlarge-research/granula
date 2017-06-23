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
package science.atlarge.granula.monitor.executor;

import science.atlarge.granula.monitor.master.MonitorMaster;
import science.atlarge.granula.monitor.worker.MonitorWorker;

import java.nio.file.Paths;

public class GranulaMonitorWorker {


    public static void main(String[] args) {
        startProfilerWorker();
    }



    public static void startProfilerMaster() {
        MonitorMaster monitorMaster = new MonitorMaster();
        monitorMaster.init();
    }


    public static void startProfilerWorker() {

        MonitorWorker monitorWorker = new MonitorWorker();
        monitorWorker.setConf(Paths.get("conf/profiler-worker.conf"));
        monitorWorker.init();
    }

}
