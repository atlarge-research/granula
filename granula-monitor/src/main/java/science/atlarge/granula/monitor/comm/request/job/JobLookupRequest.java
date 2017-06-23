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
package science.atlarge.granula.monitor.comm.request.job;

import science.atlarge.granula.monitor.comm.request.Request;
import science.atlarge.granula.monitor.comm.request.RequestType;

/**
 * Created by wlngai on 9-5-16.
 */
public class JobLookupRequest extends Request {

    String questId;
    String jobId;
    long responseCount;

    public JobLookupRequest(String jobId) {
        type = RequestType.MonitorProcess;
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getQuestId() {
        return questId;
    }

    public long getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(long responseCount) {
        this.responseCount = responseCount;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
