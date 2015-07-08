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

package nl.tudelft.pds.granula.archiver.record;

import nl.tudelft.pds.granula.archiver.entity.info.TimeSeries;
import nl.tudelft.pds.granula.archiver.log.WorkloadLog;
import nl.tudelft.pds.granula.util.RrdManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wing on 2-2-15.
 */
public class RecordManager {

    protected WorkloadLog workloadLog;

    public RecordManager(WorkloadLog workloadLog) {
        this.workloadLog = workloadLog;
    }

    public List<JobRecord> extract() {

        List<JobRecord> jobRecords = new ArrayList<>();

        List<List<String>> jobOLFCollections = getJobOLFCollections();

        for (List<String> jobOLFCollection : jobOLFCollections) {
            jobRecords.add(extractJobRecord(jobOLFCollection));
        }

        return jobRecords;
    }

    public JobRecord extractJobRecord(List<String> jobOLFCollection) {

        final JobRecord jobRecord = new JobRecord();

        for (String jobOLogFilePaths : jobOLFCollection) {
            List<Record> records = extractRecordFromFile(new File(jobOLogFilePaths));
            jobRecord.addRecords(records);
        }

        jobRecord.sort();

        final long startTime = Long.parseLong(jobRecord.getRecords().get(0).getAttr("Timestamp"));
        final long endTime = Long.parseLong(jobRecord.getRecords().get(jobRecord.getRecords().size() - 1).getAttr("Timestamp"));

//        ExecutorUtil executorUtil = new ExecutorUtil();
//
//        for (String rLogFilePath : getResourceLogFilePaths()) {
//
//            final String fRLFPath = rLogFilePath;
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

    public List<List<String>> getJobOLFCollections() {

        List<List<String>> jobOLFCollections = new ArrayList<>();

        List<File> jobLogDirs = Arrays.asList(new File(workloadLog.getTmpDirPath() + "/YarnLog/").listFiles());
        for (File jobLogDir : jobLogDirs) {
            List<String> jobOLFCollection = new ArrayList<>();
            List<File> olfs = new ArrayList<>(
                    FileUtils.listFilesAndDirs(jobLogDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE));
            for (File olf : olfs) {
                if(olf.isFile()) {
                    jobOLFCollection.add(olf.getAbsolutePath());
                }
            }
            jobOLFCollections.add(jobOLFCollection);
        }
        return jobOLFCollections;
    }

    public List<String> getResourceLogFilePaths() {

        List<String> rLogFilePaths = new ArrayList<>();

        File resourceLogDir = new File(workloadLog.getTmpDirPath() + "/GangliaLog/");
        List<File> rLogFiles = new ArrayList<>(FileUtils.listFilesAndDirs(resourceLogDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE));


        for (File rLogFile : rLogFiles) {
            if(!rLogFile.getAbsolutePath().contains("__SummaryInfo__")) {
                if(rLogFile.isFile()) {
                    rLogFilePaths.add(rLogFile.getAbsolutePath());
                }
            }
        }

        return rLogFilePaths;
    }

    public ResourceRecord extractResourceRecordFromFile(String rrdFilePath, long startTime, long endTime) {

        ResourceRecord resourceRecord = new ResourceRecord();

        RecordLocation trace = new RecordLocation();
        trace.setLocation(rrdFilePath.replace(workloadLog.getTmpDirPath(), ""));
        resourceRecord.setRecordLocation(trace);

        TimeSeries timeSeries = RrdManager.extract(rrdFilePath, startTime, endTime);
        resourceRecord.setTimeSeries(timeSeries);
        return resourceRecord;
    }



    public List<Record> extractRecordFromFile(File file) {
        return null;
    }

    public Record extractRecord(String line) {
        return null;
    }


}
