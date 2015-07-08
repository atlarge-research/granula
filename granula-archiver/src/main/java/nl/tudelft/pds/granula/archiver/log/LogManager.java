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

import nl.tudelft.pds.granula.util.TarExtractor;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created by wing on 30-1-15.
 */
public class LogManager {

    final String logTempDir = "/tmp/Granula/Log";
    WorkloadLog workloadLog;

    public LogManager(WorkloadLog workloadLog) {
        this.workloadLog = workloadLog;
    }

    public void decompressLog() {

        try {
            String tarFilePath = workloadLog.getTarFilePath();
            String workloadName = workloadLog.getName();
            String outputDir = workloadLog.getTmpDirPath();
            File outputDirFile = new File(outputDir);
            FileUtils.deleteDirectory(outputDirFile);
            outputDirFile.mkdirs();
            TarExtractor tarExtractor = new TarExtractor();
            tarExtractor.extract(tarFilePath, outputDir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WorkloadLog getWorkloadLog() {
        return workloadLog;
    }
}
