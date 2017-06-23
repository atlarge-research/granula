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
package science.atlarge.granula.monitor.comm.task;

import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.monitor.JobMonitor;
import science.atlarge.granula.monitor.monitor.NodeMonitor;
import science.atlarge.granula.monitor.monitor.ProcessMonitor;
import science.atlarge.granula.monitor.worker.MonitorWorker;


public class MonitorContext extends TaskContext {



    public enum State {RegisterJob, LinkNode, LinkProcess, StartMonitorProcess, ModifyInterval, StopMonitorProcess};
    protected State state;

    protected String jobId;
    protected String machineId;
    protected String processId;
    protected String interval;

    public MonitorContext() {
        super();
        type = TaskType.Monitor;
    }

    @Override
    public void executeAt(MonitorService monitorService) {
        switch (state) {
            case RegisterJob:
                registerJob(monitorService);
                break;
            case LinkNode:
                linkNodeToJob(monitorService);
                break;
            case LinkProcess:
                linkProcessToJob(monitorService);
                break;
            case StartMonitorProcess:
                startMonitorProcess(monitorService);
                break;
            case ModifyInterval:
                modifyInterval(monitorService);
                break;
            case StopMonitorProcess:
                stopMonitorProcess(monitorService);
                break;
            default:
                throw new IllegalStateException("Undefined task state.");
        }
    }



    private void modifyInterval(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        ProcessMonitor processMonitor = monitorWorker.getOrCreateProcessMonitor(processId);

        processMonitor.modifyInterval(Long.parseLong(interval));
        System.out.println(String.format("Modify interval %s for process %s.", interval, processId));

    }

    private void startMonitorProcess(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        ProcessMonitor processMonitor = monitorWorker.getOrCreateProcessMonitor(processId);
        processMonitor.run();
        System.out.println(String.format("Start monitoring process %s.", processId));
    }

    private void stopMonitorProcess(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        ProcessMonitor processMonitor = monitorWorker.getOrCreateProcessMonitor(processId);
        processMonitor.kill();
        System.out.println(String.format("Stop monitoring process %s.", processId));
    }

    private void linkProcessToJob(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        JobMonitor jobMonitor = monitorWorker.getOrCreateJobMonitor(jobId);
        ProcessMonitor processMonitor = monitorWorker.getOrCreateProcessMonitor(processId);
        jobMonitor.linkProcess(processMonitor);
    }

    private void linkNodeToJob(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        JobMonitor jobMonitor = monitorWorker.getOrCreateJobMonitor(jobId);
        NodeMonitor nodeMonitor = monitorWorker.getOrCreateNodeMonitor();
        jobMonitor.linkMachine(nodeMonitor);
    }

    private void registerJob(MonitorService monitorService) {
        MonitorWorker monitorWorker = (MonitorWorker) monitorService;
        JobMonitor jobMonitor = monitorWorker.getOrCreateJobMonitor(jobId);
        jobMonitor.run();
        System.out.println(String.format("Monitoring job %s.", jobId));
    }


    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getJobId() {
        return jobId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
