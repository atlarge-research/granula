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

package nl.tudelft.pds.granula;

import nl.tudelft.pds.granula.archiver.process.ArchiveManager;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.process.HierachyManager;
import nl.tudelft.pds.granula.archiver.source.JobSource;
import nl.tudelft.pds.granula.archiver.process.RecordManager;
import nl.tudelft.pds.granula.modeller.model.job.JobModel;
import nl.tudelft.pds.granula.util.ProgressUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by wing on 2-2-15.
 */
public class GranulaArchiver {

    ProgressUtil progressUtil;

    // inputs
    JobSource jobSource;
    JobModel jobModel;
    String outputPath;

    // managers
    RecordManager recordManager;
    HierachyManager hierachyManager;
    ArchiveManager archiveManager;

    // deliverables
    Job job;

    public GranulaArchiver(JobSource jobSource, JobModel jobModel, String outputPath) {
        progressUtil = new ProgressUtil();
        this.jobSource = jobSource;
        this.jobModel = jobModel;
        this.outputPath = outputPath;
    }

    public void archive() {

        progressUtil.setStartTime(System.currentTimeMillis());
        assemble();

        progressUtil.setAssemblingTime(System.currentTimeMillis());
        write();

        progressUtil.setWritingTime(System.currentTimeMillis());

        progressUtil.setEndTime(System.currentTimeMillis());
        progressUtil.displayProcess();
    }

    public void assemble() {
        job = new Job();
        job.setModel(jobModel);

        recordManager = new RecordManager(job, jobSource);
        recordManager.extract();

        hierachyManager = new HierachyManager(job);
        hierachyManager.build();

        archiveManager = new ArchiveManager();
        archiveManager.build(job);
    }

    public void write() {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Job.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(job, new File(outputPath));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }



}
