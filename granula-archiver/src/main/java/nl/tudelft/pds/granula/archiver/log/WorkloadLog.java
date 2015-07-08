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

package nl.tudelft.pds.granula.archiver.log;

/**
 * Created by wing on 30-1-15.
 */
public class WorkloadLog {
    String name;
    String tarFilePath;
    String tmpDirPath;

    final String tmpParentDirPath = "/tmp/Granula/Log";

    public WorkloadLog(String name, String tarFilePath) {
        this.name = name;
        this.tarFilePath = tarFilePath;
        this.tmpDirPath = String.format("%s/%s", tmpParentDirPath, name);
    }

    public String getName() {
        return name;
    }

    public String getTarFilePath() {
        return tarFilePath;
    }

    public String getTmpDirPath() {
        return tmpDirPath;
    }
}
