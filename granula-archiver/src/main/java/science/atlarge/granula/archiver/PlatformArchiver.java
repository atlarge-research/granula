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

import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.entity.Execution;
import science.atlarge.granula.modeller.job.JobModel;
import science.atlarge.granula.modeller.job.Overview;
import science.atlarge.granula.modeller.source.JobDirectorySource;

import java.nio.file.Paths;

/**
 * Created by wlngai on 6/20/16.
 */
public class PlatformArchiver {

    public void buildArchive(Execution execution, JobModel jobModel) {

        Overview overview = new Overview();
        overview.setStartTime(execution.getStartTime());
        overview.setEndTime(execution.getEndTime());
        overview.setAlgorithm(execution.getAlgorithm());
        overview.setDataset(execution.getDataset());
        overview.setName(String.format("%s %s-%s [Proof of concepts]",
                execution.getPlatform().toUpperCase(), execution.getAlgorithm(), execution.getDataset()));
        overview.setDescription("Description not available yet.");


        JobDirectorySource jobDirSource = new JobDirectorySource(execution.getLogPath());
        jobDirSource.load();
        String arcPath = Paths.get(execution.getArcPath()).resolve("data").toAbsolutePath().toString();
        GranulaArchiver granulaArchiver = new GranulaArchiver(jobDirSource, jobModel, arcPath, BasicType.ArchiveFormat.JS);
        granulaArchiver.setOverview(overview);
        granulaArchiver.archive();
    }


}
