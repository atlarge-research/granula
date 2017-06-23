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

import ro.pippo.core.Application;

/**
 * Created by wlngai on 1/10/16.
 */
public class PippoApplication extends Application {

    MasterEndpoint masterEndpoint;

    public PippoApplication(MasterEndpoint masterEndpoint) {
        this.masterEndpoint = masterEndpoint;
    }

    @Override
    protected void onInit() {
        // send 'Hello World' as response
        GET("/", (routeContext) -> {
            routeContext.send("Hello World");
            System.out.println("hello");
        });

        GET("/monitor-process", (routeContext) -> {
            int processId = routeContext.getParameter("processId").toInt();
            String metric = routeContext.getParameter("metric").toString();
//            RestfulResponse restfulResponse = new RestfulResponse("monitoring process");
//            endpoint.monitor(processId, metric);
//            routeContext.json().send(contact);
        });
    }
}
