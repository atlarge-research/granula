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

import java.io.File;

/**
 * Created by wing on 24-8-15.
 */
public class JobDirectorySource extends JobSource {

    String dirPath;

    public JobDirectorySource(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void verify() {
        super.verify();
    }

    @Override
    public void load() {
        setSysSource(new DirectorySource(new File(dirPath).getAbsolutePath() + "/platform"));
        setEnvSource(new DirectorySource(new File(dirPath).getAbsolutePath() +  "/environment"));
        super.load();
    }
}
