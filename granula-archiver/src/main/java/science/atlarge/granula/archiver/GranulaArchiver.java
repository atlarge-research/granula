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

import science.atlarge.granula.modeller.entity.BasicType.ArchiveFormat;
import science.atlarge.granula.modeller.job.Job;
import science.atlarge.granula.modeller.job.JobModel;
import science.atlarge.granula.modeller.job.Overview;
import science.atlarge.granula.modeller.platform.Platform;
import science.atlarge.granula.util.FileUtil;
import science.atlarge.granula.modeller.source.JobSource;
import science.atlarge.granula.util.json.JsonUtil;

import java.nio.file.Paths;

/**
 * Created by wing on 2-2-15.
 */
public class GranulaArchiver {

    // inputs
    JobSource source;
    Overview overview;
    JobModel jobModel;
    String outputPath;
    ArchiveFormat format;

    // managers
    SourceManager sourceManager;
    ArchiveManager archiveManager;

    // deliverables
    Job job;

    public GranulaArchiver(JobSource source, JobModel jobModel, String outputPath, ArchiveFormat format) {
        this.source = source;
        this.jobModel = jobModel;
        this.outputPath = outputPath;
        this.format = format;
    }

    public GranulaArchiver(JobSource source, JobModel jobModel, String outputPath) {
        this(source, jobModel, outputPath, ArchiveFormat.JSON);
    }

    public void archive() {
        assemble();
        write();
    }


    public void assemble() {
        job = new Job();

        if (overview == null) {
            overview = new Overview();
        }
        job.setOverview(overview);


        Platform platform = new Platform();
        platform.setModel(jobModel.getPlatformModel());
        platform.setJob(job);
        job.setPlatform(platform);


        sourceManager = new SourceManager(job, source);
        sourceManager.extract();

        archiveManager = new ArchiveManager(job);
        archiveManager.build();

    }

    public void write() {
        String systemArcFile;
        String overviewArcFile;
        switch (format) {
            case JS:
                overviewArcFile = "var jobMetadata = " +
                        JsonUtil.toPrettyJson(job.getOverview());
                FileUtil.writeFile(overviewArcFile,
                        Paths.get(outputPath).resolve("meta-arc.js"));

                systemArcFile = "var jobOperations = " +
                        JsonUtil.toPrettyJson(job.getPlatform());
                FileUtil.writeFile(systemArcFile,
                        Paths.get(outputPath).resolve("sys-arc.js"));
                break;
            case JSON:
                overviewArcFile = JsonUtil.toPrettyJson(job.getOverview());
                FileUtil.writeFile(overviewArcFile,
                        Paths.get(outputPath).resolve("meta-arc.json"));

                systemArcFile = JsonUtil.toPrettyJson(job.getPlatform());
                FileUtil.writeFile(systemArcFile,
                        Paths.get(outputPath).resolve("sys-arc.json"));
                break;
            default:
                throw new IllegalStateException("Wrong archiving format.");
        }

    }

    public void setOverview(Overview overview) {
        this.overview = overview;
    }
}
