/*
 * Copyright 2015 Delft University of Technology
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

package nl.tudelft.pds.granula.archiver.entity.operation;

import nl.tudelft.pds.granula.archiver.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 12-3-15.
 */
public class Workload extends Entity {

    List<Job> jobs;

    public Workload() {
        super();
        this.jobs = new ArrayList<>();
    }

    public void addJob(Job job) {
        this.jobs.add(job);
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public String exportBasic() {
        return String.format("<Workload uuid=\"%s\"></Workload>", uuid);
    }

    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<Workload uuid=\"%s\">", uuid));

        for (Job job : jobs) {
            stringBuilder.append(job.export());
        }

        stringBuilder.append(exportInfos());
        stringBuilder.append(exportVisuals());

        stringBuilder.append("</Workload>");
        return stringBuilder.toString();
    }

}
