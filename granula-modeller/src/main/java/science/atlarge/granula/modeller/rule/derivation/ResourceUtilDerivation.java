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

package science.atlarge.granula.modeller.rule.derivation;

import science.atlarge.granula.modeller.platform.operation.Operation;
import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.environment.ComputationNode;
import science.atlarge.granula.modeller.environment.Environment;
import science.atlarge.granula.modeller.platform.info.Source;
import science.atlarge.granula.modeller.platform.info.TimeSeries;
import science.atlarge.granula.modeller.platform.info.TimeSeriesInfo;

import java.util.ArrayList;

/**
 * Created by wing on 13-3-15.
 */
public abstract class ResourceUtilDerivation extends DerivationRule {

    public ResourceUtilDerivation(int level) { super(level); }

    public boolean mapUtilMetricInfo(Operation operation, String utilMtc) {

        Environment env = operation.getEnvironment();

        ComputationNode node = null;
        try {
            node = env.getNode(operation.getInfo(BasicType.ComputeNode).getValue());
        } catch (IllegalStateException e) {
            System.out.println(String.format("Resource utilization metrics of node %s cannot be found.", operation.getInfo(BasicType.ComputeNode).getValue()));
        }

        if(node == null) {
            return false;
        }


        if(!node.hasInfo(utilMtc)) {
            System.out.println(String.format("Node %s does not have resource util info %s", node.getName(), utilMtc));
            return false;};

        TimeSeriesInfo pktsInInfo = (TimeSeriesInfo) node.getInfo(utilMtc);
        pktsInInfo.getTimeSeries();

        long startTime = Long.parseLong(operation.getInfo(BasicType.StartTime).getValue());
        long endTime = Long.parseLong(operation.getInfo(BasicType.EndTime).getValue());

        String localInfoName = utilMtc;
        String localMetricUnit = pktsInInfo.getMetricUnit();
        TimeSeries localTimeSeries = pktsInInfo.getTimeSeries().filterDatapoints(startTime, endTime);
        TimeSeriesInfo localPktsInInfo = new TimeSeriesInfo(localInfoName);
        localPktsInInfo.addInfo(localMetricUnit, localTimeSeries, new ArrayList<Source>());

        operation.addInfo(localPktsInInfo);

        return true;
    }
}
