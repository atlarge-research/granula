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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.conf.Conf;
import science.atlarge.granula.monitor.util.SystemUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wlngai on 5/23/16.
 */
public class MonitorService {

    public static Conf conf;
    public static Config config;
    public boolean isMaster;
    public static String nodeName;
    protected Map<String, TaskContext> tasks;

    public MonitorService() {

        nodeName = SystemUtil.getComputerName();
        tasks = new HashMap<>();
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public static Conf getConf() {
        return conf;
    }

    public static void setConf(Path path) {
        config = ConfigFactory.parseFile(path.toFile());

        conf.monDataPath = config.getString("akka.profiler.mon-path");

        if(nodeName.equals("debian-vm")) {
            conf.monDataPath = "/media/sf_Large/Large/granula/monitor/raw/";
        }


    }
}
