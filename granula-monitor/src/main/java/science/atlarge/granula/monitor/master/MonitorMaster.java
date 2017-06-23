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
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigValueFactory;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.info.ProcessInfo;
import science.atlarge.granula.monitor.info.WorkerInfo;
import science.atlarge.granula.monitor.comm.response.HealthResponse;
import science.atlarge.granula.monitor.comm.request.RegisterRequest;
import science.atlarge.granula.monitor.info.ActorId;
import science.atlarge.granula.monitor.util.json.JsonUtil;

import java.util.*;


public class MonitorMaster extends MonitorService {
    Map<ActorId, WorkerInfo> workers;
    MasterEndpoint endpoint;
    List<ActorRef> clients = new ArrayList<>();

    public MonitorMaster() {
        super();
    }

    public void init() {
        isMaster = true;
        workers = new HashMap<>();

        int port = config.getInt("akka.profiler.master.port");
        String ip = config.getString("akka.profiler.master.ip");
        config = config.withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port));
        config = config.withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(ip));
        final ActorSystem system = ActorSystem.create("profiler-master", config);
        final ActorRef master = system.actorOf(Props.create(MasterEndpoint.class, this), "profiler-master");


        System.out.println("Started Profiler Master");

    }

    public void registerWorker(RegisterRequest registerRequest, ActorRef actorRef) {
        WorkerInfo workerInfo = new WorkerInfo();
        workerInfo.setPath(actorRef.path().toString());
        workerInfo.setId(registerRequest.getWorkerId());
        workerInfo.setActorRef(actorRef);
        workerInfo.setIp(ProcessInfo.Path2IpAddress(workerInfo.getPath()));
        workerInfo.setStatus(ProcessInfo.Status.LIVE);
        workers.put(workerInfo.getId(), workerInfo);
        System.out.println("Register: " + workerInfo.toString());
    }

    public void deregisterWorker() {

    }

    public void getWorkersDescription() {

    }

    public void verifyWorkerHealth(ActorRef user) {
        clients.add(user);
        for (WorkerInfo info : workers.values()) {
            info.setStatus(ProcessInfo.Status.UNKNOWN);
            endpoint.verifyWorkerHealth(info);
        }

    }

    public void verifyWorkerHealth() {
        for (WorkerInfo info : workers.values()) {
            info.setStatus(ProcessInfo.Status.UNKNOWN);
            endpoint.verifyWorkerHealth(info);
        }

    }

    public void reportWorkerHealth(HealthResponse healthResponse) {
        WorkerInfo workerInfo = workers.get(healthResponse.getActorId());
        workerInfo.setStatus(ProcessInfo.Status.LIVE);
        System.out.println(
                String.format("Master got a health reponse from %s, who says: \"%s\"",
                        workerInfo, healthResponse.getStatus()));
        int liveCount = 0, deadCount = 0, lostCount = 0, unknownCount = 0;
        for (WorkerInfo info : workers.values()) {
            switch (info.getStatus()) {
                case LIVE:
                    liveCount++;
                    break;
                case DEAD:
                    deadCount++;
                    break;
                case LOST:
                    lostCount++;
                    break;
                case UNKNOWN:
                    unknownCount++;
                    break;
                default:
                    break;
            }
        }
        String healthReport = String.format("Worker status: LIVE(%s), DEAD(%s), LOST(%s), UNKNOWN(%s)",
                liveCount, deadCount, lostCount, unknownCount);
        System.out.println(healthReport);

        endpoint.reportOverview(clients.get(0), healthReport);

        if(unknownCount == 0) {
            clients.clear();
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


    public void benchmarkEnvironment() {

    }

    public void setMasterEndpoint(MasterEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Map<ActorId, WorkerInfo> getWorkers() {
        return workers;
    }

    public MasterEndpoint getEndpoint() {
        return endpoint;
    }
}
