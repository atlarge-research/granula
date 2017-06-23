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

package science.atlarge.granula.archiver;

import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.modeller.platform.Platform;
import science.atlarge.granula.modeller.platform.PlatformModel;
import science.atlarge.granula.modeller.rule.extraction.ExtractionRule;
import science.atlarge.granula.modeller.source.DataStream;
import science.atlarge.granula.modeller.source.JobSource;
import science.atlarge.granula.modeller.source.log.Log;

import java.util.List;

public class SourceManager {

    protected Job job;
    protected JobSource jobSource;


    public SourceManager(Job job, JobSource jobSource) {
        this.job = job;
        this.jobSource = jobSource;
    }

    public void extract() {
        Platform platform = job.getPlatform();

        for (DataStream dataStream :jobSource.getSysSource().getDataStreams()) {
            List<Log> logs = extractSystemLog(dataStream);
            for (Log log : logs) {
                platform.addLog(log);
            }
        }
    }



    public List<Log> extractSystemLog(DataStream dataStream) {
        PlatformModel platformModel = (PlatformModel) job.getPlatform().getModel();
        ExtractionRule extractionRule = platformModel.getExtractionRules().get(0);
        return extractionRule.extractLogFromInputStream(dataStream);
    }



}
