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

import ro.pippo.core.Pippo;

/**
 * Created by wlngai on 5/23/16.
 */
public class RESTMasterEndpoint {

    MasterEndpoint endpoint;
    int port;

    Pippo pippo;

    public void start() {

        (new Thread() {
            public void run() {
                System.out.println("Started worker web at port "+ port);
                pippo = new Pippo(new PippoApplication(endpoint));
                pippo.getServer().getSettings().port(port);
                pippo.start();

            }
        }).start();
    }

    public void stop() {
        pippo.stop();
    }

    public MasterEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(MasterEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
