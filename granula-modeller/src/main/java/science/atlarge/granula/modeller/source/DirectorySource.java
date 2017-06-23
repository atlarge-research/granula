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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 24-8-15.
 */
public class DirectorySource extends DataSource {

    String filepath;


    public DirectorySource(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void verify() {
        if(!(new File(filepath).exists())) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void load() {

        dataStreams = new ArrayList<>();

        List<File> files;
        try {
            files = new ArrayList<>(FileUtils.listFilesAndDirs(new File(filepath), TrueFileFilter.TRUE, TrueFileFilter.TRUE));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("%s cannot be load, as it is not a directory.", filepath));
        }

        for (File file : files) {
            if(file.isFile()) {
                try {
                    dataStreams.add(new DataStream(file.getAbsolutePath(), new FileInputStream(file)));
                } catch (FileNotFoundException e) {
                    System.out.println(String.format("Log file %s was specfified in the data source, but it cannot be found.", file));
                    throw new IllegalStateException();
                }
            }
        }
    }


    public String getFilepath() {
        return filepath;
    }
}
