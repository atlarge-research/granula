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

package nl.tudelft.pds.granula.util;

import java.io.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;

public class TarExtractor {

    public static void main(String[] args) throws IOException {

        String workloadName = "Workload20150130004448";
        String tarFilePath = String.format("%s.tar.gz", workloadName);
        String outputDir = "/tmp/Granula/Log/" + workloadName;
        File outputDirFile = new File(outputDir);
        FileUtils.deleteDirectory(outputDirFile);
        outputDirFile.mkdirs();
        TarExtractor tarExtractor = new TarExtractor();
        tarExtractor.extract(tarFilePath, outputDir);

        String gangliaTarFile = String.format("%s/%s_GangliaLog.tar.gz", outputDir, workloadName);
        String gangliaOutputDir = String.format("%s/GangliaLog", outputDir);
        tarExtractor.extract(gangliaTarFile, gangliaOutputDir);
        FileUtils.forceDelete(new File(gangliaTarFile));

        String yarnTarFile = String.format("%s/%s_YarnLog.tar.gz", outputDir, workloadName);
        String yarnOutputDir = String.format("%s/YarnLog", outputDir);
        tarExtractor.extract(yarnTarFile,yarnOutputDir);
        FileUtils.forceDelete(new File(yarnTarFile));

        String runnerTarFile = String.format("%s/%s_RunnerLog.tar.gz", outputDir, workloadName);
        String runnerOutputDir = String.format("%s/RunnerLog", outputDir);
        tarExtractor.extract(runnerTarFile,runnerOutputDir);
        FileUtils.forceDelete(new File(runnerTarFile));
    }

    public void extract(String tarFilePath, String outputDir) {

        try {
            FileInputStream fin = new FileInputStream(tarFilePath);

            BufferedInputStream in = new BufferedInputStream(fin);
            GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
            TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);

            TarArchiveEntry entry = null;

            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {
                    File f = new File(String.format("%s/%s", outputDir, entry.getName()));
                    f.mkdirs();
                } else {
                    int BUFFER = 2048;
                    int count;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(String.format("%s/%s", outputDir, entry.getName()));
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.close();
                }
            }
            tarIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
