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

package science.atlarge.granula.modeller.platform.visual;

import science.atlarge.granula.modeller.entity.BasicType;
import science.atlarge.granula.modeller.platform.info.Info;

import java.util.ArrayList;
import java.util.List;

public class TableVisual extends Visual {

    List<String> infoIds;

    private TableVisual() {
        super("unspecified", BasicType.TableVisual);
    }

    public TableVisual(String name) {
        super(name, BasicType.TableVisual);
        infoIds = new ArrayList<>();
    }

    public void addInfo(Info info) {
        infoIds.add(info.getUuid());
    }


}
