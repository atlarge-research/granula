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
package science.atlarge.granula.monitor.master;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import science.atlarge.granula.monitor.comm.response.RegisterResponse;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.info.WorkerInfo;
import science.atlarge.granula.monitor.comm.request.HealthRequest;
import science.atlarge.granula.monitor.comm.request.OverviewRequest;
import science.atlarge.granula.monitor.comm.response.OverviewResponse;
import science.atlarge.granula.monitor.comm.request.RegisterRequest;
import science.atlarge.granula.monitor.comm.response.HealthResponse;
import science.atlarge.granula.monitor.util.json.JsonUtil;

/**
 * Created by wlngai on 1/9/16.
 */
public class MasterEndpoint extends UntypedActor {

    MonitorMaster monitorMaster;
    RESTMasterEndpoint restEndpoint;

    public MasterEndpoint(MonitorMaster monitorMaster) {
        this.monitorMaster = monitorMaster;
        monitorMaster.setMasterEndpoint(this);

        startREST();
    }

    public void startREST() {
        int port = MonitorService.config.getInt("akka.profiler.master.web.port");

        restEndpoint = new RESTMasterEndpoint();
        restEndpoint.setEndpoint(this);
        restEndpoint.setPort(port);
        restEndpoint.start();
    }

    public void stopREST() {
        restEndpoint.stop();
    }


    @Override
    public void onReceive(Object message) throws Exception {

        ActorRef sender = getSender();
        if (message instanceof HealthResponse) {
            HealthResponse healthResponse = (HealthResponse) message;
            monitorMaster.reportWorkerHealth(healthResponse);
        } else if (message instanceof OverviewRequest) {
            monitorMaster.verifyWorkerHealth(getSender());
        } else if (message instanceof RegisterRequest) {
            RegisterRequest registerRequest = (RegisterRequest) message;
            monitorMaster.registerWorker(registerRequest, getSender());
            sender().tell(new RegisterResponse(), self());
        } else if (message instanceof TaskContext) {
            TaskContext taskContext = (TaskContext) message;
            taskContext.setSender(sender);
            monitorMaster.executeTask(taskContext);
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
        receiver.tell(taskContext, receiver);
    }

    public void verifyWorkerHealth(WorkerInfo workerInfo) {
        workerInfo.getActorRef().tell(new HealthRequest(), getSelf());
    }

    public void reportOverview(ActorRef actorRef, String report) {
        OverviewResponse overviewResponse = new OverviewResponse();
        overviewResponse.setMessage(report);
        actorRef.tell(overviewResponse, getSelf());
    }


}
