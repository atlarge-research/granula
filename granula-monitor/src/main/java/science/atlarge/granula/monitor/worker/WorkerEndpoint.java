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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import science.atlarge.granula.monitor.comm.response.RegisterResponse;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.info.ActorId;
import science.atlarge.granula.monitor.comm.request.HealthRequest;
import science.atlarge.granula.monitor.comm.request.MonitorProcessRequest;
import science.atlarge.granula.monitor.comm.request.RegisterRequest;
import science.atlarge.granula.monitor.comm.request.Request;
import science.atlarge.granula.monitor.info.MasterInfo;
import science.atlarge.granula.monitor.info.WorkerInfo;
import science.atlarge.granula.monitor.comm.response.HealthResponse;
import science.atlarge.granula.monitor.util.json.JsonUtil;

/**
 * Created by wlngai on 1/9/16.
 */
public class WorkerEndpoint extends UntypedActor {


    public static ActorSystem actorSystem;
    ActorId actorId = ActorId.getRandomId();
    MonitorWorker monitorWorker = null;
    RESTWorkerEndpoint restEndpoint;


    public static void InitEndpoint(MonitorWorker monitorWorker) {

        try {
            actorSystem = ActorSystem.create("profiler-worker", monitorWorker.config);
            actorSystem.actorOf(Props.create(WorkerEndpoint.class, monitorWorker), "profiler-worker");
        } catch (Exception e) {
            System.out.println("Failed to start worker" + " " +  e.getClass());
        }

    }

    public WorkerEndpoint(MonitorWorker monitorWorker) {
        this.monitorWorker = monitorWorker;
        monitorWorker.setEndpoint(this);

        startREST();
        startSocketEndpoint();
        sendRegisterRequest();
    }

    public void startSocketEndpoint() {
        final int port = MonitorService.config.getInt("akka.profiler.worker.socket.port");
        SocketEndpoint server = new SocketEndpoint(this, port);
        new Thread(server).start();
    }


    public void startREST() {
        int port = MonitorService.config.getInt("akka.profiler.worker.web.port");
        restEndpoint = new RESTWorkerEndpoint();

        restEndpoint.setEndpoint(this);
        restEndpoint.setPort(port);
        restEndpoint.start();
    }

    public void stopREST() {
        restEndpoint.stop();
    }

    private void sendRegisterRequest() {

        String path = monitorWorker.getMasterInfo().getPath();
        RegisterRequest registerRequest = new RegisterRequest(actorId, getSelf().path().toString());
        getContext().actorSelection(path).tell(registerRequest, getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        ActorRef sender = getSender();

        if (message instanceof HealthRequest) {
            HealthResponse healthResponse = new HealthResponse(actorId);
            healthResponse.setStatus("I'm high.");
            getSender().tell(healthResponse, getSelf());
        } else if (message instanceof RegisterResponse) {
            MasterInfo masterInfo = monitorWorker.getMasterInfo();
            masterInfo.setActorRef(sender());
        } else if (message instanceof TaskContext) {
            TaskContext taskContext = (TaskContext) message;
            taskContext.setSender(sender);
            monitorWorker.executeTask(taskContext);
        } else {
            unhandled(message);
        }
    }


    public void sendTask(TaskContext taskContext, ActorRef sender, ActorRef receiver) {
        System.out.println(String.format("Send: %s", JsonUtil.toJson(taskContext)));
        receiver.tell(taskContext, sender);
    }

    public void sendTask(TaskContext taskContext, ActorRef receiver) {
        System.out.println(String.format("Send: %s", JsonUtil.toJson(taskContext)));
        receiver.tell(taskContext, getSelf());
    }


    public void verifyWorkerHealth(WorkerInfo workerInfo) {

    }

    public MonitorWorker getMonitorWorker() {
        return monitorWorker;
    }

    public void setMonitorWorker(MonitorWorker monitorWorker) {
        this.monitorWorker = monitorWorker;
    }

    public void printSomething(int id, String metrics) {
        System.out.println("execute this."+ id + metrics);
    }

//    public void monitor(String jobId, int processId, String metric, int interval, int duration) {
//        monitorWorker.monitor(jobId, processId, metric, interval);
//    }

    public void startMonitorProcess(String processId) {
        monitorWorker.startMonitorProcess(processId);
    }

    public synchronized void processRequest(Request request) {

        switch (request.getType()) {
            case MonitorProcess :
                String pid = String.valueOf(((MonitorProcessRequest) request).getProcessId());
                startMonitorProcess(pid);
        }
    }

    public synchronized void processTask(TaskContext taskContext) {

    }

}
