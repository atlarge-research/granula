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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import science.atlarge.granula.monitor.executor.MonitorService;

public class RestfulClient {
    int port = 0;
    String path = null;

    public RestfulClient() {
        String masterWebPort = "akka.profiler.master.web.port";
        String workerWebPort = "akka.profiler.worker.web.port";
        port = MonitorService.config.getInt(workerWebPort);
        if(port == 0) {
            throw new IllegalArgumentException("worker port is not found");
        }
        path = String.format("http://localhost:%s", port);
    }

    public void monitorProcess(int processId) {

        try {
            GetRequest request = Unirest.get(path + "/monitor-process?"
                    + "processId=" + processId
            );
            HttpResponse<JsonNode> jsonResponse = request.asJson();
//            HttpResponse<String> out = request.asString();
//            System.out.println(out.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
