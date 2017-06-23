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
package science.atlarge.granula.monitor.collector;

import science.atlarge.granula.monitor.metric.MetricStore;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ProcFSCollector extends MetricCollector {

    String procFilePath;
    RandomAccessFile reader;

    int processId;

    public boolean init() {
        try {
            if(!Files.exists(Paths.get(procFilePath))) {
                return false;
            }
            reader = new RandomAccessFile(procFilePath, "r");
            status = Status.PREP;
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Cannot read file %s. Check if process with id %s exists", procFilePath, processId));
//            malFunction();
            return false;
        }
    }

    public boolean kill() {
        status = Status.KILLED;
        IOUtils.closeQuietly(reader);
        for (MetricStore metricStore : metricStores.values()) {
            metricStore.kill();
        }
        System.out.println(String.format("Collector %s stopping at %s.",
                this.getClass().getSimpleName(), System.currentTimeMillis()));
        return  true;
    }

    protected void preprocess() throws Exception {
        reader.seek(0);
    }

    protected void postprocess() throws Exception {

    }

    public abstract void collect() throws Exception;

    protected static String collectFromExecution(String program, String parameter) {

        String output = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(baos));
            executor.setExitValues(null);

            CommandLine commandLine = new CommandLine(program);
            commandLine.addArgument(parameter, false);
            executor.execute(commandLine);
            output = baos.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }



//    public static String collectFromExecution(String program, String parameter) {
//
//        String output = null;
//
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            DefaultExecutor executor = new DefaultExecutor();
//            executor.setStreamHandler(new PumpStreamHandler(baos));
//            executor.setExitValues(null);
//
//            CommandLine commandLine = new CommandLine(program);
//            commandLine.addArgument(parameter, false);
//            executor.execute(commandLine);
//            output = baos.toString().trim();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return output;
//    }

//    private void malFunction() {
//        System.out.println(String.format("%s reported to be malfunctioning.", getName()));
////        stop();
//    }

}

