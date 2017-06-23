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
package science.atlarge.granula.monitor.info;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by wlngai on 1/9/16.
 */
public class ActorId implements Serializable {
    String id;

    public ActorId(String id) {
        this.id = id;
    }

    public String getShortId() {
        return id.substring(0, 3);
    }

    public static ActorId getRandomId() {
        return new ActorId(String.valueOf(new Random().nextInt(10000000) + 1000000));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorId actorId = (ActorId) o;
        return !(id != null ? !id.equals(actorId.id) : actorId.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
