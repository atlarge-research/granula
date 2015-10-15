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

package nl.tudelft.pds.granula.modeller.rule.visual;

import nl.tudelft.pds.granula.archiver.entity.info.Info;
import nl.tudelft.pds.granula.archiver.entity.info.InfoSource;
import nl.tudelft.pds.granula.archiver.entity.operation.Operation;
import nl.tudelft.pds.granula.archiver.entity.visual.TableVisual;

import java.util.List;

public class TableVisualization extends VisualizationRule {

    List<String> keyInfoNames;
    String tableName;
    public TableVisualization(int level, String tableName, List<String> keyInfoNames) {
        super(level);
        this.tableName = tableName;
        this.keyInfoNames = keyInfoNames;
    }

    @Override
    public boolean execute() {
        Operation operation = (Operation) entity;

        TableVisual tableVisual = new TableVisual(tableName);

        for (String keyInfoName : keyInfoNames) {
            Info keyInfo = operation.getInfo(keyInfoName);
            tableVisual.addTableCell(new InfoSource(keyInfo.getName(), keyInfo));
        }

        operation.addVisual(tableVisual);
        return  true;
    }
}
