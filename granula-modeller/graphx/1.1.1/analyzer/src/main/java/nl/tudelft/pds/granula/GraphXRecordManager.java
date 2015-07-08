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

import nl.tudelft.pds.granula.archiver.log.WorkloadLog;
import nl.tudelft.pds.granula.archiver.record.Record;
import nl.tudelft.pds.granula.archiver.record.RecordLocation;
import nl.tudelft.pds.granula.archiver.record.RecordManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 8-7-15.
 */
public class GraphXRecordManager extends RecordManager {

    public GraphXRecordManager(WorkloadLog workloadLog) {
        super(workloadLog);
    }

    public List<Record> extractRecordFromFile(File file) {

        List<Record> granularlogList = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                if(line.contains("Granular")) {
                    Record record = extractRecord(line);

                    RecordLocation trace = new RecordLocation();

                    String codeLocation;
                    String logFilePath;
                    if(false) { //TODO if supported
                        codeLocation = line.split("\\) - Granular")[0].split(" \\(")[1];
                    }

                    codeLocation = "";
                    logFilePath = "YarnLog/" + file.getAbsolutePath().split("YarnLog/")[1];

                    trace.setLocation(logFilePath, lineCount, codeLocation);
                    record.setRecordLocation(trace);

                    granularlogList.add(record);
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return granularlogList;
    }

    public Record extractRecord(String line) {
        Record record = new Record();

        String granularLog = line.split("Granular - ")[1];
        String[] recordAttrs = granularLog.split("\\s+");

        for (String recordAttr : recordAttrs) {
            if (recordAttr.contains(":")) {
                String[] attrKeyValue = recordAttr.split(":");
                if (attrKeyValue.length == 2) {

                    String name = attrKeyValue[0];
                    String value = attrKeyValue[1];
                    String unescapedValue = value.replaceAll("\\[COLON\\]", ":").replaceAll("\\[SPACE\\]", " ");

                    record.addRecordInfo(name, unescapedValue);
                } else {
                    record.addRecordInfo(attrKeyValue[0], "");
                }
            }
        }
        return record;
    }


}
