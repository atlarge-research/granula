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

package nl.tudelft.pds.granula.modeller.giraph.operation;

import nl.tudelft.pds.granula.archiver.entity.info.Source;
import nl.tudelft.pds.granula.archiver.entity.info.SummaryInfo;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;
import nl.tudelft.pds.granula.modeller.model.operation.ConcreteOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.RecordInfoDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.ResourceUtilDerivation;
import nl.tudelft.pds.granula.modeller.rule.linking.UniqueParentLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.EnvTimeSeriesVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TimeSeriesVisualization;

import java.util.ArrayList;

public class BspWorkerWorkerTask extends ConcreteOperationModel {

    public BspWorkerWorkerTask() {
        super(GiraphType.BspWorker, GiraphType.WorkerTask);
    }

    public void loadRules() {
        super.loadRules();
        addLinkingRule(new UniqueParentLinking(GiraphType.BspMaster, GiraphType.BspIteration));
        addInfoDerivation(new RecordInfoDerivation(1, GiraphType.ComputeNode));

        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{
                    add(GiraphType.ComputeNode);
                }}));


        EnvTimeSeriesVisualization cpuUtilVisual = new EnvTimeSeriesVisualization(1, "CpuUtilVisual", "CPU Utilization", "CPU Usage", "%", "", "");
        cpuUtilVisual.addY1Info(GiraphType.cpu_user);
        cpuUtilVisual.addY1Info(GiraphType.cpu_system);
        cpuUtilVisual.addY1Info(GiraphType.cpu_idle);
        cpuUtilVisual.addY1Info(GiraphType.cpu_aidle);
        cpuUtilVisual.addY1Info(GiraphType.cpu_steal);
        cpuUtilVisual.addY1Info(GiraphType.cpu_nice);
        cpuUtilVisual.addY1Info(GiraphType.cpu_wio);
        addVisualDerivation(cpuUtilVisual);


        EnvTimeSeriesVisualization networkUtilVisual = new EnvTimeSeriesVisualization(1, "NetworkUtilVisual", "Network Utilization", "Network Traffic", "B", "Network Traffic", "packets");
        networkUtilVisual.addY1Info(GiraphType.bytes_in);
        networkUtilVisual.addY1Info(GiraphType.bytes_out);
        networkUtilVisual.addY2Info(GiraphType.pkts_in);
        networkUtilVisual.addY2Info(GiraphType.pkts_out);
        addVisualDerivation(networkUtilVisual);

        EnvTimeSeriesVisualization memoryUtilVisual = new EnvTimeSeriesVisualization(1, "MemoryUtilVisual", "Memory Utilization", "Memory Usage", "B", "", "");
        memoryUtilVisual.addY1Info(GiraphType.mem_free);
        memoryUtilVisual.addY1Info(GiraphType.mem_buffers);
        memoryUtilVisual.addY1Info(GiraphType.mem_cached);
        memoryUtilVisual.addY1Info(GiraphType.mem_shared);
//        memoryUtilVisual.addY1Info(GiraphType.mem_total);
        addVisualDerivation(memoryUtilVisual);

        addInfoDerivation(new SummaryDerivation(10));

    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;
            String summary = String.format("The [%s] operation is executed by a BspWorker to carry out a set of Supersteps locally. " +
                            "The resource utilization metrics of the computational node (on which the BspWorker runs) is reported here.",
                    operation.getName());
            summary += getBasicSummary(operation);

            SummaryInfo summaryInfo = new SummaryInfo("Summary");
            summaryInfo.setValue("A Summary");
            summaryInfo.addSummary(summary, new ArrayList<Source>());
            operation.addInfo(summaryInfo);
            return  true;
        }
    }



}
