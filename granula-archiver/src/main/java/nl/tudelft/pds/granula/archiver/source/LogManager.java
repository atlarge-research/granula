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

package nl.tudelft.pds.granula.archiver.source;

/**
 * Created by wing on 30-1-15.
 */
public class LogManager {

    final String logTempDir = "/tmp/Granula/Log";
    WorkloadFileSource workloadSource;

    public LogManager(WorkloadFileSource workloadSource) {
        this.workloadSource = workloadSource;
    }

    public WorkloadFileSource getWorkloadSource() {
        return workloadSource;
    }
}
