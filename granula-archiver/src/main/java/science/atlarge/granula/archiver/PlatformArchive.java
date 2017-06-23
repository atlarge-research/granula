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
package science.atlarge.granula.archiver;

import science.atlarge.granula.util.FileUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlatformArchive {
    Map<String, JSONObject> operations;
    Map<String, JSONObject> actors;
    Map<String, JSONObject> missions;
    Map<String, JSONObject> infos;

    public static void main(String[] args) {
        String arcDir = "/media/sf_Large/Large/runner/result/r520884/report/report-xxxxx-161109-124831/archive/r520039";
        PlatformArchive platformArchive = readArchive(Paths.get(arcDir));

        JSONObject processGraph = platformArchive.operation("ProcessGraph");
        Double procTime = Double.parseDouble(platformArchive.info(processGraph, "Duration"));
    }

    public static PlatformArchive readArchive(Path arcPath) {
        try {
            Path platArchive = arcPath.resolve("data").resolve("sys-arc.js");
            if(platArchive.toFile().exists()) {
                String jsonString = FileUtil.readFile(platArchive).replace("var jobOperations = ", "");
                return fromJson(jsonString);
            } else {
                throw new IllegalStateException("Archive data does not exists: "+ platArchive.toString());
            }
        } catch (Exception e) {
//            e.printStackTrace();
            throw new IllegalStateException("Cannot read archive due to " + e.toString());
        }
    }


    public String info(JSONObject operation, String infoName) {
        String infoId = ((JSONObject)operation.get("infoIds")).get(infoName).toString();
        return infos.get(infoId).get("value").toString();
    }


    public JSONObject operation(String missionType) {
        JSONObject targetOperation = null;
        for (JSONObject operationJ : operations.values()) {
            String mId = (String) operationJ.get("missionId");
            JSONObject mission = missions.get(mId);
            if(mission.get("name").toString().equals(missionType)) {
                targetOperation = operationJ;
            }
        }
        return targetOperation;
    }

    public static PlatformArchive fromJson(String jsonString) {

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PlatformArchive platformArchive = new PlatformArchive();

        platformArchive.operations = mapJsonItems((JSONObject) json.get("operations"));
        platformArchive.actors = mapJsonItems((JSONObject) json.get("actors"));
        platformArchive.missions = mapJsonItems((JSONObject) json.get("missions"));
        platformArchive.infos = mapJsonItems((JSONObject) json.get("infos"));

        return platformArchive;
    }


    private static Map<String, JSONObject> mapJsonItems(JSONObject jsonMap) {

        Map<String, JSONObject> map = new HashMap<>();
        for(Iterator iterator = jsonMap.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            JSONObject value = (JSONObject) jsonMap.get(key);
            map.put(key, value);
        }
        return map;
    }
}
