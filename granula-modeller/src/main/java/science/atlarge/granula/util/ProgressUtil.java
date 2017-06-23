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

package science.atlarge.granula.util;

import java.util.Date;

/**
 * Created by wing on 18-8-15.
 */
public class ProgressUtil {
    private long startTime;
    private long endTime;

    private long overallTime;

    private long assemblingTime;
    private long writingTime;


    public void displayProcess() {



        System.out.println(String.format("Archiving workload at: %s.", new Date(startTime)));
        System.out.println(String.format("Archived workload at: %s.", new Date(endTime)));

        System.out.println(String.format("Archive assembling takes %.1f seconds.", (assemblingTime - startTime) / 1000.0d));
        System.out.println(String.format("Archive writing takes %.1f seconds.", (writingTime - assemblingTime) / 1000.0d));
        System.out.println(String.format("Archiving takes %.1f seconds.", (endTime - startTime) / 1000.0d));

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getOverallTime() {
        return overallTime;
    }

    public void setOverallTime(long overallTime) {
        this.overallTime = overallTime;
    }

    public long getAssemblingTime() {
        return assemblingTime;
    }

    public void setAssemblingTime(long assemblingTime) {
        this.assemblingTime = assemblingTime;
    }

    public long getWritingTime() {
        return writingTime;
    }

    public void setWritingTime(long writingTime) {
        this.writingTime = writingTime;
    }
}
