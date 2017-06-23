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

import akka.actor.ActorRef;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.info.WorkerInfo;
import science.atlarge.granula.monitor.master.MonitorMaster;
import science.atlarge.granula.monitor.metric.Metric;
import science.atlarge.granula.monitor.monitor.JobMonitor;
import science.atlarge.granula.monitor.util.FileUtil;
import science.atlarge.granula.monitor.util.json.Exclude;
import science.atlarge.granula.monitor.worker.MonitorWorker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CollectContext extends TaskContext implements Cloneable {

    public enum State {Submit, Lookup, Request, Confirm, List, Send, Receive};
    protected State state;


    @Exclude
    protected  ActorRef submitter;

    protected String jobId;
    protected String outPath;

    protected int sndWorkerSize;
    protected int recWorkerSize;
    protected int sndMetricSize;
    protected int recMetricSize;
    @Exclude protected Set<Metric> sndMetrics;
    @Exclude protected Set<Metric> recMetrics;

    protected Metric metric;
    @Exclude protected String metricData;

    public CollectContext() {
        super();
        type = TaskType.Collect;
        state = State.Lookup;

        sndWorkerSize = -1;
        sndMetrics = new HashSet<>();
        recMetrics = new HashSet<>();
    }

    @Override
    public void executeAt(MonitorService service) {

        CollectContext context = this;

        switch (state) {
            case Submit:
                lookup(service, context);
                break;
            case Lookup:
                confirm(service, context);
                request(service, context);
                break;
            case Request:
                list(service, context);
                send(service, context);
                break;
            case Confirm:
                prepare(service, context);
                break;
            case List:
                prepare(service, context);
                break;
            case Send:
                receive(service, context);
                break;
            case Receive:
                break;
            default:
                throw new IllegalStateException("Undefined task state.");
        }
    }

    private void list(MonitorService service, CollectContext recContext) {
        MonitorWorker monitorWorker = ((MonitorWorker) service);
        JobMonitor jobMonitor = monitorWorker.getOrCreateJobMonitor(recContext.jobId);

        CollectContext sndContext = recContext.updateContext();
        sndContext.sndMetrics.addAll(jobMonitor.getMetrics());
        sndContext.state = State.List;
        monitorWorker.getEndpoint().sendTask(sndContext, sender);
    }

    public void send(MonitorService service, CollectContext recContext) {
        MonitorWorker monitorWorker = ((MonitorWorker) service);
        JobMonitor jobMonitor = monitorWorker.getOrCreateJobMonitor(recContext.jobId);

        for (Metric metric : jobMonitor.getMetrics()) {

            CollectContext sndContext = recContext.updateContext();
            sndContext.metric = metric;
            sndContext.metricData = FileUtil.readFile(Paths.get(metric.getFilePath()));

            sndContext.state = State.Send;
            monitorWorker.getEndpoint().sendTask(sndContext, sender);
        }

    }

    public void prepare(MonitorService service, CollectContext recContext) {
        MonitorWorker monitorWorker = ((MonitorWorker) service);
        CollectContext finalContext = (CollectContext) monitorWorker.getTasks().get(id);

        recContext = recContext.updateContext();

        if(recContext.state == State.Confirm) {
            finalContext.sndWorkerSize = recContext.sndWorkerSize;
        } else if(recContext.state == State.List) {
            finalContext.recWorkerSize++;
            finalContext.sndMetricSize += recContext.sndMetrics.size();
            finalContext.sndMetrics.addAll(recContext.sndMetrics);
        }
    }


    public void receive(MonitorService service, CollectContext recContext) {
        MonitorWorker monitorWorker = ((MonitorWorker) service);
        CollectContext mContext = (CollectContext) monitorWorker.getTasks().get(id);

        recContext = recContext.updateContext();

        mContext.recMetricSize++;

        String machineId = recContext.metric.getNodeId();
        String processId = recContext.metric.getProcessId();
        String fileName = Paths.get(recContext.metric.getFilePath()).getFileName().toString();

        Path outFile = Paths.get(mContext.outPath + "/" + mContext.jobId + "/" + machineId + "/" + processId + "/" + fileName);
        FileUtil.writeFile(recContext.metricData, outFile);

        System.out.println(String.format("Worker: %s/%s, Metric: %s/%s",
                mContext.recWorkerSize, mContext.sndWorkerSize, mContext.recMetricSize, mContext.sndMetricSize));

        if (mContext.recWorkerSize == mContext.sndWorkerSize & mContext.sndMetricSize == mContext.recMetricSize) {
            Path successFile = Paths.get(mContext.outPath + "/" + mContext.jobId + "/" + "success");
            FileUtil.writeFile("success", successFile);
        }
    }



    public void lookup(MonitorService service, CollectContext recContext) {
        MonitorWorker monitorWorker = ((MonitorWorker) service);
        recContext = recContext.updateContext();

        recContext.state = State.Lookup;
        monitorWorker.getEndpoint().sendTask(recContext, monitorWorker.getMasterInfo().getActorRef());
        monitorWorker.getTasks().put(recContext.getId(), recContext);
    }

    public void confirm(MonitorService service, CollectContext recContext) {
        MonitorMaster monitorMaster = ((MonitorMaster) service);
        recContext = recContext.updateContext();

        recContext.sndWorkerSize = monitorMaster.getWorkers().size();
        recContext.state = State.Confirm;
        monitorMaster.getEndpoint().sendTask(recContext, sender);
    }

    public void request(MonitorService monitorService, CollectContext recContext) {
        MonitorMaster monitorMaster = ((MonitorMaster) monitorService);
        recContext = recContext.updateContext();

        recContext.submitter = sender;
        recContext.state = State.Request;
        for (WorkerInfo receiver : monitorMaster.getWorkers().values()) {
            monitorMaster.getEndpoint().sendTask(recContext, sender, receiver.getActorRef());
        }

    }







    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public CollectContext updateContext() {
        CollectContext collectContext = null;
        try {
            collectContext = (CollectContext) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return collectContext;
    }
}
