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

import com.google.gson.JsonParser;
import science.atlarge.granula.monitor.comm.task.TaskContext;
import science.atlarge.granula.monitor.comm.task.TaskType;
import science.atlarge.granula.monitor.util.json.JsonUtil;

import java.io.*;
import java.net.Socket;


public class SocketRequestProcesser implements Runnable {

    protected WorkerEndpoint workerEndpoint;
    protected Socket clientSocket = null;

    public SocketRequestProcesser(Socket clientSocket, WorkerEndpoint workerEndpoint) {
        this.clientSocket = clientSocket;
        this.workerEndpoint = workerEndpoint;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(input));
            String line = inFromUser.readLine();
            System.out.println(line);
            String taskType = (new JsonParser()).parse(line).getAsJsonObject().get("type").getAsString();
            TaskContext taskContext = (TaskContext) JsonUtil.fromJson(line, TaskType.valueOf(taskType).getClazz());
            taskContext.setSender(null);
            workerEndpoint.getMonitorWorker().executeTask(taskContext);
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
