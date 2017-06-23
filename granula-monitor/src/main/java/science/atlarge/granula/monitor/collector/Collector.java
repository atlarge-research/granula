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
package science.atlarge.granula.monitor.collector;

public abstract class Collector {

    enum Status{PREP, STANDBY, STARTED, KILLED};

    Status status;


    public Collector() {

    }

    public abstract boolean init();
    public abstract boolean run();
    public abstract boolean pause();
    public abstract boolean kill();
    public abstract void collect() throws Exception;

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}

