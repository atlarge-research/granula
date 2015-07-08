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

import nl.tudelft.pds.granula.archiver.entity.info.TimeSeries;
import nl.tudelft.pds.granula.archiver.log.WorkloadLog;
import nl.tudelft.pds.granula.archiver.record.Record;
import nl.tudelft.pds.granula.archiver.record.RecordLocation;
import nl.tudelft.pds.granula.archiver.record.RecordManager;
import nl.tudelft.pds.granula.archiver.record.ResourceRecord;
import nl.tudelft.pds.granula.util.RrdManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 8-7-15.
 */
public class GiraphRecordManager extends RecordManager {

    public GiraphRecordManager(WorkloadLog workloadLog) {
        super(workloadLog);
    }

    public List<Record> extractRecordFromFile(File file) {

        List<Record> granulalogList = new ArrayList<>();

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
                    String codeLocation = "";

                    String[] lineParts = line.split("\\) - Granular");
                    if(lineParts.length > 1) {
                        String[] prefixParts = lineParts[0].split(" \\(");
                        if(prefixParts.length > 1) {
                            codeLocation = prefixParts[1];
                        }
                    } else {
                        String[] driverLineParts = line.split("\\s+");
                        if(driverLineParts.length > 3) {
                            codeLocation = driverLineParts[3].replace(":", "");
                        }
                    }

                    String logFilePath = "YarnLog/" + file.getAbsolutePath().split("YarnLog/")[1];
                    trace.setLocation(logFilePath, lineCount, codeLocation);
                    record.setRecordLocation(trace);

                    granulalogList.add(record);
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return granulalogList;
    }

    public Record extractRecord(String line) {
        Record record = new Record();

        String granulaLog = line.split("Granular")[1];
        String[] recordAttrs = granulaLog.split("\\s+");

        for (String recordAttr : recordAttrs) {
            if (recordAttr.contains(":")) {
                String[] attrKeyValue = recordAttr.split(":");
                if (attrKeyValue.length == 2) {

                    String name = attrKeyValue[0];
                    String value = attrKeyValue[1];
                    String unescapedValue = value.replaceAll("\\[COLON\\]", ":");

                    record.addRecordInfo(name, unescapedValue);
                } else {
                    record.addRecordInfo(attrKeyValue[0], "");
                }
            }
        }
        return record;
    }


}
