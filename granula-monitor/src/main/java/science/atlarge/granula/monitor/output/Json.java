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
package science.atlarge.granula.monitor.output;

import science.atlarge.granula.monitor.util.FileUtil;
import science.atlarge.granula.monitor.util.json.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by wlngai on 5/30/16.
 */
public class Json {

    public static void main(String[] args) {
        Json json = new Json();


        System.out.println(JsonUtil.toJson(json.run()));
        Path datajs =  Paths.get("/home/wlngai/Workstation/Repo/tudelft-atlarge/granula-viz/jobarchive-ut.js");
        FileUtil.writeFile("var jobdata = " + JsonUtil.toJson(json.run()),datajs);

    }

    public static List<MetricOutput> run() {


        List<MetricOutput> metricOutputs = new ArrayList<>();

        String rootPath = "/media/sf_Large/Large/runner/result/b5759532406-pr-twitter/JOB_UTIL/";
        try {
            Files.walk(Paths.get(rootPath)).forEach(filePath -> {
                if (Files.isRegularFile(filePath) && !filePath.getFileName().toString().equals("success")
                        && filePath.toString().contains("1000ms")) {

                    MetricOutput metricOutput = new MetricOutput();
                    metricOutput.key = filePath.toAbsolutePath().toString().replaceAll(rootPath, "");

                    try (Stream<String> stream = Files.lines(filePath)) {
                        stream.forEach(line -> {
                            String[] dp = line.split("\\s+");
                            metricOutput.addValue(dp[0], dp[1]);
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    metricOutputs.add(metricOutput);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metricOutputs;
    }
}
