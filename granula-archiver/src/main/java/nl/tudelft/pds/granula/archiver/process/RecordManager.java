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

package nl.tudelft.pds.granula.archiver.process;

import nl.tudelft.pds.granula.archiver.entity.info.TimeSeries;
import nl.tudelft.pds.granula.archiver.entity.operation.Job;
import nl.tudelft.pds.granula.archiver.source.JobSource;
import nl.tudelft.pds.granula.archiver.source.record.JobRecord;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordLocation;
import nl.tudelft.pds.granula.archiver.source.record.ResourceRecord;
import nl.tudelft.pds.granula.modeller.model.job.JobModel;
import nl.tudelft.pds.granula.modeller.rule.extraction.ExtractionRule;
import nl.tudelft.pds.granula.util.RrdManager;

import java.io.*;
import java.util.List;

/**
 * Created by wing on 2-2-15.
 */
public class RecordManager {

    protected Job job;
    protected JobSource jobSource;


    public RecordManager(Job job, JobSource embeddedJobSource) {
        this.job = job;
        this.jobSource = embeddedJobSource;
    }


    public void extract() {
        job.setJobRecord(extractJobRecord(jobSource));
    }


    public JobRecord extractJobRecord(JobSource jobSource) {

        final JobRecord jobRecord = new JobRecord();


        for (InputStream inputStream :jobSource.getOperationSource().getInputStreams()) {
            List<Record> records = extractOperationRecord(inputStream);
            jobRecord.addRecords(records);
        }

        jobRecord.sort();

//        final long startTime = Long.parseLong(jobRecord.getRecords().get(0).getAttr("Timestamp"));
//        final long endTime = Long.parseLong(jobRecord.getRecords().get(jobRecord.getRecords().size() - 1).getAttr("Timestamp"));
//
//        ExecutorUtil executorUtil = new ExecutorUtil();
//
//        for (File rLogFilePath : jobDataSource.getUtilLogFiles()) {
//
//            final String fRLFPath = rLogFilePath.getAbsolutePath();
//            executorUtil.execute(new Runnable() {
//                @Override
//                public void run() {
//                    ResourceRecord resourceRecord = extractResourceRecordFromFile(fRLFPath, startTime, endTime);
//                    jobRecord.addResourceRecord(resourceRecord);
//                    if(resourceRecord.getTimeSeries().size() <= -1) {
//                        System.out.println(String.format("%s has datapoints only %s between %s and %s", fRLFPath, resourceRecord.getTimeSeries().size(), startTime, endTime));
//                    }
//                }
//            });
//        }
//        executorUtil.setBarrier();

        return jobRecord;
    }


    public ResourceRecord extractResourceRecordFromFile(String rrdFilePath, long startTime, long endTime) {

        ResourceRecord resourceRecord = new ResourceRecord();

        RecordLocation trace = new RecordLocation();
//        trace.setLocation(rrdFilePath.replace(workloadLog.getTmpDirPath(), ""));
        trace.setLocation("unknown");
        resourceRecord.setRecordLocation(trace);

        TimeSeries timeSeries = RrdManager.extract(rrdFilePath, startTime, endTime);
        resourceRecord.setTimeSeries(timeSeries);
        return resourceRecord;
    }



    public List<Record> extractOperationRecord(InputStream inputStream) {
        JobModel jobModel = (JobModel) job.getModel();
        ExtractionRule extractionRule = jobModel.getExtractionRules().get(0);
        return extractionRule.extractRecordFromInputStream(inputStream);
    }



}
