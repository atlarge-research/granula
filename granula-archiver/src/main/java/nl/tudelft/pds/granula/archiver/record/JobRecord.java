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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wing on 2-2-15.
 */
public class JobRecord {
    List<Record> records;
    List<ResourceRecord> resourceRecords;

    public JobRecord() {
        this.records = new ArrayList<>();
        this.resourceRecords = new ArrayList<>();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public void addRecords(List<Record> records) {
        this.records.addAll(records);
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<ResourceRecord> getResourceRecords() {
        return resourceRecords;
    }

    public void addResourceRecord(ResourceRecord resourceRecord) {
        resourceRecords.add(resourceRecord);
    }

    public void sort() {
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record record1, Record record2) {
                if(Long.parseLong(record1.getAttr("Timestamp")) > Long.parseLong(record2.getAttr("Timestamp"))) {
                    return 1;
                }
                else if(Long.parseLong(record2.getAttr("Timestamp")) > Long.parseLong(record1.getAttr("Timestamp"))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

}
