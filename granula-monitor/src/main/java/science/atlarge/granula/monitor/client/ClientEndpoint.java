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

import akka.actor.UntypedActor;
import science.atlarge.granula.monitor.info.ActorId;
import science.atlarge.granula.monitor.comm.request.OverviewRequest;
import science.atlarge.granula.monitor.comm.response.OverviewResponse;

/**
 * Created by wlngai on 1/9/16.
 */
public class ClientEndpoint extends UntypedActor {

    ActorId actorId = ActorId.getRandomId();
    MonitorClient profilerClient = null;

    public ClientEndpoint(MonitorClient profilerClient) {
        this.profilerClient = profilerClient;
        profilerClient.setClientEndpoint(this);
    }

    public void sendHealthRequest() {
        String path = profilerClient.getMasterInfo().getPath();
        getContext().actorSelection(path).tell(new OverviewRequest(), getSelf());

    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof OverviewResponse) {
            OverviewResponse overviewResponse = (OverviewResponse) message;
            System.out.println(overviewResponse.getMessage());
        } else {
            unhandled(message);
        }
    }

}
