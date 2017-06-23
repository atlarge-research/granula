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

import ro.pippo.core.Application;

/**
 * Created by wlngai on 1/10/16.
 */
public class PippoApplication extends Application {

    WorkerEndpoint workerEndpoint;

    public PippoApplication(WorkerEndpoint workerEndpoint) {
        this.workerEndpoint = workerEndpoint;
    }

    @Override
    protected void onInit() {
        // send 'Hello World' as response
        GET("/", (routeContext) -> {
            routeContext.send("Hello World");
            System.out.println("hello");
        });

//        GET("/monitor-process", (routeContext) -> {
//            String jobId = routeContext.getParameter("jobId").toString();
//            int processId = routeContext.getParameter("processId").toInt();
//            String metric = routeContext.getParameter("metric").toString();
//            int interval = routeContext.getParameter("interval").toInt();
//            int duration = routeContext.getParameter("duration").toInt();
//            RestfulResponse restfulResponse = new RestfulResponse("monitoring process");
//            Contact contact = Contact.createContact();
//            workerEndpoint.monitor(jobId, processId, metric, interval, duration);
//            routeContext.json().send(contact);
//        });

        GET("/monitor-process", (routeContext) -> {
            String processId = routeContext.getParameter("processId").toString();
//            RestfulResponse restfulResponse = new RestfulResponse("monitoring process");
            workerEndpoint.startMonitorProcess(processId);

//            routeContext.json().send("scucess");
        });

        GET("/monitor-job", (routeContext) -> {
            String jobId = routeContext.getParameter("jobId").toString();
//            RestfulResponse restfulResponse = new RestfulResponse("monitoring job");
//            workerEndpoint.monitorJob(jobId);
//            routeContext.json().send(contact);
        });
    }
}
