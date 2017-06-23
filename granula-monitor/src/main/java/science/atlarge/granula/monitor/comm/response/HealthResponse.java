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
package science.atlarge.granula.monitor.comm.response;

import science.atlarge.granula.monitor.info.ActorId;

/**
 * Created by wlngai on 1/9/16.
 */
public class HealthResponse extends Response {
    ActorId actorId;
    String status;

    public HealthResponse(ActorId actorId) {
        this.actorId = actorId;
    }

    public ActorId getActorId() {
        return actorId;
    }

    public void setActorId(ActorId actorId) {
        this.actorId = actorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
