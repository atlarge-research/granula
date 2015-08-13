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

package nl.tudelft.pds.granula.archiver.source.record;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wing on 30-1-15.
 */
public class Record {
    Map<String, String> recordAttrs = new LinkedHashMap<>();
    RecordLocation recordLocation;

    public void addRecordInfo(String name, String value) {
        if(recordAttrs.containsKey(name)) {
            throw new IllegalStateException();
        }
        else {
            recordAttrs.put(name, value);
        }
    }

    public String getAttr(String name) {
        if(recordAttrs.containsKey(name)) {
            return recordAttrs.get(name);
        }
        else {
            throw new IllegalStateException();
        }
    }

    public String getRecordAttrs() {

        StringBuilder str = new StringBuilder();

        for (Map.Entry<String, String> attribute : recordAttrs.entrySet()) {
            str.append(String.format("[%s, %s]\n", attribute.getKey(), attribute.getValue()));
        }
        return str.toString();
    }

    public RecordLocation getRecordLocation() {
        return recordLocation;
    }

    public void setRecordLocation(RecordLocation recordLocation) {
        this.recordLocation = recordLocation;
    }

    public static boolean Match(Record recordLeft, Record recordRight, String recordInfoName) {
        return recordLeft.getAttr(recordInfoName).equals(recordRight.getAttr(recordInfoName));
    }

    public boolean isOf(String name, String value) {
        if(recordAttrs.containsKey(RecordInfo.InfoName)) {
            return recordAttrs.get(name).equals(value);
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return "Record \n" +
                getRecordAttrs() +
                "[" + recordLocation + ']';
    }
}
