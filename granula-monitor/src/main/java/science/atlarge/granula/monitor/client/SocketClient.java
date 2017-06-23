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
package science.atlarge.granula.monitor.client;

import science.atlarge.granula.monitor.comm.task.CollectContext;
import science.atlarge.granula.monitor.comm.task.MonitorContext;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.util.json.JsonUtil;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by wlngai on 9-5-16.
 */
public class SocketClient {

    int port = 0;
    String host = null;

    public SocketClient() {
        String masterWebPort = "akka.profiler.master.web.port";
        String workerSocketPort = "akka.profiler.worker.socket.port";
        port = MonitorService.config.getInt(workerSocketPort);
        if(port == 0) {
            throw new IllegalArgumentException("worker port is not found");
        }
        host = "localhost";
    }


    public void monitorProcess(int processId) {

        MonitorContext monitorContext = new MonitorContext();
        monitorContext.setState(MonitorContext.State.StartMonitorProcess);
        monitorContext.setProcessId(String.valueOf(processId));
        submitTask(monitorContext);

    }

    public void registerJob(String jobId) {
        try {
            Socket clientSocket = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            MonitorContext monitorContext = new MonitorContext();
            monitorContext.setState(MonitorContext.State.RegisterJob);
            monitorContext.setJobId(jobId);
            String requestJson = JsonUtil.toJson(monitorContext);
            os.writeBytes(requestJson + '\n');
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkProcessToJob(String processId, String jobId) {
        try {
            Socket clientSocket = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            MonitorContext monitorContext = new MonitorContext();
            monitorContext.setState(MonitorContext.State.LinkProcess);
            monitorContext.setProcessId(processId);
            monitorContext.setJobId(jobId);


            String requestJson = JsonUtil.toJson(monitorContext);
            os.writeBytes(requestJson + '\n');
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkMachineToJob(String machineId, String jobId) {
        try {
            Socket clientSocket = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            MonitorContext monitorContext = new MonitorContext();
            monitorContext.setState(MonitorContext.State.LinkNode);
            monitorContext.setMachineId(machineId);
            monitorContext.setJobId(jobId);


            String requestJson = JsonUtil.toJson(monitorContext);
            os.writeBytes(requestJson + '\n');
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void collectJob(String jobId, String outPath) {

        CollectContext collectContext = new CollectContext();
        collectContext.setState(CollectContext.State.Submit);
        collectContext.setJobId(jobId);
        collectContext.setOutPath(outPath);
        submitTask(collectContext);
    }

    public void submitTask(TaskContext taskContext) {
        try {
            Socket clientSocket = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            String requestJson = JsonUtil.toJson(taskContext);
            os.writeBytes(requestJson + '\n');
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
