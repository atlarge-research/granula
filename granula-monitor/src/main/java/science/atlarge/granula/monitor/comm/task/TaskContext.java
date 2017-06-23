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
package science.atlarge.granula.monitor.comm.task;

import akka.actor.ActorRef;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.util.UUIDUtil;

import java.io.Serializable;

public abstract class TaskContext implements Serializable {
    protected String id;
    public TaskType type;
    protected transient ActorRef sender;

    public TaskContext() {
        this.id = "t" + UUIDUtil.setNumericalID();
    }

    public abstract void executeAt(MonitorService monitorService);

    public String getId() {
        return id;
    }


    public ActorRef getSender() {
        return sender;
    }

    public void setSender(ActorRef sender) {
        this.sender = sender;
    }

    public void setId(String id) {
        this.id = id;
    }
}
