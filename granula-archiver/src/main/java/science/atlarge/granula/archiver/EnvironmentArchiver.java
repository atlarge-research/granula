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

import science.atlarge.granula.modeller.entity.Execution;
import science.atlarge.granula.util.FileUtil;
import science.atlarge.granula.util.json.JsonUtil;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by wlngai on 6/20/16.
 */
public class EnvironmentArchiver {

    private int collectTimeout = 200000;


    public void buildArchive(Execution execution) {

        Path envLogPath = Paths.get(execution.getLogPath()).resolve("environment");
        collectEnvData(envLogPath, execution.getJobId());

        Path envArcPath =  Paths.get(execution.getArcPath()).resolve("data").resolve("env-arc.js");
        String envData = "var jobMetrics = " + JsonUtil.toJson(createMetrics(envLogPath.resolve(execution.getJobId())));
        envData = envData.replaceAll("\\{\"key", "\n\\{\"key");
        FileUtil.writeFile(envData, envArcPath);
    }



    public void collectEnvData(Path outpath, String jobId) {

        try {
            CommandLine commandLine = new CommandLine("/var/scratch/wlngai/graphalytics-runner/debug/app/granula/sh/collect-data.sh");
            commandLine.addArgument(jobId);
            commandLine.addArgument(outpath.toAbsolutePath().toString());
            executeCommand(commandLine);

            System.out.println(commandLine.toString());

            Path successFile = outpath.resolve(jobId).resolve("success");
            waitForEnvLogs(successFile);
        } catch (Exception e) {
            System.out.println("Failed to collect monitoring data.");
        }
    }

    private void executeCommand(CommandLine commandLine) {
        Executor executor = new DefaultExecutor();
        executor.setExitValues(null);
        try {
            executor.execute(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void waitForEnvLogs(Path successFile) {
        try {
            boolean waitRetrieval = true;
            long waitedTime = 0;
            while(waitRetrieval) {
                if(successFile.toFile().exists() || waitedTime > collectTimeout) {
                    waitRetrieval = false;
                }
                Thread.sleep(2000);
                waitedTime += 2000;
                System.out.println("Waiting for monitoring data.");
            }

            if(successFile.toFile().exists()) {
                System.out.println("Retrieval completed");
            } else {
                System.out.println("Retrieval not completed. File not found " + successFile.toAbsolutePath().toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<MetricData> createMetrics(Path inputPath) {


        List<MetricData> metricDatas = new ArrayList<>();

        String rootPath = inputPath.toAbsolutePath().toString();
        Collection files = FileUtils.listFiles(new File(rootPath), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);

        long envLogFileSize = 0;
        for (Object f : files) {
            Path filePath = ((File) f).toPath();
            if (Files.isRegularFile(filePath) && !filePath.getFileName().toString().equals("success")
                    ) {
                envLogFileSize++;
                MetricData metricData = new MetricData();
                metricData.key = filePath.toAbsolutePath().toString().replaceAll(rootPath, "");
                if(metricData.key.startsWith("/")) {
                    metricData.key = metricData.key.substring(1, metricData.key.length());
                }

                try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] dp = line.split("\\s+");
                        metricData.addValue(dp[0], dp[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                metricDatas.add(metricData);
            }

        }
        System.out.println(String.format("Parsed %s environment files", envLogFileSize));

        return metricDatas;
    }

    private class MetricData {
        String key;
        List values;

        public MetricData() {
            values = new ArrayList<>();
        }

        public void addValue(String timestamp, String value) {
            values.add(Arrays.asList(timestamp, value));
        }
    }

}
