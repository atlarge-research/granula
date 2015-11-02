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
import nl.tudelft.pds.granula.archiver.source.DataStream;
import nl.tudelft.pds.granula.archiver.source.JobSource;
import nl.tudelft.pds.granula.archiver.source.record.JobRecord;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordLocation;
import nl.tudelft.pds.granula.archiver.source.record.UtilRecord;
import nl.tudelft.pds.granula.modeller.model.job.JobModel;
import nl.tudelft.pds.granula.modeller.rule.extraction.ExtractionRule;
import nl.tudelft.pds.granula.util.ExecutorUtil;
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


        for (DataStream dataStream :jobSource.getOperationSource().getDataStreams()) {
            List<Record> records = extractOperationRecord(dataStream.getInputStream());
            jobRecord.addRecords(records);
        }

        jobRecord.sort();

        final long startTime = Long.parseLong(jobRecord.getRecords().get(0).getAttr("Timestamp"));
        final long endTime = Long.parseLong(jobRecord.getRecords().get(jobRecord.getRecords().size() - 1).getAttr("Timestamp"));

        ExecutorUtil executorUtil = new ExecutorUtil();

        for (final DataStream dataStream : jobSource.getUtilSource().getDataStreams()) {
            executorUtil.execute(new Runnable() {
                @Override
                public void run() {

                    if(!dataStream.getPath().contains("SummaryInfo")) {
                        UtilRecord utilRecord = extractUtilizationRecord(dataStream, startTime, endTime);
                        jobRecord.addUtilRecord(utilRecord);
                        if(utilRecord.getTimeSeries().size() <= -1) {
                            System.out.println(String.format("%s has datapoints only %s between %s and %s", dataStream.getPath(), utilRecord.getTimeSeries().size(), startTime, endTime));
                        }
                    }

                }
            });
        }

        executorUtil.setBarrier();

        return jobRecord;
    }


    public UtilRecord extractUtilizationRecord(DataStream dataStream, long startTime, long endTime) {

        UtilRecord utilRecord = new UtilRecord();

        RecordLocation trace = new RecordLocation();
//        trace.setLocation(rrdFilePath.replace(workloadLog.getTmpDirPath(), ""));
        trace.setLocation(dataStream.getPath());
        utilRecord.setRecordLocation(trace);

        TimeSeries timeSeries = RrdManager.extract(dataStream, startTime, endTime);
        utilRecord.setTimeSeries(timeSeries);
        return utilRecord;
    }



    public List<Record> extractOperationRecord(InputStream inputStream) {
        JobModel jobModel = (JobModel) job.getModel();
        ExtractionRule extractionRule = jobModel.getExtractionRules().get(0);
        return extractionRule.extractRecordFromInputStream(inputStream);
    }



}
