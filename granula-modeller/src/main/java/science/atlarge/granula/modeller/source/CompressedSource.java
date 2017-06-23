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

package science.atlarge.granula.modeller.source;

import science.atlarge.granula.util.TarExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wing on 30-1-15.
 */
public class CompressedSource extends Source {

    String tarFilePath;
    String tmpDirPath;
    String format;


    public CompressedSource(String tarFilePath, String tmpDirPath, String format) {

        this.tmpDirPath = tmpDirPath;
        this.tarFilePath = tarFilePath;
        this.format = format;
    }

    @Override
    public void verify() {
        if(!(new File(tarFilePath).exists())) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void load() {
        try {
            File outputDirFile = new File(tmpDirPath);
            FileUtils.deleteDirectory(outputDirFile);
            outputDirFile.mkdirs();

            switch (format) {
                case "tar" :
                    TarExtractor tarExtractor = new TarExtractor();
                    tarExtractor.extract(tarFilePath, tmpDirPath);
                    break;
                default:
                    throw new IllegalArgumentException();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
