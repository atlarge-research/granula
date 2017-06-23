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
package science.atlarge.granula.monitor.worker;


import com.typesafe.config.ConfigValueFactory;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.info.MasterInfo;
import science.atlarge.granula.monitor.info.ProcessInfo;
import science.atlarge.granula.monitor.monitor.*;
import science.atlarge.granula.monitor.util.json.JsonUtil;
import science.atlarge.granula.monitor.util.SystemUtil;

import java.util.HashMap;
import java.util.Map;

public class MonitorWorker extends MonitorService {

    MasterInfo masterInfo;
    WorkerEndpoint endpoint;
    Map<String, JobMonitor> jobMonitors;
    NodeMonitor osMonitor;
    Map<String, ProcessMonitor> processMonitors;




    public MonitorWorker() {
        super();
        masterInfo = new MasterInfo();
    }

    public void init() {
        isMaster = false;

        int port = config.getInt("akka.profiler.worker.port");
//        port = (new Random()).nextInt(4000)+4000;
        config = config.withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port));

        setMasterInfo();

        jobMonitors = new HashMap<>();
        processMonitors = new HashMap<>();

        WorkerEndpoint.InitEndpoint(this);
        monitorNode();
    }

    private void setMasterInfo() {

        int masterPort = config.getInt("akka.profiler.master.port");
        String masterIp = config.getString("akka.profiler.master.ip");
        masterInfo.setPath(String.format("akka.tcp://profiler-master@%s:%s/user/profiler-master", masterIp, masterPort));
        masterInfo.setIp(ProcessInfo.Path2IpAddress(masterInfo.getPath()));

    }



    public void setEndpoint(WorkerEndpoint wEndpoint) {
        this.endpoint = wEndpoint;
    }

    public MasterInfo getMasterInfo() {
        return masterInfo;
    }

    public void setMasterInfo(MasterInfo masterInfo) {
        this.masterInfo = masterInfo;
    }

    public synchronized JobMonitor getOrCreateJobMonitor(String jobId) {
        if(jobMonitors.containsKey(jobId)) {
            return jobMonitors.get(jobId);
        } else {
            System.out.println("Create new job-mon " + jobId);
            JobMonitor jobMonitor = new JobMonitor(jobId);
            jobMonitors.put(jobId, jobMonitor);
            return jobMonitor;
        }
    }

    public synchronized NodeMonitor getOrCreateNodeMonitor() {
        if(osMonitor != null) {
            return osMonitor;
        } else {
            String nodeName = SystemUtil.getComputerName();
            osMonitor = new StandardNodeMonitor();
            osMonitor.setNodeName(nodeName);
            return osMonitor;
        }

    }

    public void monitorNode() {
        NodeMonitor osMonitor = getOrCreateNodeMonitor();
        osMonitor.run();
        System.out.println("Monitoring os " + nodeName);
    }


    public synchronized ProcessMonitor getOrCreateProcessMonitor(String processId) {
        if(processMonitors.containsKey(processId)) {
            return processMonitors.get(processId);
        } else {
            ProcessMonitor processMonitor = new StandardProcessMonitor(processId);
            processMonitors.put(processId, processMonitor);
            return processMonitor;
        }
    }

    public void executeTask(TaskContext taskContext) {
        System.out.println(String.format("Received: %s", JsonUtil.toJson(taskContext)));
        try {
            taskContext.executeAt(this);
        } catch (Exception e) {
            System.out.println(String.format("Task %s failed", taskContext.getId()));
        }

    }

    public synchronized void startMonitorProcess(String processId) {
        ProcessMonitor processMonitor = getOrCreateProcessMonitor(processId);
        processMonitor.run();
        System.out.println("Start monitoring process " + processId);
    }

    public synchronized void stopMonitorProcess(String processId) {
        ProcessMonitor processMonitor = getOrCreateProcessMonitor(processId);
        processMonitor.kill();
        System.out.println("Stop monitoring process " + processId);
    }

    public Map<String, JobMonitor> getJobMonitors() {
        return jobMonitors;
    }

    public Map<String, TaskContext> getTasks() {
        return tasks;
    }

    public WorkerEndpoint getEndpoint() {
        return endpoint;
    }
}
