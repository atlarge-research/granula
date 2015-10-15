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

import nl.tudelft.pds.granula.archiver.entity.info.*;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.modeller.model.operation.AbstractOperationModel;
import nl.tudelft.pds.granula.modeller.rule.derivation.BasicSummaryDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.DerivationRule;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.DurationDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialEndTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.derivation.time.FilialStartTimeDerivation;
import nl.tudelft.pds.granula.modeller.rule.filling.UniqueOperationFilling;
import nl.tudelft.pds.granula.modeller.rule.linking.EmptyLinking;
import nl.tudelft.pds.granula.modeller.rule.visual.MainInfoTableVisualization;
import nl.tudelft.pds.granula.modeller.rule.visual.TableVisualization;
import nl.tudelft.pds.granula.modeller.giraph.GiraphType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopActorTopMission extends AbstractOperationModel {

    public TopActorTopMission() {
        super(GiraphType.TopActor, GiraphType.TopMission);
    }

    public void loadRules() {
        super.loadRules();

        addLinkingRule(new EmptyLinking());
        addFillingRule(new UniqueOperationFilling(2, GiraphType.AppMaster, GiraphType.Deployment));
        addFillingRule(new UniqueOperationFilling(2, GiraphType.AppMaster, GiraphType.BspExecution));
        addFillingRule(new UniqueOperationFilling(2, GiraphType.AppMaster, GiraphType.Undeployment));

        addInfoDerivation(new FilialStartTimeDerivation(6));
        addInfoDerivation(new FilialEndTimeDerivation(6));
        addInfoDerivation(new DurationDerivation(6));
        addInfoDerivation(new SummaryDerivation(10));
        addInfoDerivation(new BspTimeDerivation(7));
        addInfoDerivation(new BspRatioDerivation(8));
        addInfoDerivation(new ResourceAllocTimeDerivation(7));
        addInfoDerivation(new ResourceAllocRatioDerivation(8));
        addInfoDerivation(new CoordinationTimeDerivation(7));
        addInfoDerivation(new CoordinationRatioDerivation(8));
        addInfoDerivation(new IOTimeDerivation(7));
        addInfoDerivation(new IORatioDerivation(8));
        addInfoDerivation(new ArchivingTimeDerivation(1));
        addInfoDerivation(new JobStartTimeDerivation(7));
        addInfoDerivation(new ComputationClassDerivation(2));
        addInfoDerivation(new DatasetDerivation(2));
        addInfoDerivation(new ContainersLoadedDerivation(2));
        addInfoDerivation(new ContainerSizeDerivation(2));
        addVisualDerivation(new MainInfoTableVisualization(1,
                new ArrayList<String>() {{

                }}));
        addVisualDerivation(new TableVisualization(1, "GeneralInfoTable",
                new ArrayList<String>() {{
                    add("ComputationClass");
                    add("Dataset");
                    add("ContainersLoaded");
                    add("ContainerSize");
                    add("JobStartTime");
                    add("ArchivingTime");
                }}));
        addVisualDerivation(new TableVisualization(1, "ExecutionTimeTable",
                new ArrayList<String>() {{
                    add("BspTime");
                    add("BspRatio");
                    add("ResourceAllocTime");
                    add("ResourceAllocRatio");
                    add("CoordinationTime");
                    add("CoordinationRatio");
                    add("IOTime");
                    add("IORatio");
                }}));
    }

    protected class SummaryDerivation extends BasicSummaryDerivation {

        public SummaryDerivation(int level) { super(level); }

        @Override
        public boolean execute() {
                Operation operation = (Operation) entity;
                String summary = String.format("The [%s] operation is the top-level operation of each Giraph job. " +
                        "In Giraph, this operation starts when the AppMaster is initialized, " +
                        "and ends when the AppMaster is terminated. " +
                        "It contains 3 child operations: Deployment, BspExecution and Undeployment. ", operation.getName());
                summary += getBasicSummary(operation);

                SummaryInfo summaryInfo = new SummaryInfo("Summary");
                summaryInfo.setValue("A Summary");
                summaryInfo.addSummary(summary, new ArrayList<Source>());
                operation.addInfo(summaryInfo);
                return  true;
        }
    }


    protected class JobStartTimeDerivation extends DerivationRule {

        public JobStartTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info startTimeInfo = operation.getInfo("StartTime");

            List<Source> sources = new ArrayList<>();
            sources.add(new InfoSource(startTimeInfo.getName(), startTimeInfo));

            BasicInfo jobStartTimeInfo = new BasicInfo("JobStartTime");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            jobStartTimeInfo.setDescription(String.format("The [%s] is the time when this job is started", jobStartTimeInfo.getName()));
            jobStartTimeInfo.addInfo(String.valueOf(dateFormat.format(new Date(Long.parseLong(startTimeInfo.getValue())))), new ArrayList<Source>());
            operation.addInfo(jobStartTimeInfo);

            return true;
        }
    }

    protected class ArchivingTimeDerivation extends DerivationRule {

        public ArchivingTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            BasicInfo archivingTimeInfo = new BasicInfo("ArchivingTime");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            archivingTimeInfo.setDescription(String.format("The [%s] is the time when this performance archive is created", archivingTimeInfo.getName()));
            archivingTimeInfo.addInfo(String.valueOf(dateFormat.format(new Date(System.currentTimeMillis()))), new ArrayList<Source>());
            operation.addInfo(archivingTimeInfo);

            return true;
        }
    }

    protected class BspTimeDerivation extends DerivationRule {

        public BspTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info bspDurationInfo = null;

            List<Source> sources = new ArrayList<>();
            for (Operation suboperation : operation.getChildren()) {
                if(suboperation.getMission().getType().equals(GiraphType.BspExecution)) {
                    for (Operation subSuboperation : suboperation.getChildren()) {
                        if(subSuboperation.getMission().getType().equals(GiraphType.BspIteration)) {
                            bspDurationInfo = subSuboperation.getInfo("Duration");
                        }
                    }
                }
            }

            sources.add(new InfoSource(bspDurationInfo.getName(), bspDurationInfo));

            BasicInfo bspTimeInfo = new BasicInfo("BspTime");
            long bspTime = Long.parseLong(bspDurationInfo.getValue());
            bspTimeInfo.setDescription(String.format("The [%s] is the execution time used for the actual BSP supersteps. ", bspTimeInfo.getName()));
            bspTimeInfo.addInfo(String.valueOf(bspTime), sources);
            operation.addInfo(bspTimeInfo);

            return true;
        }
    }

    protected class BspRatioDerivation extends DerivationRule {

        public BspRatioDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info totalDurationInfo = operation.getInfo("Duration");
            Info bspTimeInfo = operation.getInfo("BspTime");

            List<Source> sources = new ArrayList<>();
            sources.add(new InfoSource(bspTimeInfo.getName(), bspTimeInfo));
            sources.add(new InfoSource("TotalDuration", totalDurationInfo));

            BasicInfo bspRatioInfo = new BasicInfo("BspRatio");
            double bspRatio = Long.parseLong(bspTimeInfo.getValue()) * 1.0d / Long.parseLong(totalDurationInfo.getValue());
            bspRatioInfo.setDescription(String.format("The [%s] is the proportion of the total execution time used for the actual BSP supersteps, " +
                    "which is equal to [%s] / [%s].", bspRatioInfo.getName(), sources.get(0).getName(), sources.get(1).getName()));
            bspRatioInfo.addInfo(String.valueOf(String.format("%.2f", bspRatio)), sources);
            operation.addInfo(bspRatioInfo);

            return true;
        }
    }




    protected class ResourceAllocRatioDerivation extends DerivationRule {

        public ResourceAllocRatioDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;


            Info totalDurationInfo = operation.getInfo("Duration");
            Info allocDurationInfo = operation.getInfo("ResourceAllocTime");
            List<Source> sources = new ArrayList<>();

            sources.add(new InfoSource(allocDurationInfo.getName(), allocDurationInfo));
            sources.add(new InfoSource("TotalDuration", totalDurationInfo));

            BasicInfo allocRatioInfo = new BasicInfo("ResourceAllocRatio");
            double allocRatio = (Long.parseLong(allocDurationInfo.getValue()) * 1.0d) / Long.parseLong(totalDurationInfo.getValue());
            allocRatioInfo.addInfo(String.valueOf(String.format("%.2f", allocRatio)), sources);
            allocRatioInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for resource allocation, " +
                    "which is equal to [%s] / [%s].", allocRatioInfo.getName(), sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(allocRatioInfo);

            return true;
        }
    }


    protected class ResourceAllocTimeDerivation extends DerivationRule {

        public ResourceAllocTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info deploymentDurationInfo = null;
            Info undeploymentDurationInfo = null;

            List<Source> sources = new ArrayList<>();

            for (Operation suboperation : operation.getChildren()) {
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.Deployment)) {
                    deploymentDurationInfo = suboperation.getInfo("Duration");
                }
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.Undeployment)) {
                    undeploymentDurationInfo = suboperation.getInfo("Duration");

                }
            }

            sources.add(new InfoSource("DeploymentDuration", deploymentDurationInfo));
            sources.add(new InfoSource("UndeploymentDuration", undeploymentDurationInfo));

            BasicInfo allocTimeInfo = new BasicInfo("ResourceAllocTime");
            long allocRatio = Long.parseLong(deploymentDurationInfo.getValue()) + Long.parseLong(undeploymentDurationInfo.getValue());
            allocTimeInfo.addInfo(String.valueOf(allocRatio), sources);
            allocTimeInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for resource allocation, " +
                            "which is equal to ([%s] + [%s]). ", allocTimeInfo.getName(),
                    sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(allocTimeInfo);

            return true;
        }
    }


    protected class CoordinationRatioDerivation extends DerivationRule {

        public CoordinationRatioDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;


            Info totalDurationInfo = operation.getInfo("Duration");
            Info coordTimeInfo = operation.getInfo("CoordinationTime");
            List<Source> sources = new ArrayList<>();

            sources.add(new InfoSource(coordTimeInfo.getName(), coordTimeInfo));
            sources.add(new InfoSource("TotalDuration", totalDurationInfo));

            BasicInfo coordRatioInfo = new BasicInfo("CoordinationRatio");
            double allocRatio = (Long.parseLong(coordTimeInfo.getValue()) * 1.0d) / Long.parseLong(totalDurationInfo.getValue());
            coordRatioInfo.addInfo(String.valueOf(String.format("%.2f", allocRatio)), sources);
            coordRatioInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for BSP coordination, " +
                    "which is equal to [%s] / [%s].", coordRatioInfo.getName(), sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(coordRatioInfo);

            return true;
        }
    }


    protected class CoordinationTimeDerivation extends DerivationRule {

        public CoordinationTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info bspSetupDurationInfo = null;
            Info bspCleanupDurationInfo = null;

            List<Source> sources = new ArrayList<>();

            for (Operation suboperation : operation.getChildren()) {
                if(suboperation.getMission().getType().equals(GiraphType.BspExecution)) {
                    for (Operation subSuboperation : suboperation.getChildren()) {
                        if(subSuboperation.hasType(GiraphType.BspMaster, GiraphType.BspSetup)) {
                            bspSetupDurationInfo = subSuboperation.getInfo("Duration");
                        }
                        if(subSuboperation.hasType(GiraphType.BspMaster, GiraphType.BspCleanup)) {
                            bspCleanupDurationInfo = subSuboperation.getInfo("Duration");

                        }
                    }
                }
            }

            sources.add(new InfoSource("BspSetupDuration", bspSetupDurationInfo));
            sources.add(new InfoSource("BspCleanupDuration", bspCleanupDurationInfo));

            BasicInfo allocTimeInfo = new BasicInfo("CoordinationTime");
            long allocRatio = Long.parseLong(bspSetupDurationInfo.getValue()) + Long.parseLong(bspCleanupDurationInfo.getValue());
            allocTimeInfo.addInfo(String.valueOf(allocRatio), sources);
            allocTimeInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for BSP coordination, " +
                            "which is equal to ([%s] + [%s]). ", allocTimeInfo.getName(),
                    sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(allocTimeInfo);

            return true;
        }


    }


    protected class IORatioDerivation extends DerivationRule {

        public IORatioDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;


            Info totalDurationInfo = operation.getInfo("Duration");
            Info ioTimeInfo = operation.getInfo("IOTime");
            List<Source> sources = new ArrayList<>();

            sources.add(new InfoSource(ioTimeInfo.getName(), ioTimeInfo));
            sources.add(new InfoSource("TotalDuration", totalDurationInfo));

            BasicInfo coordRatioInfo = new BasicInfo("IORatio");
            double allocRatio = (Long.parseLong(ioTimeInfo.getValue()) * 1.0d) / Long.parseLong(totalDurationInfo.getValue());
            coordRatioInfo.addInfo(String.valueOf(String.format("%.2f", allocRatio)), sources);
            coordRatioInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for IO, " +
                    "which is equal to [%s] / [%s].", coordRatioInfo.getName(), sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(coordRatioInfo);

            return true;
        }
    }

    protected class IOTimeDerivation extends DerivationRule {

        public IOTimeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {
            Operation operation = (Operation) entity;

            Info dataLoadDuration = null;
            Info dataOffloadTimeInfo = null;

            List<Source> sources = new ArrayList<>();

            for (Operation suboperation : operation.getChildren()) {
                if(suboperation.getMission().getType().equals(GiraphType.BspExecution)) {
                    for (Operation subSuboperation : suboperation.getChildren()) {
                        if(subSuboperation.hasType(GiraphType.BspMaster, GiraphType.BspSetup)) {
                            for (Operation subsubsubOperation : subSuboperation.getChildren()) {
                                if(subsubsubOperation.hasType(GiraphType.GlobalCoordinator, GiraphType.GlobalDataload)) {
                                    dataLoadDuration = subsubsubOperation.getInfo("Duration");
                                }
                            }
                        }

                        if(subSuboperation.hasType(GiraphType.BspMaster, GiraphType.BspCleanup)) {
                            dataOffloadTimeInfo = subSuboperation.getInfo("DataOffloadTime");
                        }
                    }
                }
            }

            sources.add(new InfoSource("DataLoadTime", dataLoadDuration));
            sources.add(new InfoSource("DataOffloadTime", dataOffloadTimeInfo));

            BasicInfo ioTimeInfo = new BasicInfo("IOTime");
            long allocRatio = Long.parseLong(dataLoadDuration.getValue()) + Long.parseLong(dataOffloadTimeInfo.getValue());
            ioTimeInfo.addInfo(String.valueOf(allocRatio), sources);
            ioTimeInfo.setDescription(String.format("[%s] is the proportion of the total execution time used for IO, " +
                            "which is equal to ([%s] + [%s]). ", ioTimeInfo.getName(),
                    sources.get(0).getName(), sources.get(1).getName()));
            operation.addInfo(ioTimeInfo);

            return true;
        }


    }

    protected class ComputationClassDerivation extends DerivationRule {

        public ComputationClassDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            List<Source> sources = new ArrayList<>();

            Operation bspIteration = null;
            for(Operation suboperation: operation.getChildren()) {
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.BspExecution)) {
                    for (Operation subsuboperation : suboperation.getChildren()) {
                        if (subsuboperation.hasType(GiraphType.BspMaster, GiraphType.BspIteration)) {
                            bspIteration = subsuboperation;
                        }
                    }
                }
            }

            Info computeClassInfo = bspIteration.getInfo("ComputationClass");
            sources.add(new InfoSource("ComputationClass", computeClassInfo));

            BasicInfo computationClassLocalInfo = new BasicInfo("ComputationClass");
            computationClassLocalInfo.setDescription(String.format("The [%s] is the ComputationClass used for this job.", computationClassLocalInfo.getName()));

            computationClassLocalInfo.addInfo(computeClassInfo.getValue(), sources);
            operation.addInfo(computationClassLocalInfo);
            return  true;
        }
    }

    protected class DatasetDerivation extends DerivationRule {

        public DatasetDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            List<Source> sources = new ArrayList<>();

            Operation bspIteration = null;
            for(Operation suboperation: operation.getChildren()) {
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.BspExecution)) {
                    for (Operation subsuboperation : suboperation.getChildren()) {
                        if (subsuboperation.hasType(GiraphType.BspMaster, GiraphType.BspIteration)) {
                            bspIteration = subsuboperation;
                        }
                    }
                }
            }

            Info dataInputPathInfo = bspIteration.getInfo("DataInputPath");
            String dataset = new File(dataInputPathInfo.getValue()).getName();
            sources.add(new InfoSource(dataInputPathInfo.getName(), dataInputPathInfo));

            BasicInfo datasetInfo = new BasicInfo("Dataset");
            datasetInfo.setDescription(String.format("The [%s] is the Dataset used for this job.", datasetInfo.getName()));

            datasetInfo.addInfo(dataset, sources);
            operation.addInfo(datasetInfo);
            return  true;
        }
    }



    protected class ContainersLoadedDerivation extends DerivationRule {

        public ContainersLoadedDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            List<Source> sources = new ArrayList<>();

            Operation containerAssignment = null;
            for(Operation suboperation: operation.getChildren()) {
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.Deployment)) {
                    for (Operation subsuboperation : suboperation.getChildren()) {
                        if(subsuboperation.hasType(GiraphType.AppMaster, GiraphType.ContainerAssignment)) {
                            containerAssignment = subsuboperation;
                        }
                    }
                }
            }

            Info containersLoaded = containerAssignment.getInfo("NumContainers");
            sources.add(new InfoSource(containersLoaded.getName(), containersLoaded));

            BasicInfo containersLoadedLInfo = new BasicInfo("ContainersLoaded");
            containersLoadedLInfo.setDescription(
                    String.format("The [%s] is the number of Yarn containers used for this job.",
                            containersLoadedLInfo.getName()));

            containersLoadedLInfo.addInfo(containersLoaded.getValue(), sources);
            operation.addInfo(containersLoadedLInfo);

            return  true;

        }
    }

    protected class ContainerSizeDerivation extends DerivationRule {

        public ContainerSizeDerivation(int level) {
            super(level);
        }

        @Override
        public boolean execute() {

            Operation operation = (Operation) entity;

            List<Source> sources = new ArrayList<>();

            Operation containerAssignment = null;
            for(Operation suboperation: operation.getChildren()) {
                if(suboperation.hasType(GiraphType.AppMaster, GiraphType.Deployment)) {
                    for (Operation subsuboperation : suboperation.getChildren()) {
                        if(subsuboperation.hasType(GiraphType.AppMaster, GiraphType.ContainerAssignment)) {
                            containerAssignment = subsuboperation;
                        }
                    }
                }
            }

            Info containerHeapSize = containerAssignment.getInfo("ContainerHeapSize");
            sources.add(new InfoSource(containerHeapSize.getName(), containerHeapSize));

            BasicInfo containerSizeLInfo = new BasicInfo("ContainerSize");
            containerSizeLInfo.setDescription(
                    String.format("The [%s] is the heap size of Yarn containers used for this job.",
                            containerSizeLInfo.getName()));

            containerSizeLInfo.addInfo(containerHeapSize.getValue(), sources);
            operation.addInfo(containerSizeLInfo);

            return  true;

        }
    }
}
