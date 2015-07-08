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

package nl.tudelft.pds.granula.archiver.entity.operation;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.record.Record;
import nl.tudelft.pds.granula.archiver.record.RecordInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class JobStatistics {

    protected Job job;
    public Distribution numRecordsByRecordType;
    public Distribution numOperationsByOperationType;
    public Distribution numInfosByInfoType;
    public Distribution numDirectCitationsByInfo;
    public Distribution numIndirectCitationsByInfo;
    public Distribution numIndirectSourcesByInfo;

    public JobStatistics(Job job) {
        this.job = job;
        numOperationsByOperationType = new Distribution("Operation Type");
        numRecordsByRecordType = new Distribution("Record Type");
        numInfosByInfoType = new Distribution("Info Type");
        numDirectCitationsByInfo = new Distribution("Direct Citations By Info");
        numIndirectCitationsByInfo = new Distribution("(In)direct Citations By Info");
        numIndirectSourcesByInfo = new Distribution("(In)direct Sources");
    }

    public void calc() {
        for (Operation operation : job.getMemberOperations()) {
            numOperationsByOperationType.add(operation.getType());
        }
        for (Record record : job.getJobRecord().getRecords()) {
            numRecordsByRecordType.add(record.getAttr(RecordInfo.InfoName));
        }
        for (Operation operation : job.getMemberOperations()) {
            for (Info info : operation.getInfos()) {

                numInfosByInfoType.add(info.getName());
                numDirectCitationsByInfo.add(String.format("%s(%s)", info.getName(), info.getUuid()), 0);
                List<String> directReferences = getDirectReference(job, info);
                for (String directReference : directReferences) {
                    numDirectCitationsByInfo.add(directReference);
                }
                numIndirectCitationsByInfo.add(String.format("%s(%s)", info.getName(), info.getUuid()), 0);
                List<String> indirectReferences = getIndirectReference(job, info);
                numIndirectSourcesByInfo.add(String.format("%s(%s)", info.getName(), info.getUuid()), indirectReferences.size());
                for (String indirectReference : indirectReferences) {
                    numIndirectCitationsByInfo.add(indirectReference);
                }

            }
        }

        System.out.println(numRecordsByRecordType.exporyBasic());
        System.out.println(numOperationsByOperationType.exporyBasic());
        System.out.println(numInfosByInfoType.exporyBasic());
        System.out.println(numDirectCitationsByInfo.exporyBasic());
        System.out.println(numIndirectCitationsByInfo.exporyBasic());
        System.out.println(numIndirectSourcesByInfo.exporyBasic());

    }

    public List<String> getDirectReference(Job job, Info info) {
        List<String> directReference = new ArrayList<>();
        for (Source source : info.getSources()) {
            if(source instanceof InfoSource) {
                for (String uuid : ((InfoSource) source).getInfoUuids()) {
                    for (Operation operation : job.getMemberOperations()) {
                        for (Info sourceInfo : operation.getInfos()) {
                            if(sourceInfo.getUuid().equals(uuid)) {
                                directReference.add(String.format("%s(%s)", sourceInfo.getName(), sourceInfo.getUuid()));
                            }
                        }
                    }
                }
            }
        }
        return directReference;
    }

    public List<String> getIndirectReference(Job job, Info info) {
        List<String> indirectReference = new ArrayList<>();
        for (Source source : info.getSources()) {
            if(source instanceof InfoSource) {
                for (String uuid : ((InfoSource) source).getInfoUuids()) {
                    for (Operation operation : job.getMemberOperations()) {
                        for (Info sourceInfo : operation.getInfos()) {
                            if(sourceInfo.getUuid().equals(uuid)) {
                                indirectReference.add(String.format("%s(%s)", sourceInfo.getName(), sourceInfo.getUuid()));
                                indirectReference.addAll(getIndirectReference(job, sourceInfo));
                            }
                        }
                    }
                }
            }
        }
        return indirectReference;
    }

    protected class Distribution {

        protected Map<String, Long> map;
        protected String name;

        public Distribution(String name) {
            this.name = name;
            map = new LinkedHashMap<>();
        }

        public void add(String name, long num) {
            if(map.containsKey(name)) {
                map.put(name, map.get(name) + num);
            } else {
                map.put(name, num);
            }
        }

        public void add(String name) {
            add(name, 1);
        }

        public String toString() {
            String res = String.format("[%s : (%s) total: %s]", name, map.values().size(), total());
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                res += '\n' + String.format(" %s : %s", entry.getKey(), entry.getValue());
            }
            res += '\n';
            return res;
        }

        public String exporyBasic() {
            String res = String.format("[%s : %s]", name, map.values().size());
            Map<Long, Long> histo = new LinkedHashMap<>();
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                if(histo.containsKey(entry.getValue())) {
                    histo.put(entry.getValue(), histo.get(entry.getValue()) + 1l);
                } else {
                    histo.put(entry.getValue(), 1l);
                }
            }
            res += String.format("(%s, %s) ", "total", total());
            for (Map.Entry<Long, Long> entry : histo.entrySet()) {
                res += '\n' + String.format("%s, %s ", entry.getKey(), entry.getValue());
            }
            return res;
        }

        public long get(String name) {
            if(map.containsKey(name)) {
                return map.get(name);
            } else {
                throw new IllegalStateException();
            }
        }

        public long total() {
            long total = 0;
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                total += entry.getValue();
            }
            return total;
        }
    }
}
